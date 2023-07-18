package com.lec.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ReactionTestResult {

	@Id
	private String name;

    
    private double averageReactionTime;
    private LocalDateTime testDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageReactionTime() {
        return averageReactionTime;
    }

    public void setAverageReactionTime(double averageReactionTime) {
        this.averageReactionTime = averageReactionTime;
    }

    public LocalDateTime getTestDateTime() {
        return testDateTime;
    }

    public void setTestDateTime(LocalDateTime testDateTime) {
        this.testDateTime = testDateTime;
    }
}
