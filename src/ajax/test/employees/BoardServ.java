package ajax.test.employees;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

@WebServlet("/BoardServ")
public class BoardServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardServ() {
        super();
    }
    

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		String action = (String)request.getParameter("action");
		BoardDAO dao = new BoardDAO();
		
		System.out.println(action);
		
		if(action == null || action.equals("")) {
			out.print("invalid action");
		} else if(action.equals("list")) {	// -------------------- 리스트 출력
			List<BoardDTO> list = dao.getBoardList();
			out.print(JSONArray.fromObject(list).toString());
		} else if(action.equals("register")) {  //  ------------- 글 등록
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int newBno = dao.getNewBoard(); // 신규 게시글 번호
			BoardDTO board = new BoardDTO();
			board.setBoardNo(newBno);
			board.setTitle(title);
			board.setContent(content);
			
			if(dao.insertBoard(board) > 0) { // 입력 완료되면
				out.print(newBno);
			}
		} else if(action.equals("get")) {  // --------------------- 수정 버튼 클릭
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			BoardDTO board = new BoardDTO();
			board = dao.getBoard(boardNo);
			out.print(JSONArray.fromObject(board).toString());
		} else if(action.equals("update")) { // --------------------------- 수정 > 변경 버튼 클릭
			BoardDTO board = new BoardDTO();
			board.setTitle(request.getParameter("title"));
			board.setContent(request.getParameter("content"));
			board.setBoardNo(Integer.parseInt(request.getParameter("boardNo")));
			dao.updateBoard(board);
		} else if(action.equals("delete")) { // --------------------------- 삭제
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			dao.deleteBoard(boardNo);
		}
		
	}
}
