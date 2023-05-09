package com.KoreaIT.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.JAM.Member;
import com.KoreaIT.JAM.service.MemberService;
import com.KoreaIT.JAM.session.Session;

public class MemberController {

	private Scanner sc;
	private MemberService memberService;
	
	public MemberController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.memberService = new MemberService(conn);
	}

	public void doJoin() {
		System.out.println("== 회원 가입 ==");

		String loginId = null;
		String loginPw = null;
		String loginPwChk = null;
		String name = null;
		
		while(true) {
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine().trim();
			
			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}
			
			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);
			
			if (isLoginIdDup) {
				System.out.printf("%s(은)는 이미 사용중인 아이디입니다\n", loginId);
				continue;
			}
			
			System.out.printf("%s(은)는 사용가능한 아이디입니다\n", loginId);
			break;
		}
		
		while(true) {
			System.out.printf("로그인 비밀번호 : ");
			loginPw = sc.nextLine().trim();
			
			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요");
				continue;
			}
			
			boolean loginPwCheck = true;
			
			while(true) {
				System.out.printf("로그인 비밀번호 확인 : ");
				loginPwChk = sc.nextLine().trim();
				
				if (loginPwChk.length() == 0) {
					System.out.println("비밀번호 확인을 입력해주세요");
					continue;
				}
				
				if (loginPw.equals(loginPwChk) == false) {
					System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요");
					loginPwCheck = false;
				}
				break;
			}
			if (loginPwCheck) {
				break;
			}
		}
		
		while(true) {
			System.out.printf("이름 : ");
			name = sc.nextLine().trim();
			
			if (name.length() == 0) {
				System.out.println("이름을 입력해주세요");
				continue;
			}
			break;
		}
		
		memberService.doJoin(loginId, loginPw, name);
		
		System.out.printf("%s님 환영합니다~\n", name);

	}

	public void doLogin() {
		System.out.println("== 로그인 ==");
		
		while(true) {
			System.out.printf("로그인 아이디 : ");
			String loginId = sc.nextLine().trim();
			
			if(loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}
			
			System.out.printf("로그인 비밀번호 : ");
			String loginPw = sc.nextLine();
			
			if(loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요");
				continue;
			}
			
			Member member = memberService.getMember(loginId);
			
			if (member == null) {
				System.out.printf("%s은(는) 존재하지 않는 아이디입니다\n", loginId);
				return;
			}
			
			if (member.loginPw.equals(loginPw) == false) {
				System.out.println("비밀번호가 일치하지 않습니다");
				return;
			}
			
			System.out.printf("%s님 환영합니다\n", member.name);
			
			Session.loginedMemeberId = member.id;
			Session.loginedMember = member;
			
			break;
		}
		
	}
	
}
