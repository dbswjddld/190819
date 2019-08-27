<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ajax.test.employees.Employee"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String jdbc_driver = "oracle.jdbc.driver.OracleDriver";
	String jdbc_url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "hr";
	String passwd = "hr";
	Connection conn = null;
	PreparedStatement pstmt;
	ResultSet rs;
	
	
	try{
		Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url, user, passwd);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	// insert, select 기능 따로 구분하기
	String action = request.getParameter("action");
	// insert
	if(action.equals("insert")){
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String hireDate = request.getParameter("hireDate");
		String jobId = request.getParameter("jobId");
		int salary = Integer.parseInt(request.getParameter("salary"));
		
		String sql = "INSERT INTO employees (employee_id, first_name, last_name, email, hire_date, job_id, salary)"
				 + " VALUES ((SELECT MAX(employee_id) FROM employees)+1, ?, ?, ?, ?, ?, ?)";
		int r = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setString(4, hireDate);
			pstmt.setString(5, jobId);
			pstmt.setInt(6, salary);
			r = pstmt.executeUpdate();
			
			if (r > 0)	out.print("ok");
			else out.print("insert failed");
			
		} catch (Exception e) {
			out.print("exception");
			e.printStackTrace();
		}
		
	// select
	} else if(action.equals("select")){
		String sql = "SELECT * FROM employees WHERE rownum <= 10 ORDER BY employee_id desc";
		List<Employee> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			Employee emp = null;
			while(rs.next()) {
				emp = new Employee();
				emp.setEmployeeId(rs.getInt("employee_id"));
				emp.setFirstName(rs.getString("first_name"));
				emp.setLastName(rs.getString("last_name"));
				emp.setEmail(rs.getString("email"));
				emp.setSalary(rs.getInt("salary"));
				emp.setHireDate(rs.getString("hire_date"));
				emp.setJobId(rs.getString("job_id"));
				list.add(emp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// JSON을 쓰기 위해, WEB-INF 폴더 밑에 json-lib-2.4-jdk15.jar, commons 5개, ezmorph 붙여넣기
		//out.print(JSONArray.fromObject(list).toString());
		int a = list.size();
		int b = 0;
		out.print("[");
		for(Employee emp:list){
			out.print("{\"firstName\":\"" + emp.getFirstName() + 
				"\",\"lastName\":\"" + emp.getLastName() +
				"\",\"employeeId\":\"" + emp.getEmployeeId() +
				"\",\"email\":\"" + emp.getEmail() +
				"\",\"salary\":\"" + emp.getSalary() +
				"\",\"hireDate\":\"" + emp.getHireDate() +
				"\",\"jobId\":\"" + emp.getJobId() +
				"\"}");
			b++;
			if(a!=b) out.print(",");
		}
		out.print("]");
	}
%>