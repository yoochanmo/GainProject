package com.lec.persistence;

import java.util.Optional;

import javax.persistence.PrePersist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.lec.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

	//클래스, 메서드 모두에 @Transactional 어노테이션을 붙이면 메서드 레벨의 @Transactional 선언이 우선 적용
	// @Transactional이 붙은 메서드는 메서드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소 
	// @Modifying 어노테이션은 @Query 어노테이션에서 작성된 조회를 제외한 데이터의 변경이 있는
	// 삽입(Insert), 수정(Update), 삭제(Delete) 쿼리 사용시 필요한 어노테이션이다.
	@Modifying
    @Transactional
    @Query("update Board b set b.cnt = b.cnt + 1 where b.seq = :seq")
    int updateReadCount(@Param("seq")Long seq);
	
	@Modifying
	@Transactional
	@Query("update Board b set b.board_ref = b.seq, b.board_lev=:lev, b.board_seq=:_seq where b.seq = :seq")
	void updateLastSeq(@Param("lev")Long i, @Param("_seq")Long j, @Param("seq")Long seq);

	
	@Modifying
	@Transactional
	@Query("update Board b set b.replyStatus = :replyStatus where b.seq = :seq")
	void updateReplyStatus(@Param("seq") Long seq, @Param("replyStatus") String replyStatus);


	
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    Page<Board> findByWriterContaining(String writer, Pageable pageable);
    Page<Board> findByContentContaining(String content, Pageable pageable);
    
    Optional<Board> findBySeq(Long seq);



}
