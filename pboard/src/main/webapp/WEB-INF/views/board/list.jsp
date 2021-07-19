<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div>
	<table class="table table-hover">
		<tr>
			<th>글번호</th><th style="width:20px;"></th><th>제목</th><th>작성자</th><th>시간</th><th>조회수</th>
		</tr>
		<tbody>
			<c:forEach items="${page.list }" var="b">
				<tr>
					<td>${b.bno}</td>
					<td>
						<c:if test="${b.attachmentCnt>0}">
							<i class="fa fa-paperclip"></i>
						</c:if>
					</td>
					<td>
					<a href="/pboard/board/read?bno=${b.bno}">${b.title}&nbsp;&nbsp;[${b.commentCnt}]</a></td>
					<td>${b.writer }</td>
					<td>${b.writeTimeString }</td>
					<td>${b.readCnt }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagination">
		<ul class="pagination">
				<c:if test="${page.prev > 0 }">
					<li><a href="/pboard/board/list?pageno=${page.pageno }">이전으로</a></li>
				</c:if>
				<c:forEach begin="${page.start }" end="${page.end }" var="i">
					<c:if test="${page.pageno eq i}">
						<li class="active"><a href="/pboard/board/list?pageno=${page.pageno }">${i }</a></li>
					</c:if>
					<c:if test="${page.pageno ne i}">
						<li><a href="/pboard/board/list?pageno=${page.pageno }">${i }</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${page.next > 0 }">
					<li><a href="/pboard/board/list?pageno=${page.pageno }">다음으로</a></li>
				</c:if>
		</ul>	
	</div>
</div>
</body>
</html>