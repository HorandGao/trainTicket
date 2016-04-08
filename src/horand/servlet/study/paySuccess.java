package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class paySuccess
 */
@WebServlet("/paySuccess.action")
public class paySuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public paySuccess() {
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
		  PrintWriter out = response.getWriter();
	      Statement stmt = null;
	      int rs = 100;

	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	           
	            stmt = conn.createStatement();
	            String str_orderNum = request.getParameter("orderNum");
	            rs = stmt.executeUpdate("update bookings set orderType=2 where orderNum="+str_orderNum);
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
