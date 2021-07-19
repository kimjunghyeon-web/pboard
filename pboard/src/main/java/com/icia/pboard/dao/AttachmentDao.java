package com.icia.pboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.icia.pboard.entity.Attachment;

public interface AttachmentDao {
	@Insert("insert into attachment values(attachment_seq.nextval, #{bno}, #{writer}, #{originalFileName}, #{saveFileName}, #{length}, #{isImage})")
	public int insert(Attachment attachment);
	
	@Select("select * from attachment where bno = #{bno}")
	public List<Attachment> findAllByBno(int bno);

	@Select("select * from attachment where ano = #{ano} and rownum=1")
	public Attachment findById(Integer ano);

	@Delete("delete from attachment where ano=#{ano} and rownum=1")
	public void delete(Integer ano);
}
