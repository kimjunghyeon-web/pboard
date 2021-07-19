package com.icia.pboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.icia.pboard.entity.Comment;

public interface CommentDao {
	@Insert("insert into comments values(comment_seq.nextval, #{bno},#{writer}, #{content}, sysdate, #{profile})")
	public int insert(Comment comment);
	
	@Delete("delete from comments where cno=#{cno} and rownum=1")
	public int delete(int cno);
	
	@Select("select /*+ index_desc(comments comment_pk_cno) */ * from comments where bno=#{bno}")
	public List<Comment> findAllByBno(int bno);
}
