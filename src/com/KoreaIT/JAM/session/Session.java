package com.KoreaIT.JAM.session;

import com.KoreaIT.JAM.Member;

public class Session {
	public static int loginedMemeberId;
	public static Member loginedMember;
	
	static {
		loginedMemeberId = -1;
		loginedMember = null;
	}
}