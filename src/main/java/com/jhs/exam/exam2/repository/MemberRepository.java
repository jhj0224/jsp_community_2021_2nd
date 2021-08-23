package com.jhs.exam.exam2.repository;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class MemberRepository implements ContainerComponent {
	public void init() {
		
	}
	
	// 로그인아이디로 해당 member가 DB에 찾는 함수
	public Member getMemberByLoginId(String loginId) {
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.loginId = ?", loginId);
		
		return MysqlUtil.selectRow(sql, Member.class);
	}

	// 해당 name과 email을 이용해 해당member를 DB에서 찾아 리턴하는 함수
	public Member getMemberByNameAndEmail(String name, String email) {
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.name = ?", name);
		sql.append("AND M.email = ?", email);
		sql.append("LIMIT 1");

		return MysqlUtil.selectRow(sql, Member.class);
	}

	// 입력받은 변수를 이용 새로운 member를 DB에서 생성
	public int join(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", name = ?", name);
		sql.append(", nickname = ?", nickname);
		sql.append(", cellphoneNo = ?", cellphoneNo);
		sql.append(", email = ?", email);

		int id = MysqlUtil.insert(sql);

		return id;
	}


	// 해당 멤버 DB에서 비밀번호 변경하는 함수
	public void modifyPassword(int id, String loginPw) {
		SecSql sql = new SecSql();
		sql.append("UPDATE `member`");
		sql.append("SET updateDate = NOW()");
		sql.append(", loginPw = ?", loginPw);
		sql.append("WHERE id = ?", id);

		MysqlUtil.update(sql);
		
	}

}
