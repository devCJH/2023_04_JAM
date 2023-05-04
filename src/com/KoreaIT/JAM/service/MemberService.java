package com.KoreaIT.JAM.service;

import java.sql.Connection;

import com.KoreaIT.JAM.dao.MemberDao;

public class MemberService {

	MemberDao memberDao;
	
	public MemberService(Connection conn) {
		this.memberDao = new MemberDao(conn);
	}

	public boolean isLoginIdDup(String loginId) {
		return memberDao.isLoginIdDup(loginId);
	}

	public void doJoin(String loginId, String loginPw, String name) {
		memberDao.doJoin(loginId, loginPw, name);
	}

}
