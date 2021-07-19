package com.icia.pboard.dao;

import java.util.List;
import java.util.Map;

import com.icia.pboard.entity.Board;

public interface BoardDao {
	public int count();
	
	public List<Board> findAll(Map<String, Object> map);
	
	public int insert(Board board);

	public Board findByBno(int bno);

	public Map findById(int bno);
	
	public int update(Board board);
}
