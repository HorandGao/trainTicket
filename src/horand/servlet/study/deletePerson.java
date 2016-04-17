package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class deletePerson
 */
@WebServlet("/deletePerson.action")
public class deletePerson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deletePerson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  request.setCharacterEncoding("utf-8");
		  response.setCharacterEncoding("utf-8");
		  response.setContentType("application/json; charset=utf-8");
		  //response.setContentType("text/html; charset=utf-8");
		  PrintWriter out = response.getWriter();
	      Statement stmt = null;
	      int rs = -1;
	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	           
	            stmt = conn.createStatement();
	            String str_name = request.getParameter("name");
	            String str_idcard = request.getParameter("idcard");
	            
	            if(request.getMethod().equalsIgnoreCase("GET"))
	            {
	            	str_name = new String(str_name.getBytes("iso8859-1"),"utf-8");
	            }

	            String str_sql = "update person set personDelete=1 where user_name='"+str_name+"' and idcard='"+str_idcard+"'";
	            rs = stmt.executeUpdate(str_sql);
	            
	            if(rs>=0){
	            	out.write("{\"success\":\"1\",\"msg\":\"成功\"}");
	            }
	            else{
	            	out.write("{\"success\":\"0\",\"msg\":\"未知错误\"}");
	            }
	            
	            if (stmt != null) {
	                	stmt.close();
	            }
	 
	            if (conn != null) {
	                   conn.close();
	            }
	            
	      } catch (Exception e) {
	            e.printStackTrace();
	      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
