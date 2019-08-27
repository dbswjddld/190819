package ajax.test.employees;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@WebServlet("/EmpServlet")
public class EmpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmpServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		List<Employee> list = dao.getEmpList();
		JSONArray ary = new JSONArray();	// import org.json.simple.JSONArray;
		JSONArray orig = new JSONArray();
		JSONObject obj = new JSONObject();	// import org.json.simple.JSONObject;
		for(Employee emp : list) {
			ary = new JSONArray();
			ary.add(emp.getFirstName());
			ary.add(emp.getLastName());
			ary.add(emp.getJobId());
			ary.add(emp.getEmail());
			ary.add(emp.getHireDate());
			ary.add(emp.getSalary());
			orig.add(ary); // 다시 배열로 감싸겠다
			// https://datatables.net/examples/ajax/simple.html 를 보면 데이터가 배열안에 배열로 되어있다
		}
		obj.put("data", orig);
		response.getWriter().print(obj);
		// !!이 페이지만 run해서 데이터 형식이 맞는지 확인하기
	}


}
