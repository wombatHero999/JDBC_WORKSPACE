package com.kh.mvc.controller;

import java.util.List;

import com.kh.mvc.model.service.MemberService;
import com.kh.mvc.model.vo.Member;
import com.kh.mvc.view.MemberView;

/* 
 * Controller
 *  - View를 통해서 요청받은 기능 처리를 담당
 *  - View로부터 전달받은 데이터들을 가공처리를 거친 후, Service메서드
 *    에게 전달한다.
 *  - Service로부터 반환받은 결과에 따라 사용자가 보게될 응답화면을 지정.
 * */
public class MemberController {

	private MemberService mService = new MemberService();
	//private MemberView view = new MemberView();
	
	public void insertMember(String userId, String userPwd, String userName, String gender, int age, String email,
			String phone, String address) {
		// 전달받은 데이터 가공처리
		Member m =
				new Member(userId, userPwd, userName, gender, email, phone, address, age, null);
		
		// service의 insertMember를 처리할 메서드 호출
		int result = mService.insertMember(m);
		
		// 응답결과에 따라 사용자가 보게될 화면(View)지정
		if(result > 0) {
			new MemberView().displaySuccess("회원 추가 성공");
		}else {
			new MemberView().displayFail("회원 추가 실패");
		}		
	}

	public void selectAll() {
		// SELECT -> ResultSet -> List<Mebmer>
		List<Member> list = mService.selectAll();
		
		if(list.isEmpty()) {
			new MemberView().displayNodata("전체 조회 결과가 없습니다..");
		}else {
			new MemberView().displayList(list);
		}
		
	}

}







