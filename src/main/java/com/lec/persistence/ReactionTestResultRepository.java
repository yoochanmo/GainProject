package com.lec.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.domain.ReactionTestResult;

public interface ReactionTestResultRepository extends JpaRepository<ReactionTestResult, Long> {
	List<ReactionTestResult> findTop10ByOrderByAverageReactionTimeAsc();

}
