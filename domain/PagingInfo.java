package com.lec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingInfo {
	
	//입력받는 데이터
	private int curPage = 1;           // 현재 페이지 번호
	private int rowSizePerPage = 50;   // 한 페이지당 레코드 수      기본10
	private int pageSize = 10;         // 페이지 리스트에서 보여줄 페이지 갯수  이거는 보통 10 or 5 안 변함 
	private int totalRowCount;         // 총 레코드 건수
	private int firstRow ;             // 시작 레크드 번호   
	private int lastRow;               // 마지막 레크드 번호 
	private int totalPageCount;        // 총 페이지 건수
	private int startPage; 	           // 페이지 리스트에서 시작  페이지 번호 
	private int endPage;               // 페이지 리스트에서 마지막 페이지 번호 
	private String searchWord;
	private String searchType;
	
	//page계산
	public void pageSetting() {

		totalPageCount=(totalRowCount-1)/rowSizePerPage+1;  
		firstRow=(curPage-1)*rowSizePerPage;  
		lastRow=firstRow + rowSizePerPage;      
		if(lastRow>totalRowCount) lastRow=totalRowCount;		
		startPage=(curPage-1)/pageSize*pageSize+1;
		endPage=startPage+pageSize-1;
		if(endPage>totalPageCount) endPage=totalPageCount;			
	}
}