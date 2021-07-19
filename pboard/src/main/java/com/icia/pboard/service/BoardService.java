package com.icia.pboard.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.icia.pboard.dao.AttachmentDao;
import com.icia.pboard.dao.BoardDao;
import com.icia.pboard.dao.CommentDao;
import com.icia.pboard.dto.BoardDto;
import com.icia.pboard.dto.CommentDto;
import com.icia.pboard.dto.BoardDto.Write;
import com.icia.pboard.dto.Page;
import com.icia.pboard.entity.Attachment;
import com.icia.pboard.entity.Board;
import com.icia.pboard.entity.Comment;
import com.icia.pboard.exception.BoardJob;
import com.icia.pboard.util.PagingUtil;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private ModelMapper modelmapper;
	@Autowired
	private PagingUtil paginUtil;
	@Value("c:/upload/attachment")
	private String attachmentFolder;
	@Value("http://localhost:8081/profile/")
	private String profilePath;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	
	public Page list(Integer pageno) {
		int count = boardDao.count();
		List<Board> boardList = boardDao.findAll(paginUtil.getRowNum(pageno, count));
		Page page = paginUtil.getPage(pageno, count);
		
		List<BoardDto.ListView> list = new ArrayList<BoardDto.ListView>();
		for(Board board:boardList) {
			BoardDto.ListView dto = modelmapper.map(board, BoardDto.ListView.class);
			dto.setWriteTimeString(formatter.format(board.getWriteTime()));
			list.add(dto);
		}
		page.setList(list);
		return page;
	}

	public int write(BoardDto.Write dto, List<MultipartFile> attachments, String username) {
		Board board = Board.builder().title(dto.getTitle()).content(dto.getContent()).writer(username)
				.attachmentCnt(attachments.size()).build();
		
		boardDao.insert(board);
		
		for(MultipartFile file:attachments) {
			String saveFileName = System.currentTimeMillis() +"-" + file.getOriginalFilename();
			File saveFile = new File(attachmentFolder, saveFileName);
			try {
				FileCopyUtils.copy(file.getBytes(), saveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			boolean isImage = file.getContentType().toLowerCase().startsWith("image/");
			Attachment attachment = new Attachment(0, board.getBno(), file.getOriginalFilename(),
					saveFileName, username, file.getSize(), isImage);
			attachmentDao.insert(attachment);
		}
		return board.getBno();
	}
	
	// 글 읽기
	// 1. 글이 없다면 예외 처리
	// 2. 로그인했고 글쓴이가 아닌 경우 조회수 증가 : DB 증가, 읽어왔던 글에서 증가
	// 3. 첨부파일 읽기
	// 4. dto 생성해서 리턴
	public BoardDto.Read easyRead(int bno, String username) {
		Board board = boardDao.findByBno(bno);
		if(board==null) {
			throw new BoardJob.MvcUserNotFoundException();
		}
		
		if(username!=null && board.getWriter().equals(username)==false) {
			boardDao.update(Board.builder().bno(bno).readCnt(1).build());
			board.setReadCnt(board.getReadCnt()+1);
		}
		List<Attachment> attachments = attachmentDao.findAllByBno(bno);
		List<Comment> commentlist = commentDao.findAllByBno(bno);
		List<CommentDto.Read> comments = new ArrayList<CommentDto.Read>();
		
		for(Comment comment:commentlist) {
			CommentDto.Read dto = modelmapper.map(comment, CommentDto.Read.class);
			dto.setIsWriter(dto.getWriter().equals(username));
			dto.setWriteTimeString(formatter.format(comment.getWriteTime()));
			comments.add(dto);
		}
		
		
		BoardDto.Read dto = modelmapper.map(board, BoardDto.Read.class);
		
		dto.setIsWriter(board.getWriter().equals(username));
		dto.setWriteTimeString(formatter.format(board.getWriteTime()));
		dto.setComments(comments);
		dto.setAttachments(attachments);
		return dto;
	}
	
	public BoardDto.Read hardRead(int bno, String username) {
		Map map = boardDao.findById(bno);
		BoardDto.Read dto = modelmapper.map(map, BoardDto.Read.class);
		dto.setIsWriter(map.get("writer").equals(username));
		dto.setWriteTimeString(formatter.format((LocalDateTime)map.get("writeTime")));
		return dto;
	}
}
