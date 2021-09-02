package com.jhs.mysqliutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MysqlUtil {
	private static String dbHost;
	private static String dbLoginId;
	private static String dbLoginPw;
	private static String dbName;
	private static boolean isDevMode;

	private static Map<Long, Connection> connections;

	static {
		connections = new HashMap<>();
	}

	// 변수값이 boolean값이 들어올시 인스턴트 변수 isDevMode를 변환시켜주는 메서드
	public static void setDevMode(boolean isDevMode) {
		MysqlUtil.isDevMode = isDevMode;
	}

	// 현재 isDevMode값을 리턴하는 메서드
	public static boolean isDevMode() {
		return isDevMode;
	}

	// 해당 변수를 인스턴트 변수에 저장하는 메서드
	public static void setDBInfo(String dbHost, String dbLoginId, String dbLoginPw, String dbName) {
		MysqlUtil.dbHost = dbHost;
		MysqlUtil.dbLoginId = dbLoginId;
		MysqlUtil.dbLoginPw = dbLoginPw;
		MysqlUtil.dbName = dbName;
	}

	// 연결 닫기
	public static void closeConnection() {
		long currentThreadId = Thread.currentThread().getId();

		if (connections.containsKey(currentThreadId) == false) {
			return;
		}

		Connection connection = connections.get(currentThreadId);

		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		connections.remove(currentThreadId);
	}

	// DB에 연결
	private static Connection getConnection() {
		// 현재 쓰레드 아이디를 넣는다
		long currentThreadId = Thread.currentThread().getId();

		// 이전에 현재 쓰레드를 요청 했는지 확인
		if (connections.containsKey(currentThreadId) == false) {
			// false일 경우 DB연결
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new MysqlUtilException(e);
			}

			Connection connection = null;

			// url를 만든다
			String url = "jdbc:mysql://" + dbHost + "/" + dbName
					+ "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull&connectTimeout=60";
			try {
				// DB 연결
				connection = DriverManager.getConnection(url, dbLoginId, dbLoginPw);
				// currentThreadId, connection 넣는다
				connections.put(currentThreadId, connection);

			} catch (SQLException e) {
				closeConnection();
				throw new MysqlUtilException(e);
			}
		}

		return connections.get(currentThreadId);
	}

	// DB에서 selectRow 값을 가져오는 메서드
	public static <T> T selectRow(SecSql sql) {
		return (T) selectRow(sql, Map.class);
	}

	// DB에서 selectRow 값을 가져오는 메서드
	public static <T> T selectRow(SecSql sql, Class<T> cls) {
		List<T> rows = selectRows(sql, cls);

		if (rows.size() == 0) {
			return null;
		}

		return rows.get(0);
	}

	// DB에서 selectRows 값을 가져오는 메서드
	public static <T> List<T> selectRows(SecSql sql) {
		return (List<T>) selectRows(sql, Map.class);
	}

	// DB에서 selectRows 값을 가져오는 메서드
	public static <T> List<T> selectRows(SecSql sql, Class<T> cls) throws MysqlUtilException {
		ObjectMapper om = new ObjectMapper();
		List<T> rows = new ArrayList<>();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = sql.getPreparedStatement(getConnection());
			rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnSize = metaData.getColumnCount();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();

				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					String columnName = metaData.getColumnName(columnIndex + 1);
					Object value = rs.getObject(columnName);

					if (value instanceof Long) {
						int numValue = (int) (long) value;
						row.put(columnName, numValue);
					} else if (value instanceof Timestamp) {
						String dateValue = value.toString();
						dateValue = dateValue.substring(0, dateValue.length() - 2);
						row.put(columnName, dateValue);
					} else if (value instanceof LocalDateTime) {
						String dateValue = value.toString();
						dateValue = dateValue.replace("T", " ");
						row.put(columnName, dateValue);
					} else {
						row.put(columnName, value);
					}
				}

				if (cls.getSimpleName().equals("Map")) {
					rows.add((T) row);
				} else {
					rows.add((T) om.convertValue(row, cls));
				}
			}
		} catch (SQLException e) {
			closeConnection();
			throw new MysqlUtilException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					closeConnection();
					throw new MysqlUtilException(e);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					closeConnection();
					throw new MysqlUtilException(e);
				}
			}
		}

		return rows;
	}

	// DB의 row값이 정수형일 경우 반환해주는 메서드
	public static int selectRowIntValue(SecSql sql) {
		Map<String, Object> row = selectRow(sql);

		for (String key : row.keySet()) {
			return (int) row.get(key);
		}

		return -1;
	}

	// DB의 row값이 문자형일 경우 반환해주는 메서드
	public static String selectRowStringValue(SecSql sql) {
		Map<String, Object> row = selectRow(sql);

		for (String key : row.keySet()) {
			return (String) row.get(key);
		}

		return "";
	}

	public static boolean selectRowBooleanValue(SecSql sql) {
		Map<String, Object> row = selectRow(sql);

		for (String key : row.keySet()) {
			return ((int) row.get(key)) == 1;
		}

		return false;
	}

	// DB에 row를 생성하는 메서드
	public static int insert(SecSql sql) {
		int id = -1;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = sql.getPreparedStatement(getConnection());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException e) {
			closeConnection();
			throw new MysqlUtilException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					closeConnection();
					throw new MysqlUtilException(e);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					closeConnection();
					throw new MysqlUtilException(e);
				}
			}

		}

		return id;
	}

	// DB의 rows 값을 수정해주는 메서드
	public static int update(SecSql sql) {
		int affectedRows = 0;

		PreparedStatement stmt = null;

		try {
			stmt = sql.getPreparedStatement(getConnection());
			affectedRows = stmt.executeUpdate();
		} catch (SQLException e) {
			closeConnection();
			throw new MysqlUtilException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					closeConnection();
					throw new MysqlUtilException(e);
				}
			}
		}

		return affectedRows;
	}

	// DB에 해당 rows를 삭제하는 메서드
	public static int delete(SecSql sql) {
		return update(sql);
	}
}