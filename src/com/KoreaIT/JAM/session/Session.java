package com.KoreaIT.JAM.session;

import com.KoreaIT.JAM.dto.Member;

public class Session {
	public static int loginedMemeberId;
	public static Member loginedMember;

	static {
		loginedMemeberId = -1;
		loginedMember = null;
	}

	public static void login(Member member) {
		loginedMemeberId = member.id;
		loginedMember = member;
	}

	public static void logout() {
		loginedMemeberId = -1;
		loginedMember = null;
	}

	public static boolean isLogined() {
		return loginedMemeberId != -1;
	}
}