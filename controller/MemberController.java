package com.lec.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lec.domain.Member;
import com.lec.domain.PagingInfo;
import com.lec.domain.SessionMember;
import com.lec.persistence.MemberRepository;
import com.lec.service.MemberService;

@Controller
@SessionAttributes({"pagingInfo"})
public class MemberController {

	@Autowired
	private MemberService memberService;	
	
	public PagingInfo pagingInfo = new PagingInfo();
	
	@GetMapping("getMemberList")
	public String getMemberList(Model model,
			@RequestParam(defaultValue="0") int curPage,
			@RequestParam(defaultValue="10") int rowSizePerPage,
			@RequestParam(defaultValue="name") String searchType,
			@RequestParam(defaultValue="") String searchWord) {   		

		 if (searchType == null) {
		        searchType = "name";  
		    }

		    if (!searchType.equalsIgnoreCase("id") && !searchType.equalsIgnoreCase("name")) {
		        throw new IllegalArgumentException("Invalid searchType: " + searchType);
		    }

		    Pageable pageable = PageRequest.of(curPage, rowSizePerPage, Sort.by(searchType).ascending());
		    Page<Member> pagedResult = memberService.getMemberList(pageable, searchType, searchWord);
	
		int totalRowCount  = pagedResult.getNumberOfElements();
		int totalPageCount = pagedResult.getTotalPages();
		int pageSize       = pagingInfo.getPageSize();
		int startPage      = curPage / pageSize * pageSize + 1;
		int endPage        = startPage + pageSize - 1;
		endPage = endPage > totalPageCount ? (totalPageCount > 0 ? totalPageCount : 1) : endPage;
		
//		if (endPage > totalPageCount) {
//			if(totalPageCount > 0) endPage = totalPageCount; else endPage = 1;
//		} 
	
		pagingInfo.setCurPage(curPage);
		pagingInfo.setTotalRowCount(totalRowCount);
		pagingInfo.setTotalPageCount(totalPageCount);
		pagingInfo.setStartPage(startPage);
		pagingInfo.setEndPage(endPage);
		pagingInfo.setSearchType(searchType);
		pagingInfo.setSearchWord(searchWord);
		pagingInfo.setRowSizePerPage(rowSizePerPage);
		model.addAttribute("pagingInfo", pagingInfo);

		model.addAttribute("pagedResult", pagedResult);
		model.addAttribute("pageable", pageable);
		model.addAttribute("cp", curPage);
		model.addAttribute("sp", startPage);
		model.addAttribute("ep", endPage);
		model.addAttribute("ps", pageSize);
		model.addAttribute("rp", rowSizePerPage);
		model.addAttribute("tp", totalPageCount);
		model.addAttribute("st", searchType);
		model.addAttribute("sw", searchWord);
		
		return "member/getMemberList";
	}
	
	@Autowired
	HttpSession session;
	
	
	
	@GetMapping("/insertMember")
	public String insertMemberForm(Member member) {
		return "member/insertMember";
	}
	
	@PostMapping("/insertMember")
	public String insertMember(Member member) {
		if (member.getId() == null) {
			return "redirect:login";
		}
		member.setRole(member.getRole() != null ? "ADMIN" : "USER");	
		memberService.insertMember(member);
		
		// 사용자 정보 세션에 저장
		SessionMember sessionmember = new SessionMember(member);
		session.setAttribute("member", member);
		
		return "redirect:start";
	}
	
	@GetMapping("deleteMember")
	public String deleteMember(Member member) {
		
		System.out.println("1........" + member.toString());
		
		if (member.getId() == null) {
			return "redirect:login";
		}
		memberService.deleteMember(member);
		return "forward:getMemberList";		
	}

	@GetMapping("updateMember")
	public String updateMember(Model model, HttpSession session) {
	    Member member = (Member) session.getAttribute("member");
	    if (member == null) {
	        return "redirect:login";
	    }
	    model.addAttribute("member", memberService.getMemberById(member.getId()));
	    return "member/updateMember";
	}



	
	@PostMapping("updateMember")
	public String updateMember(Member member) {
		if (member.getId() == null) {
			return "redirect:login";
		}
		member.setRole(member.getRole() != null ? "ADMIN" : "USER");
		memberService.updateMember(member);	
		return "redirect:getMemberList";
		                          
	}
		
	@GetMapping("/logout")
	public String logout() {
		session.removeAttribute("member");
		return "redirect:start";
	}
	
	@GetMapping("/existsById")
	public ResponseEntity<Boolean> existsById(@RequestParam String id){
		boolean exists = memberService.existsById(id);
		return ResponseEntity.ok().body(exists);
	}
	
	@GetMapping("/checkId")
	public ResponseEntity<Boolean> checkId(@RequestParam String id) {
	    boolean exists = memberService.existsById(id);
	    return ResponseEntity.ok().body(exists);
	}
}
