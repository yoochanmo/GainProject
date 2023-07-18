package com.lec.controller;

import com.lec.domain.ReactionTestResult;
import com.lec.service.ReactionTestResultService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ReactionTestResultController {

	private final ReactionTestResultService reactionTestResultService;

	public ReactionTestResultController(ReactionTestResultService reactionTestResultService) {
		this.reactionTestResultService = reactionTestResultService;
	}

	@PostMapping("/result")
	public ReactionTestResult saveResult(@RequestBody ReactionTestResult result) {
		return reactionTestResultService.saveResult(result);
	}

	@GetMapping("/start")
	public ModelAndView start() {
		return new ModelAndView("start");
	}

	@GetMapping("/testPage")
	public ModelAndView getTestPage() {
		return new ModelAndView("testPage");
	}
	
	/*
	 * @GetMapping("/apmTest") public ModelAndView apmPage() { return new
	 * ModelAndView("apmTest"); }
	 */


	@GetMapping("/rank")
	public List<ReactionTestResult> getRanking(@RequestParam int n) {
		return reactionTestResultService.getRanking(n);
	}

}
