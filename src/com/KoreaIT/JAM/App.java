package com.KoreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 끝 ==");
					break;
				}

				if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine();
					System.out.printf("내용 : ");
					String body = sc.nextLine();

					SecSql sql = new SecSql();
					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					
					int id = DBUtil.insert(conn, sql);
					
					System.out.printf("%d번 게시글이 생성되었습니다\n", id);

				} else if (cmd.equals("article list")) {
					List<Article> articles = new ArrayList<>();
					
					SecSql sql = new SecSql();
					
					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("ORDER BY id DESC");
					
					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
					
					for(Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}

					if (articles.size() == 0) {
						System.out.println("존재하는 게시물이 없습니다");
						continue;
					}
					System.out.println("== 게시물 리스트 ==");
					System.out.println("번호	|	제목	|	날짜");

					for (Article article : articles) {
						System.out.printf("%d	|	%s	|	%s\n", article.id, article.title, article.regDate);
					}
				} else if (cmd.startsWith("article modify ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);
					System.out.printf("== %d번 게시글 수정 ==\n", id);

					SecSql sql = SecSql.from("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					int articleCount = DBUtil.selectRowIntValue(conn, sql);
					
					if (articleCount == 0) {
						System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
						continue;
					}
					
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
