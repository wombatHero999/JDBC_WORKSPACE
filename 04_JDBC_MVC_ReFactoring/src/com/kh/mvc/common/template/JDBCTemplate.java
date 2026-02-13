package com.kh.mvc.common.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

// JDBC과정 중 자주 등장하는 구문들을 각각의 메서드로 정의한 클래스
// 1. DB와 접속된 Connection을 생성하여 반환하는 메서드
// 2. close 메서드
// 3. commit/rollback 메서드
/*
 * 
 *  Refactoring
 *   - 기존의 코드를 변경하고 코드를 더 이해하기 쉽게 만드는 과정
 *   - 코드의 중복 제거, 공용메서드 및 클래스를 추출, 변수이름을 변경하는 등의 작업을 수행
 *   - 단, 리팩토링의 결과로 인해 잘 작동하던 기능에 문제가 발생하지 않도록 주의해야 한다.
 *  
 *  */
public class JDBCTemplate {
	
	/*
	 * 커넥션 객체의 생성 방식
	 * 1) 어플리케이션에서 DB드라이버를 통해 커넥션을 조회
	 * 2) DB드라이버는 DB와 TCP통신을 수행(3-WAY HandShake)
	 * 3) DB드라이버는 TCP로 커넥션 연결이 완료되면, 아이디와 비번, 기타정보를 DB에 넘긴다
	 * 4) DB는 아이디 비번을 통해 내부인증을 거친 후 내부에 연결된 SESSION을 생성
	 * 5) DB는 커넥션 생성이 완료되었다는 응답 보낸다.
	 * 6) DB드라이버는 커넥션 객체를 생성해서 클라이언트에게 반환한다.
	 *  
	 * 커넥션은 위 작업을 통해 생성 및 종료하기 때문에 처리에 많은 시간이든다.
	 * 따라서, 자주 생성하고 종료하는것은 효율적이지 못하기 때문에, 커넥션은 "미리"여러 개 
	 * 생성해두고 필요할때마다 꺼내쓰는것이 효과적이다. 
	 *  
	 * ConnectionPool(커넥션저장소)
	 *  - 커넥션 객체를 미리 생성하여 보관해두다가, 사용자가 요청할 때마다 커넥션을 전달해주는
	 *    객체. 
	 *  - 대표 커넥션풀 구현 라이브러리 : DBCP, HikariCP, Tomcat DataSource
	 *  */
	public static Connection getConnection() {
		
		Connection conn = null;
		
		// 드라이버정보 로딩용 Properties객체 생성
		Properties prop = new Properties();
		try {
						
			/* 
			 * BasicDataSource
			 *  - javax.sql.DataSource를 구현한 클래스
			 *    DataSource는 db와의 연결 및 커넥션풀 관리, 트랜잭션관리를 위한
			 *    방법들을 정의한 인터페이스
			 *  - 데이터베이스에 연결 및 커넥션 풀 생성 등 커넥션을 관리할 수 있는
			 *  효율적인 메서드를 제공한다.
			 * */
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			BasicDataSource dataSource = new BasicDataSource();
			
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// 생성하고자 하는 커네션에 대한 정보 기술
//			dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//			dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
//			dataSource.setUsername("JDBC");
//			dataSource.setPassword("JDBC");
			dataSource.setDriverClassName(prop.getProperty("driver"));
			dataSource.setUrl(prop.getProperty("url"));
			dataSource.setUsername(prop.getProperty("username"));
			dataSource.setPassword(prop.getProperty("password"));
			dataSource.setInitialSize(10); // 초키 커넥션풀 사이즈(기본값0)
			dataSource.setMaxTotal(50);// 커넥션풀이 가질 수 있는 최대커넥션 수. (기본 8)
			dataSource.setDefaultAutoCommit(false);
			dataSource.setMaxWaitMillis(10000);// 최대 대기 시간설정. 10초가 지나면 에러발생.
			dataSource.setRemoveAbandonedTimeout(300);// 사용하고 있지 않은 커넥션 삭제
			
			conn = dataSource.getConnection();
			
			/*
			 * 	커넥션풀의 장점
 			 *   - 성능향상 : 커넥션풀은 데이터베이스 연결을 미리 생성하고 풀에 유지
 			 *   함으로써 어플리케이션의 성능을 향상시킬수 있다.(생성시의 비용문제해결)
 			 *   - 메모리 누수 방지 : 사용하고 닫아두 않는 커넥션들을 일정시간이 
 			 *   지났을때 자동으로 삭제처리해주는 알고리즘이 장착되어 있다.
 			 *   - 확장성 : 커넥션풀에서 만들어놓은 다양한 옵션을 통해 원하는 성능의
 			 *   커넥션풀을 생성할 수 있다.
 			 *   
 			 *   커넥션풀의 단점
 			 *   - 코드가 많아진다
 			 *   - 메모리사용량이 증가한다.
 			 *   - 초기 생성비용이 크다.
			 *  */
//			conn = DriverManager
//				.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
//						"JDBC","JDBC");
//			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	// 전달받은 JDBC용 객체를 반납시켜주는 메서드
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 트랜잭션처리 메서드
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed())
				conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed())
				conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}



