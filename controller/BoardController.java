package com.lec.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.lec.domain.Board;
import com.lec.domain.Member;
import com.lec.domain.PagingInfo;
import com.lec.service.BoardService;

@Controller
@SessionAttributes({"member", "pagingInfo"})
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	Environment environment;
	
	public PagingInfo pagingInfo = new PagingInfo();
	
	@Value("${path.upload}")
	public String uploadFolder;
	
	@ModelAttribute("member")
	public Member setMember() {
		return new Member();
	}

	@RequestMapping("/getBoardList")
	public String getBoardList(Model model,
	        @RequestParam(defaultValue = "0") int curPage,
	        @RequestParam(defaultValue = "10") int rowSizePerPage,
	        @RequestParam(defaultValue = "title") String searchType,
	        @RequestParam(defaultValue = "") String searchWord) {

	    Pageable pageable = PageRequest.of(curPage, rowSizePerPage, Sort.by("seq").descending());
	    Page<Board> pagedResult = boardService.getBoardList(pageable, searchType, searchWord);

	    int totalRowCount = pagedResult.getNumberOfElements();
	    int totalPageCount = pagedResult.getTotalPages();
	    int pageSize = pagingInfo.getPageSize();
	    int startPage = curPage / pageSize * pageSize + 1;
	    int endPage = startPage + pageSize - 1;
	    endPage = endPage > totalPageCount ? (totalPageCount > 0 ? totalPageCount : 1) : endPage;

	    pagingInfo.setCurPage(curPage);
	    pagingInfo.setTotalRowCount(totalRowCount);
	    pagingInfo.setTotalPageCount(totalPageCount);
	    pagingInfo.setStartPage(startPage);
	    pagingInfo.setEndPage(endPage);
	    pagingInfo.setSearchType(searchType);
	    pagingInfo.setSearchWord(searchWord);
	    pagingInfo.setRowSizePerPage(rowSizePerPage);
	    model.addAttribute("pagingInfo", pagingInfo);

	    List<Board> boardList = pagedResult.getContent();
	    for (Board board : boardList) {
	        Board updatedBoard = boardService.getBoard(board);
	        model.addAttribute("board", updatedBoard);
	    }

	    model.addAttribute("pagedResult", pagedResult);
	    model.addAttribute("pageable", pageable);
	    model.addAttribute("cp", curPage);
	    model.addAttribute("sp", startPage);
	    model.addAttribute("ep", endPage);
	    model.addAttribute("ps", pageSize);
	    model.addAttribute("rp", rowSizePerPage);
	    model.addAttribute("tp", totalPageCount);
	    model.addAttribute("st", searchType);
	    model.addAttribute("sw", searchWord);

	    return "board/getBoardList";
	}


	@GetMapping("/insertBoard")
	public String insertBoardView(@ModelAttribute("member") Member member, Model model) {
		if (member.getId() == null) {
			return "redirect:login";
		}
		
		model.addAttribute("board", new Board()); // 빈 Board 객체를 모델에 추가하여 폼에 바인딩
		return "board/insertBoard";
	}

	@PostMapping("/insertBoard")
	public String insertBoard(@ModelAttribute("member") Member member, Board board, @RequestParam("category") String category) throws IOException {
		if (member.getId() == null) {
			return "redirect:login";
		}	
		// 파일업로드
		MultipartFile uploadFile = board.getUploadFile();
		if(!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			uploadFile.transferTo(new File(uploadFolder + fileName));
			board.setFileName(fileName);
		}
		
		board.setCategory(category); // 받은 카테고리 값을 Board 객체에 설정
		
		boardService.insertBoard(board);
		return "redirect:getBoardList";
	}
	@GetMapping("/getBoard")
	public String getBoard(@ModelAttribute("member") Member member, Board board, Model model) {
	    if (member.getId() == null) {
	        return "redirect:login";
	    }

	    boardService.updateReadCount(board);
	    Board updatedBoard = boardService.getBoard(board);
	    model.addAttribute("board", updatedBoard);
	    return "board/getBoard";
	}

	@PostMapping("/updateBoard")
	public String updateBoard(@ModelAttribute("member") Member member, Board board) {
	    if (member.getId() == null) {
	        return "redirect:login";
	    }

	    Board existingBoard = boardService.getBoard(board); // 기존에 저장되어 있는 게시글 가져오기
	    if (!existingBoard.getWriter().equals(member.getId()) && !member.getRole().equals("ADMIN")) {
	        // 게시글 작성자와 현재 로그인한 사용자를 비교하고, 사용자가 관리자가 아닐 경우 에러 페이지로 리다이렉트
	        return "error/errorPage"; 
	    }

	    boardService.updateBoard(board); // 게시글 수정
	    return "forward:getBoardList";
	}



	@GetMapping("/deleteBoard")
	public String deleteBoard(@ModelAttribute("member") Member member, Board board) {
		if (member.getId() == null) {
			return "redirect:login";
		}

		boardService.deleteBoard(board);
		return "forward:getBoardList";
	}
	
	
	@PostMapping("/adminReply")
	public String adminReply(Board board, @RequestParam("reply") String reply) {
	    boardService.adminReply(board, reply); // BoardService를 사용하여 답변 처리
	    return "redirect:/getBoardList";
	}


	
	
	@RequestMapping("/download")
	public void download(HttpServletRequest req, HttpServletResponse res) throws Exception { 	
		req.setCharacterEncoding("utf-8");
		String fileName = req.getParameter("fn");
		
		String fromPath = uploadFolder + fileName;
		String toPath = uploadFolder + fileName;
	
		byte[] b = new byte[4096];
		File f = new File(toPath);
		FileInputStream fis = new FileInputStream(fromPath);
		
		String sMimeType = req.getSession().getServletContext().getMimeType(fromPath); // mimetype = file type : pdf, exe, txt.... 
		if(sMimeType == null) sMimeType = "application/octet-stream";
		
		String sEncoding = new String(fileName.getBytes("utf-8"), "8859_1");
		String sEncoding1 = URLEncoder.encode(fileName, "utf-8");
		
		res.setContentType(sMimeType);
		res.setHeader("Content-Transfer-Encoding", "binary");
		res.setHeader("Content-Disposition", "attachment; filename = " + sEncoding1);
			
		int numRead;
		ServletOutputStream os = res.getOutputStream();
	
		while((numRead=fis.read(b, 0, b.length)) != -1 ) {
			os.write(b, 0, numRead);
		}
		
		os.flush();
		os.close();
		fis.close();
		
		// return "redirect:getBoardList";
	}
	
    public int updateView(Board board) {
        return boardService.updateReadCount(board);
    }    

    
    
}
