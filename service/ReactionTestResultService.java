package com.lec.service;

import com.lec.domain.ReactionTestResult;
import com.lec.persistence.ReactionTestResultRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReactionTestResultService {
    private static final Logger logger = LoggerFactory.getLogger(ReactionTestResultService.class);
	private ReactionTestResultRepository reactionTestResultRepository;

    
    public ReactionTestResultService(ReactionTestResultRepository reactionTestResultRepository) {
        this.reactionTestResultRepository = reactionTestResultRepository;
    }
    
    public ReactionTestResult saveResult(ReactionTestResult result) {
        result.setTestDateTime(LocalDateTime.now()); // 현재 시간 설정
        return reactionTestResultRepository.save(result);
    }

    public List<ReactionTestResult> getRanking(int n) {
        return reactionTestResultRepository.findTop10ByOrderByAverageReactionTimeAsc();
    }




}
