package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addUser
 */
@WebServlet("/addUser.action")
public class addUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  response.setContentType("application/json; charset=utf-8");
		  PrintWriter out = response.getWriter();
	      Statement stmt = null;
	      int rs = 100;

	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train", "root", "horand");
	           
	            stmt = conn.createStatement();
	            String reqName = request.getParameter("name");
	            String reqPwd = request.getParameter("pwd");
	            String reqIDcard = request.getParameter("idcard");
	            String reqPhone = request.getParameter("phone");
	            String reqRealName = request.getParameter("realName");
	            if(reqName==null || reqPwd==null || reqIDcard==null || reqPhone==null || reqRealName==null ||
	            		reqName=="" || reqPwd=="" || reqIDcard=="" || reqPhone=="" || reqRealName==""){
	            	out.write("{\"success\":\"0\"}");
	            	return;
	            }
	            rs = stmt.executeUpdate("insert into user values(NULL,'"+reqName+"','"+reqPwd+"','"+reqIDcard+"',"+reqPhone+",'"+reqRealName+"')");
	            if(rs>=1){
	            	out.write("{\"success\":\"1\"}");
	            }
	            else{
	            	out.write("{\"success\":\"0\"}");
	            }
	            out.write(rs);
	            if (stmt != null) {
	                try {
	                	stmt.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
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
