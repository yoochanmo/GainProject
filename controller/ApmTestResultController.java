package com.lec.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lec.domain.ApmtestResult;
import com.lec.service.ApmTestResultService;

@RestController
public class ApmTestResultController {
	private final ApmTestResultService apmTestResultService;

	@Autowired
	public ApmTestResultController(ApmTestResultService apmTestResultService) {
		this.apmTestResultService = apmTestResultService;
	}

	/*
	 * @PostMapping public ApmtestResult saveApmTestResult(@RequestBody
	 * ApmtestResult apmtestResult) {
	 * apmtestResult.setApmDateTime(LocalDateTime.now()); // 현재 시간을 apmDateTime 필드에
	 * return apmTestResultService.saveApmtestResult(apmtestResult); }
	 */

	@PostMapping("/apmTest")
	public ApmtestResult submitApmTestResult(@RequestBody ApmtestResult apmtestResult) {
		apmtestResult.setApmDateTime(LocalDateTime.now()); // 현재 시간을 apmDateTime 필드에 설정
		return apmTestResultService.saveApmtestResult(apmtestResult);
	}

	@GetMapping("/apmTest/rankings")
	public List<ApmtestResult> getApmtestResultsOrderedByApm() {
		return apmTestResultService.getApmtestResultsOrderByApmDesc();
	}

	@GetMapping("/apmTest")
	public ModelAndView apmTestPage() {
	    ModelAndView modelAndView = new ModelAndView("apmTest");
	    List<ApmtestResult> gameResults = apmTestResultService.getApmtestResultsOrderByApmDesc();
	    modelAndView.addObject("gameResults", gameResults);
	    return modelAndView;
	}

}
