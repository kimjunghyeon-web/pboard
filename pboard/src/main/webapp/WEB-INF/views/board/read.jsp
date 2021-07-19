<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 읽기</title>
<link rel="stylesheet" href="/pboard/css/read.css">
<script src="/pboard/ckeditor/ckeditor.js"></script>
<style>
	#content {
		min-height: 400px;
	}
</style>
<sec:authorize access="isAuthenticated()">
	<script>
		var isLogin = true;
		var isWriter = ${board.isWriter};
	</script>
</sec:authorize>

<sec:authorize access="isAnonymous()">
	<script>
		var isLogin = false;
		var isWriter = false;
	</script>
</sec:authorize>
<script>
function printAttachments(attachments) {
	var $attachment = $("#attachment");
	$attachment.empty();
	$.each(attachments, function(idx, attachment) {
		var $li = $("<li>").css("overflow", "hidden").css("width", "300px").appendTo($attachment);
		var $div1 = $("<div>").css("float", "left").appendTo($li);
		if(attachment.isImage==true)
			$("<i class='fa fa-file-image-o'></i>").appendTo($div1);
		else
			$("<i class='fa fa-paperclip'></i>").appendTo($div1);
		$("<span>").text(" ").appendTo($div1);
		$("<a>").attr("href", "/pboard/attachments/" + attachment.ano).text(attachment.originalFileName)
			.appendTo($div1);
		var $div2 = $("<div>").css("float", "right").appendTo($li);
		if(isWriter==true)
			$("<span>").attr("class","delete_attachment").data("ano", attachment.ano).data("bno", attachment.bno)
				.attr("title", attachment.originalFileName +" 삭제").text("X").css("cursor", "pointer").appendTo($div2);
		});
	
	/*
	<ul id="attachment">
					<c:forEach items="${board.attachments}" var="attachment">
						<li style="overflow:hidden; width: 300px;">
							<div style="float:left">
								<c:if test="${attachment.isImage==true }">
									<i class="fa fa-file-image-o"></i>
								</c:if>
								<c:if test="${attachment.isImage==false }">
									<i class="fa fa-paperclip"></i>
								</c:if>
								 <a href='/pboard/attachments/${attachment.ano }'>${attachment.originalFileName}</a>
							</div>
							<div style="float:right;">										
								<c:if test="${board.isWriter==true}">
									<span class='delete_attachment' data-ano='${attachment.ano}' data-bno='${attachment.bno}' 
										title='${attachment.originalFileName} 삭제' style='cursor:pointer;'>X</span>
								</c:if>
							</div>
						</li>
					</c:forEach>	
				</ul>
	*/
}

function printComments(comments) {
	var $comments = $("#comments");
	$comments.empty();
	$.each(comments, function(idx, comment) {
		var $outer_div = $("<div>").appendTo($comments);
		var $upper_div = $("<div>").appendTo($outer_div);
		$("<span>").css("color", "blue").text(comment.writer).appendTo($upper_div);
		$("<span>").text(" [ " + comment.writeTime + " ] ").appendTo($upper_div);
		var $lower_div = $("<div>").css("overflow", "hidden").appendTo($outer_div);
		$("<img>").attr("src", comment.profile).css("width", "60px").appendTo($lower_div);	
		$("<span>").text(comment.content).appendTo($lower_div);

		if(isWriter==true)
			$("<button>").attr("class", "delete_comment").data("cno", comment.cno).data("bno", comment.bno)
			.css("float","right").text("삭제").appendTo($lower_div);
	});

	/*
	<div id="comments">
	<c:forEach items="${board.comments }" var="comment">
		<div>
			<div><span>${comment.writer }</span> ${comment.writeTimeString }</div>
			<div style="overflow:hidden;">
				<img src="${comment.profile }" style="width:60px;">
				<span>${comment.content }</span>
				<c:if test="${comment.isWriter ==true }">
					<button class="delete_comment" data-cno="${comment.cno }" style="float:right;">삭제</button>
				</c:if>
			</div>
		</div>			
	</c:forEach>
</div>
*/
}


// 비로그인이라면 : 아무것도 못해(default 값)
// 로그인했고 글쓴이인 경우
// 		제목 활성화, 내용에 ck 적용
//		btnArea에 변경/삭제 버튼 추가
//		comment_textarea 활성화, commentBtnArea에 "댓글 추가" 버튼 추가
// 로그인했고 글쓴이가 아닌 경우
//		추천, 비추 badge 활성화
// 		comment_textarea 활성화, commentBtnArea에 "댓글 추가" 버튼 추가
$(function() {
	if(isLogin==true && isWriter==true) {
		$("#title").prop("disabled", false);
		var ck = CKEDITOR.replace('content', {
				filebrowserUploadUrl : '/pboard/boards/ck?_csrf=${_csrf.token}'
		});
		
		var $btnArea = $("#btnArea");
		$("<button>").attr("id","update").attr("class","btn btn-info").text("변 경").appendTo($btnArea);
		$("<button>").attr("id","delete").attr("class","btn btn-info").text("삭 제").appendTo($btnArea);

		$("#comment_textarea").prop("readonly", false)
		.attr("placeholder","욕설이나 모욕적인 댓글은 삭제될 수 있습니다.");
		var $commentBtnArea = $("#commentBtnArea");
		$("<button>").attr("id", "write").attr("class","btn btn-info").text("댓글 추가").appendTo($commentBtnArea);
	} else if(isLogin==true && isWriter==false) {
			$("#good_btn").prop("disabled", false);
			$("#bad_btn").prop("disabled", false);
			$("#comment_textarea").prop("readonly", false)
				.attr("placeholder","욕설이나 모욕적인 댓글은 삭제될 수 있습니다.");
			
			var $commentBtnArea = $("#commentBtnArea");
			$("<button>").attr("id", "write").attr("class","btn btn-info").text("댓글 추가").appendTo($commentBtnArea);
		}
	$("#commentBtnArea").on("click","#write",function(){
		params = {
			bno : '${board.bno}',
			content: $("#comment_textarea").val(),
			_csrf : '${_csrf.token}'
				}
		$.ajax({
			url: '/pboard/comments',
			method: "post",
			data: params
			}).done((comments)=>printComments(comments)); 
		});
	$("#comments").on("click",".delete_comment",function(){
		params = {
			cno : $(this).data("cno"),
			bno : '${board.bno}',
			_csrf : '${_csrf.token}',
			_method: 'delete'
				}
		$.ajax({
			url: '/pboard/comments',
			method: "post",
			data: params
			}).done((comments)=>printComments(comments)); 
		});
	$("#commentBtnArea").on("click","#write",function(){
		params = {
			bno : '${board.bno}',
			content: $("#comment_textarea").val(),
			_csrf : '${_csrf.token}'
				}
		$.ajax({
			url: '/pboard/comments',
			method: "post",
			data: params
			}).done((comments)=>printComments(comments)); 
		});

	$("#attachment").on("click",".delete_attachment", function(){
		var params = {
			bno : $(this).data("bno"),
			_csrf : "${_csrf.token}",
			_method : "delete"
			}
		$.ajax({
				url: "/pboard/attachments/" + $(this).data("ano"),
				method: "post",
				data: params
			}).done((attachments)=>printAttachments(attachments));
		});
});
</script>
<title>Insert title here</title>
</head>
<body>
<div id="wrap">
	<div>
		<div id="title_div">
			<!-- 제목, 작성자 출력 영역 -->
			<div id="upper">
				<input type="text" id="title" disabled="disabled" value="${board.title }">
				<span id="writer">${board.writer }</span>
			</div>
			<!-- 글번호, 작성일, 아이피, 추천수, 조회수, 신고수 출력 영역 -->
			<div id="lower">
				<ul id="lower_left">
					<li>글번호<span id="bno">${board.bno }</span></li>
					<li><span id="writeTimeString">${board.writeTimeString }</span></li>
				</ul>
				<ul id="lower_right">
					<li><button type="button" class="btn btn-primary" id="good_btn" disabled="disabled">추천<span class="badge" id="goodCnt">${board.goodCnt }</span></button></li>
					<li><button type="button" class="btn btn-success" id="b" disabled="disabled">조회 <span class="badge" id="readCnt">${board.readCnt }</span></button></li>    
  					<li><button type="button" class="btn btn-danger" id="bad_btn" disabled="disabled">비추<span class="badge" id="badCnt">${board.badCnt }</span></button></li>        
				</ul>
			</div>
			<!-- 첨부파일 출력 영역 -->
			<div>
				<ul id="attachment">
					<c:forEach items="${board.attachments}" var="attachment">
						<li style="overflow:hidden; width: 300px;">
							<div style="float:left">
								<c:if test="${attachment.isImage==true }">
									<i class="fa fa-file-image-o"></i>
								</c:if>
								<c:if test="${attachment.isImage==false }">
									<i class="fa fa-paperclip"></i>
								</c:if>
								 <a href='/pboard/attachments/${attachment.ano }'>${attachment.originalFileName}</a>
							</div>
							<div style="float:right;">										
								<c:if test="${board.isWriter==true}">
									<span class='delete_attachment' data-ano='${attachment.ano}' data-bno='${attachment.bno}' 
										title='${attachment.originalFileName} 삭제' style='cursor:pointer;'>X</span>
								</c:if>
							</div>
						</li>
					</c:forEach>	
				</ul>
			</div>
		</div> 
		<!--  본문, 갱신 버튼, 삭제 버튼 출력 영역 -->
		<div id="content_div">
			<div class="form-group">
				<div class="form-control" id="content">${board.content }
				</div>
			</div>
			<div id="btnArea">
			</div>
		</div>
	</div>
	<div>
		<div class="form-group">
      		<label for="comment_teaxarea">댓글을 입력하세요</label>
      		<textarea class="form-control" rows="5" id="comment_textarea" readonly="readonly"
      			placeholder="댓글을 작성하려면 로그인 해주세요"></textarea>
    	</div>
    	<div id="commentBtnArea">
    	<!-- 로그인하면 댓글 달기 버튼을 추가 -->
		</div>
	</div>
	<hr>
	<div id="comments">
		<c:forEach items="${board.comments }" var="comment">
			<div>
				<div><span>${comment.writer }</span> ${comment.writeTimeString }</div>
				<div style="overflow:hidden;">
					<img src="${comment.profile }" style="width:60px;">
					<span>${comment.content }</span>
					<c:if test="${comment.isWriter ==true }">
						<button class="delete_comment" data-cno="${comment.cno }" style="float:right;">삭제</button>
					</c:if>
				</div>
			</div>			
		</c:forEach>
	</div>
</div>
</body>
</html>