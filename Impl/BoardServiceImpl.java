package com.lec.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.domain.Board;
import com.lec.domain.Member;
import com.lec.persistence.BoardRepository;
import com.lec.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardRepository boardRepo;

	public Board getBoard(Board board) {
	    Optional<Board> findBoard = boardRepo.findBySeq(board.getSeq());
	    if (findBoard.isPresent())
	        return findBoard.get();
	    else
	        return null;
	}


	public Page<Board> getBoardList(Pageable pageable, String searchType, String searchWord) {		
		if(searchType.equalsIgnoreCase("title")) {
			return boardRepo.findByTitleContaining(searchWord, pageable);
		} else if(searchType.equalsIgnoreCase("writer")) {
			return boardRepo.findByWriterContaining(searchWord, pageable);
		} else {
			return boardRepo.findByContentContaining(searchWord, pageable);
		}
	}
    

	public void insertBoard(Board board) {
		boardRepo.save(board);
		boardRepo.updateLastSeq(0L, 0L, board.getSeq());
	}


	public void updateBoard(Board board) {
		Board findBoard = boardRepo.findById(board.getSeq()).get();

		findBoard.setTitle(board.getTitle());
		findBoard.setContent(board.getContent());
		findBoard.setAdminReply(board.getAdminReply());
		boardRepo.save(findBoard);
	}

	public void deleteBoard(Board board) {
		boardRepo.deleteById(board.getSeq());
	}

	@Override
	public long getTotalRowCount(Board board) {
		return boardRepo.count();
	}

	@Override
	public int updateReadCount(Board board) {
		return boardRepo.updateReadCount(board.getSeq());
	}

	@Override
	public void adminReply(Board board, String reply) {
		board.setAdminReply(reply);
		boardRepo.updateReplyStatus(board.getSeq(), board.getReplyStatus());
		updateBoard(board);
	}





	
}
