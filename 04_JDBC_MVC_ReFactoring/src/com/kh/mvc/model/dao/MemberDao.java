package com.kh.mvc.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.kh.mvc.common.template.JDBCTemplate;
import com.kh.mvc.model.vo.Member;

/*
 * 	DAO(Data Access Object)
 *   - Service에 의해 호출되며, 맡은 기능을 수행하기 위해 db에
 *   직접 접근하여 sql문을 호출한 후 처리 결과값을 반환시켜주는 객체.
 *  */
public class MemberDao {
	
	/** 
	 * 사용자가 view에서 입력한 값을 DB에 Insert하는 메서드
	 * @param conn : service에서 생성하여 전달
	 * @param m : controller에서 래핑처리하여 전달
	 * @return 처리된 행의 갯수(int) 
	 * */
	
	private Properties prop = new Properties();
	
	{
		try {
			prop.loadFromXML(new FileInputStream("resources/member_mapper.xml"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int insertMember(Connection conn, Member m) {
		int result = 0;
		PreparedStatement pstmt = null;
//		String sql = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, ?,?,?,?, SYSDATE )";
		String sql = prop.getProperty("insertMember");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getMemberId());
			pstmt.setString(2, m.getMemberPwd());
			pstmt.setString(3, m.getMemberName());
			pstmt.setString(4, m.getGender());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getPhone());
			pstmt.setString(7, m.getAddress());
			pstmt.setInt(8, m.getAge());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	
	public List<Member> selectAll(Connection conn){
		// 변수 셋팅
		List<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
//		String sql = "SELECT * FROM MEMBER";
		String sql = prop.getProperty("selectAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member m = new Member();
				m.setMemberId(rset.getString("MEMBER_ID"));
				m.setMemberName(rset.getString("MEMBER_NAME"));
				m.setPhone(rset.getString("PHONE"));
				m.setAge(rset.getInt("AGE"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				
				list.add(m);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}


	public Member selectByUserId(Connection conn, String userId) {
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
//		String sql = "SELECT * FROM MEMBER "
//				+ "WHERE MEMBER_ID = ?";
		String sql = prop.getProperty("selectByUserId");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				m = new Member();
				m.setMemberId(rset.getString("MEMBER_ID"));
				m.setMemberName(rset.getString("MEMBER_NAME"));
				m.setPhone(rset.getString("PHONE"));
				m.setAge(rset.getInt("AGE"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return m;	
	}


	public List<Member> selectByUserName(Connection conn, String keyword) {
		List<Member> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		//String sql = "SELECT * FROM MEMBER WHERE MEMBER_NAME LIKE ?";
		String sql = prop.getProperty("selectByUserName");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				Member m = new Member();
				m.setMemberId(rset.getString("MEMBER_ID"));
				m.setMemberName(rset.getString("MEMBER_NAME"));
				m.setPhone(rset.getString("PHONE"));
				m.setAge(rset.getInt("AGE"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}


	public int updateMember(Connection conn, Member m) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "UPDATE MEMBER SET MEMBER_PWD = ? , "
				+ "EMAIL = ? , PHONE =? , ADDRESS = ? "
				+ "WHERE MEMBER_ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getMemberPwd() );
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getMemberId());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}


	public int deleteMember(Connection conn, String userId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM MEMBER WHERE MEMBER_ID = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	
}








