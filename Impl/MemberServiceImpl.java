package com.lec.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.domain.Board;
import com.lec.domain.Member;
import com.lec.persistence.BoardRepository;
import com.lec.persistence.MemberRepository;
import com.lec.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberRepository memberRepo;	
	
	public Member getMember(Member member) {
		Optional<Member> findMember = memberRepo.findById(member.getId());
		if(findMember.isPresent())
			return findMember.get();
		else return null;
	}
	
	@Override
	public long getTotalRowCount(Member member) {
		return memberRepo.count();
	}

	@Override // https://howtodoinjava.com/spring-boot2/pagination-sorting-example/
	public Page<Member> getMemberList(Pageable pageable, String searchType, String searchWord) {		
		if(searchType.equalsIgnoreCase("id")) {
			return memberRepo.findByIdContaining(searchWord, pageable);
		} else {
			return memberRepo.findByNameContaining(searchWord, pageable);
		}
	}

	@Override
	public void insertMember(Member member) {
		memberRepo.save(member);
	}
	
	@Override
	public void deleteMember(Member member) {
		memberRepo.deleteById(member.getId());
	}

	@Override
	public void updateMember(Member member) {
		memberRepo.save(member);		
	}

	@Override
	public Member getMemberById(String memberId) {
		Optional<Member> findMember = memberRepo.findById(memberId);
		return findMember.orElse(null);
	}



	@Override
	public boolean existsById(String id) {
	    return memberRepo.existsById(id);
	}


}
