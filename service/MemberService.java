package com.lec.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.domain.Board;
import com.lec.domain.Member;

public interface MemberService {
	
	long getTotalRowCount(Member member);
	Member getMember(Member member);
	Member getMemberById(String memberId);
	Page<Member> getMemberList(Pageable pageable, String searchType, String searchWord);
	void insertMember(Member member);
	void updateMember(Member member);
	void deleteMember(Member member);
	boolean existsById(String id);
	
	
	
}