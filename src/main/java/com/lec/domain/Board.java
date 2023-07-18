package com.lec.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EntityListeners(BoardListeners.class)
@Getter
@Setter
@ToString
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	private String title;

	@Column(updatable = false)
	private String writer;

	private String content;

	@Column(insertable = false, updatable = false, columnDefinition = "date default now()")
	private Date createDate;

	@Column(insertable = false, updatable = false, columnDefinition = "bigint default 0")
	private Long cnt;

	private String fileName;

	// 파일 이름 대신 파일 유무를 Y/N 으로 표시
	@ColumnDefault("'N'")
	private String fileExists;

	@Transient
	private MultipartFile uploadFile;

	// 카테고리
	private String category;

	// @Transient
	// @Column(columnDefinition = "integer default 0", nullable = true)
	private Long board_ref;

	// @Transient
	// @Column(columnDefinition = "integer default 0", nullable = true)
	private Long board_lev;

	// @Transient
	// @Column(columnDefinition = "integer default 0", nullable = true)
	private Long board_seq;

	// 게시판 관리자 답변
	private String adminReply;

	// 답변 상태 추가
	private String replyStatus;

}
