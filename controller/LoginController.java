package com.lec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.lec.domain.Member;
import com.lec.service.MemberService;

@Controller
@SessionAttributes("member")
public class LoginController {

	@Autowired
	private MemberService memberService;
			
	@GetMapping("/login")
	public String loginView() {
		return "login/login";
	}
	
	@PostMapping("/login")
	public String login(Member member, Model model) { 
			
		Member findMember = memberService.getMember(member);		
		if (findMember != null && findMember.getPassword().equals(member.getPassword())) {	
			model.addAttribute("member", findMember); 
			return "redirect:getBoardList";
		} else {
			model.addAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "login/login";
		}
	}	

	
	/*
	 * @GetMapping("/logout") public String logout(SessionStatus status) {
	 * status.setComplete(); return "redirect:login"; }
	 */
	
	@PostMapping("/login_xxx")
	public String login(Member member, 
						Model model, 
						@PageableDefault(page=0, size=10, sort="name", direction=Sort.Direction.ASC) Pageable pageable) {
		Member findMember = memberService.getMember(member);
		if (findMember != null && findMember.getPassword().equals(member.getPassword())) {
			model.addAttribute("member", findMember);
			Page<Member> pagedResult = memberService.getMemberList(pageable, "name", "");
			
			int curPage   = pagedResult.getPageable().getPageNumber() + 1;
			int startPage = Math.max(curPage-9, 1);
			int endPage   = Math.min(curPage+10, pagedResult.getTotalPages());
			
			model.addAttribute("pagedResult", pagedResult);
			model.addAttribute("pageable", pageable);
			model.addAttribute("st", "name");
			model.addAttribute("sw", "");
			model.addAttribute("cp", curPage);
			model.addAttribute("sp", startPage);
			model.addAttribute("ep", endPage);
			
			return "member/getMemberList";
		} else {
			return "redirect:login";
		}
	}
	
}
