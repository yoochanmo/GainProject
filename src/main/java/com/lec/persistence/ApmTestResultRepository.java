package com.lec.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec.domain.ApmtestResult;

@Repository
public interface ApmTestResultRepository extends JpaRepository<ApmtestResult, Long> {
	 List<ApmtestResult> findAllByOrderByApmDesc();
}
