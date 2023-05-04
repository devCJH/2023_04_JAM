package com.KoreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.KoreaIT.JAM.controller.ArticleController;
import com.KoreaIT.JAM.controller.MemberController;
import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class App {
	public void run() {
		System.out.println("== 프로그램 시작 ==");

		Scanner sc = new Scanner(System.in);

		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			MemberController memberController = new MemberController(conn, sc);
			ArticleController articleController = new ArticleController(conn, sc);
			
			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 끝 ==");
					break;
				}

				if (cmd.equals("member join")) {
					memberController.doJoin();
				} else if (cmd.equals("article write")) {
					articleController.doWrite();
				} else if (cmd.equals("article list")) {
					articleController.showList();
				} else if (cmd.startsWith("article detail ")) {
					articleController.showDetail(cmd);
				} else if (cmd.startsWith("article modify ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = SecSql.from("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					int articleCount = DBUtil.selectRowIntValue(conn, sql);
					
					if (articleCount == 0) {
						System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
						continue;
					}
					
					System.out.printf("== %d번 게시글 수정 ==\n", id);
					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine();

					sql = SecSql.from("UPDATE article");
					sql.append("SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append("WHERE id = ?", id);
					
					DBUtil.update(conn, sql);
					
					System.out.printf("%d번 게시글이 수정되었습니다\n", id);
				} else if (cmd.startsWith("article delete ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();
					sql.append("SELECT COUNT(*) > 0");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					boolean isHaveArticle = DBUtil.selectRowBooleanValue(conn, sql);
					
					if (!isHaveArticle) {
						System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
						continue;
					}
					sql = new SecSql();
					sql.append("DELETE FROM article");
					sql.append("WHERE id = ?", id);
					
					DBUtil.delete(conn, sql);
					
					System.out.printf("== %d번 게시글 삭제 ==\n", id);
					System.out.printf("%d번 게시글이 삭제되었습니다\n", id);
				} else {
					System.out.println("존재하지 않는 명령어 입니다");
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.close();
	}
}
