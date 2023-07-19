package com.lec.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ApmtestResult {

	@Id
	private String name;
	
	private int apm;
	private LocalDateTime apmDateTime;
}
