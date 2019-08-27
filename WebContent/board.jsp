<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<script src = "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
			var requestPage = "${pageContext.request.contextPath}/BoardServ";
			
			// 리스트 출력
			$.ajax({
				url:requestPage,
				data:{action:"list"},
				dataType:"json",
				success: function(result){
					console.log(result);
					var $table = $("<table>").attr("id", "tab1").attr("border","1");
					$.map(result, function(r, i){
						$tr = $("<tr>").attr("id", r.boardNo).append(
							$("<td>").text(r.boardNo).attr("width", "30"),
							$("<td>").text(r.title).attr("width", "100"),
							$("<td>").text(r.content).attr("width", "370"),
							$("<td>").html($("<button>").text("수정").click(onupdate)),
							$("<td>").html($("<button>").text("삭제").click(ondelete))
						)
						$table.append($tr);
					})
					$("#show").append($table);
				}
			});
			
			// 삭제
			function ondelete(){
				console.log("delete");
				var boardNo = $(this).parent().parent().children().eq(0).text();
				$.ajax({
					url:requestPage,
					data:{action:"delete",
						boardNo:boardNo},
					success:function(result){
						console.log(boardNo + "삭제");
						$("#" + boardNo).empty();
						// ↑삭제된 댓글 없애기
					}
				});
			}
			
			// 수정
			function onupdate(){
				console.log("update");
				var boardNo = $(this).parent().parent().children().eq(0).text();
				$.ajax({
					url:requestPage,
					data:{action:"get",
						boardNo: boardNo},
					dataType:"json",
					success:function(result){
						$("#" + boardNo).after(
							$("<tr>").append(
								$("<td>").html(boardNo).css("color", "white"),
								$("<td>").html($("<input>").attr("type", "text").val(result[0].title)),
								$("<td>").html($("<input>").attr("type", "text").val(result[0].content)),
								$("<td>").html($("<button>").text("변경").click(updaterow))
							)
						);
						$("#" + boardNo).children().eq(3).children().eq(0).prop("disabled", true);
						// ↑수정 버튼 클릭 못하게
					}
				});
			}
			
			// 수정 - 변경 버튼 눌렀을 때
			function updaterow(){
				var $id = $(this).parent().parent().children().eq(0).text();
				var $title = $(this).parent().parent().children().eq(1).children().eq(0).val();
				var $content = $(this).parent().parent().children().eq(2).children().eq(0).val();
				$.ajax({
					url:requestPage,
					data:{
						action:"update",
						boardNo: $id,
						title: $title,
						content: $content
					},
					success: function(result){
						$("#" + $id).children().eq(1).text($title);
						$("#" + $id).children().eq(2).text($content);
						// ↑원래 댓글 내용 수정되게
						$("#" + $id).children().eq(3).children().eq(0).prop("disabled", false);
						// ↑수정버튼 클릭 가능하게 만듦
						$("#" + $id).next().empty();
						// ↑수정하는 부분 없애기
					}
				})
			}
			
			// 등록
			$("#register").on("click", function(){
				$.ajax({
					url:requestPage,
					data:{action:"register", 
						  title:$("#title").val(),
						  content:$("#content").val()
						  },
					success: function(result){
						console.log(result + " 등록 완료");
						$("#tab1").append(
							$("<tr>").append(
								$("<td>").text(result),
								$("<td>").text($("#title").val()),
								$("<td>").text($("#content").val()),
								$("<td>").html($("<button>").text("수정").click(onupdate)),
								$("<td>").html($("<button>").text("삭제").click(ondelete))
							)
						);
						$("#title").val("");
						$("#content").val("");
					}
				});
			});
		});
	</script>
</head>
<body>
	<div id = "show"></div>
	<form id = "frm1">
		<table>
			<tr>
				<td>Title : <input type = "text" id = "title" name = "title"></td>
				<td>Content : <input type = "text" id = "content" name = "content"></td>
				<td><input type = "button" id = "register" value = "등록"></td>
			</tr>
		</table>
	</form>
</body>
</html>