package com.icia.pboard.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.icia.pboard.dto.Page;

@Component
public class PagingUtil {
	// map에 startRowNum endRowNum 넣주서 보내기
	// pageno startRowNum endRowNum
	// 1 1 10
	// 2 11 20
	// 3 21 30
	public Map<String, Object> getRowNum(int pageno, int count) {
		int startRowNum = (pageno - 1) * Zboard3Constrant.BOARD_PER_PAGE + 1;
		int endRowNum = startRowNum * Zboard3Constrant.BOARD_PER_PAGE - 1;
		if (endRowNum > count)
			endRowNum = count;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startRowNum", startRowNum);
		map.put("endRowNum", endRowNum);
		return map;
	}

	// 블록당 5개 페이지
	// blockNo prev start end next
	// 0 0 1 5 6
	// 1 5 6 10 11
	// 2 10 11 15 16
	public Page getPage(int pageno, int count) {
		int countOfPage = count / Zboard3Constrant.PAGE_PER_BLOCK + 1;
		if (count % Zboard3Constrant.PAGE_PER_BLOCK == 0)
			countOfPage--;
		if(pageno>countOfPage)
			pageno=countOfPage;
		int blockNo = (pageno - 1) / Zboard3Constrant.PAGE_PER_BLOCK;
		int prev = blockNo * Zboard3Constrant.PAGE_PER_BLOCK;
		int start = prev + 1;
		int end = prev + Zboard3Constrant.PAGE_PER_BLOCK;
		int next = end + 1;
		if (end >= countOfPage) {
			end = countOfPage;
			next = 0;
		}
		return Page.builder().pageno(pageno).prev(prev).start(start).end(end).next(next).build();
	}
}
