package ajax.test.employees;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ajax.test.common.DAO;

public class BoardDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	// 종료
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
			if(conn!=null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// employee 리스트 가져오기
	public List<Employee> getEmpList(){
		conn = DAO.getConnection();
		String sql = "SELECT * FROM employees";
		Employee emp = null;
		List<Employee> list = new ArrayList<Employee>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				emp = new Employee();
				emp.setEmployeeId(rs.getInt("employee_id"));
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setSalary(rs.getInt("salary"));
				emp.setJobId(rs.getString("job_id"));
				emp.setEmail(rs.getString("email"));
				emp.setHireDate(rs.getString("hire_date"));
				list.add(emp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getNewBoard() {
		conn = DAO.getConnection();
		String sql = "select max(board_no)+1 board_no from board";
		int newBoardNo = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				newBoardNo = rs.getInt("board_no");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newBoardNo;
	}

	public int insertBoard(BoardDTO board) {
		conn = DAO.getConnection();
		int r = 0;
		String sql = "insert into board (board_no, title, content, writer, creation_date)"
				+ " values(?, ?, ?, 'test', sysdate)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBoardNo());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			r = pstmt.executeUpdate();
			System.out.println(r + " row inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	public void deleteBoard(int boardNo) {
		conn = DAO.getConnection();
		String sql = "delete board where board_no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			int r = pstmt.executeUpdate();
			System.out.println(r + " deleted. ");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateBoard(BoardDTO board) {
		conn = DAO.getConnection();
		String sql = "update board set title=?, content=? where board_no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setInt(3, board.getBoardNo());
			int r = pstmt.executeUpdate();
			System.out.println(r + " updated. ");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<BoardDTO> getBoardList() {
		conn = DAO.getConnection();
		String sql = "select * from board where parent_no is null order by 1";
		List<BoardDTO> list = new ArrayList<>();
		BoardDTO board = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				board = new BoardDTO();
				board.setBoardNo(rs.getInt("board_no"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setCreationDate(rs.getString("creation_date"));
				board.setParentNo(rs.getInt("parent_no"));
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public BoardDTO getBoard(int boardNo) {
		conn = DAO.getConnection();
		String sql = "select * from board where board_no = ?";
		BoardDTO board = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				board = new BoardDTO();
				board.setBoardNo(rs.getInt("board_no"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setCreationDate(rs.getString("creation_date"));
				board.setParentNo(rs.getInt("parent_no"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return board;
	}
}
