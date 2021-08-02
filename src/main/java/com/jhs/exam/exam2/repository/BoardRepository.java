package com.jhs.exam.exam2.repository;

import com.jhs.exam.exam2.dto.Board;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class BoardRepository {
	public void init() {
		
	}
	
	
	public Board getBoardById(int id) {
		SecSql sql = new SecSql();
		sql.append("SELECT B.*");
		sql.append("FROM board AS B");
		sql.append("WHERE B.id = ?", id);

		return MysqlUtil.selectRow(sql, Board.class);
	}

}