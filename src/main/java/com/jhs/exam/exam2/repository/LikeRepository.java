package com.jhs.exam.exam2.repository;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.IsLike;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class LikeRepository implements ContainerComponent {

	@Override
	public void init() {

	}

	public IsLike getLikeByRelTypeCodeAndMemberId(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT L.*");
		sql.append("FROM islike AS L");
		sql.append("WHERE L.relTypeCode = ?", relTypeCode);
		sql.append("AND L.relId = ?", relId);
		sql.append("AND L.memberId = ?", memberId);
		sql.append("AND L.point = 1");
		
		return MysqlUtil.selectRow(sql, IsLike.class);
	}

	public void likeInsert(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO isLike");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", relTypeCode = ?", relTypeCode);
		sql.append(", relId = ?", relId);
		sql.append(", memberId = ?", memberId);
		sql.append(", `point` = 1");

		MysqlUtil.insert(sql);
	}

	public void likeDelete(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM islike");
		sql.append("WHERE relTypeCode = ?", relTypeCode);
		sql.append("AND relId = ?", relId);
		sql.append("AND memberId = ?", memberId);
		sql.append("AND `point` = 1");

		MysqlUtil.delete(sql);
	}

	public IsLike getDisLikeByRelTypeCodeAndMemberId(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT L.*");
		sql.append("FROM islike AS L");
		sql.append("WHERE L.relTypeCode = ?", relTypeCode);
		sql.append("AND L.relId = ?", relId);
		sql.append("AND L.memberId = ?", memberId);
		sql.append("AND L.point = -1");
		
		return MysqlUtil.selectRow(sql, IsLike.class);
	}

	public void disLikeInsert(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO isLike");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", relTypeCode = ?", relTypeCode);
		sql.append(", relId = ?", relId);
		sql.append(", memberId = ?", memberId);
		sql.append(", `point` = -1");

		MysqlUtil.insert(sql);
		
	}

	public void disLikeDelete(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM islike");
		sql.append("WHERE relTypeCode = ?", relTypeCode);
		sql.append("AND relId = ?", relId);
		sql.append("AND memberId = ?", memberId);
		sql.append("AND `point` = -1");

		MysqlUtil.delete(sql);
	}

	public void likeUpdate(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("UPDATE islike");
		sql.append("SET updateDate = NOW()");
		sql.append(", `point` = -1");
		sql.append("WHERE relTypeCode = ?", relTypeCode);
		sql.append("AND relId = ?", relId);
		sql.append("AND memberId = ?", memberId);

		MysqlUtil.update(sql);
	}

	public void disLikeUpdate(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("UPDATE islike");
		sql.append("SET updateDate = NOW()");
		sql.append(", `point` = 1");
		sql.append("WHERE relTypeCode = ?", relTypeCode);
		sql.append("AND relId = ?", relId);
		sql.append("AND memberId = ?", memberId);

		MysqlUtil.update(sql);
	}

	public int getPoint(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT IFNULL(SUM(L.point), 0) AS `point`");
		sql.append("FROM islike AS L");
		sql.append("WHERE 1");
		sql.append("AND L.relTypeCode = ?", relTypeCode);
		sql.append("AND L.relId = ?", relId);
		sql.append("AND L.memberId = ?", memberId);

		return MysqlUtil.selectRowIntValue(sql);
	}
}