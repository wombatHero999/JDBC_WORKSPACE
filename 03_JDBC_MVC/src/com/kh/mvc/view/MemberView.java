package com.kh.mvc.view;

import java.util.List;
import java.util.Scanner;

import com.kh.mvc.controller.MemberController;
import com.kh.mvc.model.vo.Member;

/* 
 * View : 사용자가 보게될 시각적인 요소를 담당.
 * */
public class MemberView {

	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();
	
	/**
	 * 사용자가 보게될 메인화면
	 */
	public void mainMenu() {
		
		while(true) {
			System.out.println("***** 회원 프로그램 *****");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디로 검색");
			System.out.println("4. 회원 이름 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴 기능");
			System.out.println("9. 프로그램 종료");
			System.out.println("----------------------------------------------");
			System.out.print("이용할 메뉴 선택 : ");
			int menu = Integer.parseInt(sc.nextLine());
			
			switch(menu){
			case 1: insertMember(); break;
			case 2: selectAll(); break;
			case 3: selectByUserId(); break;
			case 4: selectByUserName(); break;
			case 5: updateMember(); break;
			case 6: deleteMember(); break;
			case 9: System.out.println("프로그램을 종료합니다.."); return;
			default : System.out.println("잘못된 메뉴를 선택했습니다. 다시입력해주세요.");
			}
		}
	}
	
	/* 
	 * 회원 추가용 View
	 * 추가하고자 하는 회원의 정보를 입력받아, Controller에 회원추가요청을
	 * 보낸다.
	 * */
	private void insertMember() {
		System.out.println("----- 회원추가 -----");
		
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String userPwd = sc.nextLine();
		
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		
		System.out.print("성별(M/F) : ");
		String gender = sc.nextLine().toUpperCase();
		
		System.out.print("나이 : ");
		int age = sc.nextInt();
		sc.nextLine();
		
		System.out.print("이메일 : ");
		String email = sc.nextLine();

		System.out.print("핸드폰 : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		mc.insertMember(userId,userPwd,userName,gender,age,email,phone,address);
	}

	/**
	 * 회원 전체 조회를 할 수 있는 화면
	 */
	public void selectAll() {
		
		System.out.println("----- 회원 전체 조회 -----");
		
		// 회원 전체 조회 요청
		mc.selectAll();
	}
	
	/**
	 * 사용자에게 검색할 회원의 아이디를 입력받은 후 조회 요청하는 메소드
	 */
	public void selectByUserId() {
		
		System.out.println("----- 회원 아이디로 검색 -----");
		
		System.out.print("검색할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 입력한 아이디를 회원 아이디 검색 요청시 같이 넘김
		mc.selectByUserId(userId);
		
	}
	
	public void selectByUserName() {
		
		System.out.println("----- 회원 이름 키워드 검색 -----");
		
		System.out.print("회원 이름 키워드 입력 : ");
		String keyword = sc.nextLine();
		
		mc.selectByUserName(keyword);
	}
	
	/**
	 * 사용자에게 변경할 회원의 아이디, 변경할 정보들 (비번, 이메일, 휴대폰번호, 주소)을
	 * 입력받은 후 변경 요청하는 메소드
	 */
	public void updateMember() {
		
		System.out.println("----- 회원정보 변경 -----");
		
		// 변경할 회원의 아이디
		System.out.print("변경할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 변경할 정보들
		System.out.print("변경할 비밀번호 : ");
		String newPwd = sc.nextLine();
		
		System.out.print("변경할 이메일 : ");
		String newEmail = sc.nextLine();
		
		System.out.print("변경할 휴대폰번호 (숫자만)  : ");
		String newPhone = sc.nextLine();
		
		System.out.print("변경할 주소 : ");
		String newAddress = sc.nextLine();
		
		// 회원 정보 수정 요청
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress);
	}
	
	/**
	 * 사용자에게 탈퇴할 회원의 아이디 를 입력받은 후 삭제 요청하는 메소드
	 */
	private void deleteMember() {
		
		System.out.println("----- 회원 탈퇴 -----");
		
		System.out.print("탈퇴할 회원 ID : ");
		String userId = sc.nextLine();
		
		mc.deleteMember(userId);
	}
	
	// -- 서비스 요청 처리후 사용자가 보게될 응답View
	public void displaySuccess(String string) {
		System.out.println(string);
	}

	public void displayFail(String string) {
		System.out.println(string);
	}

	/**
	 * 조회 서비스 요청 시 조회결과가 없을 때 보게 될 화면
	 * @param message : 사용자에게 보여질 메세지
	 */
	public void displayNodata(String message) {
		System.out.println(message);
	}
	
	/**
	 * 조회 서비스 요청 시 여러 행이 조회된 결과를 받아서 보게 될 화면
	 * @param list : 여러 행이 조회된 결과
	 */
	public void displayList(List<Member> list) {
		System.out.println("\n조회된 데이터는 " + list.size() + "건 입니다.\n");
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	public void displayOne(Member m) {
		System.out.println("\n조회된 데이터는 다음과 같습니다.");
		System.out.println(m);
	}
}







