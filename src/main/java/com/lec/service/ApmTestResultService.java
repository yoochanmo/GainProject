package com.lec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lec.domain.ApmtestResult;
import com.lec.persistence.ApmTestResultRepository;

@Service
public class ApmTestResultService {
	private final ApmTestResultRepository apmTestResultRepository;
	
	@Autowired
	public ApmTestResultService(ApmTestResultRepository apmTestResultRepository) {
		this.apmTestResultRepository = apmTestResultRepository;
	}
	
	public ApmtestResult saveApmtestResult(ApmtestResult apmtestResult) {
		return apmTestResultRepository.save(apmtestResult);
	}
	
	public List<ApmtestResult> getApmtestResultsOrderByApmDesc(){
		return apmTestResultRepository.findAllByOrderByApmDesc();
	}
}
