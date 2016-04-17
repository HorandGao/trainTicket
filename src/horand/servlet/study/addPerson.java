package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addPerson
 */
@WebServlet("/addPerson.action")
public class addPerson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addPerson() {
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
	      ResultSet resultSet = null;
	      int rs = 100;

	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	           
	            stmt = conn.createStatement();
	            String reqName = request.getParameter("name");
	            String reqRealName = request.getParameter("realName");
	            String reqIDcard = request.getParameter("idcard");
	            
	            if(request.getMethod().equalsIgnoreCase("GET"))
	            {
	            	reqRealName = new String(reqRealName.getBytes("iso8859-1"),"utf-8");
	            }
	            if(reqName==null || reqIDcard==null || reqRealName==null||
	            		reqName=="" || reqIDcard=="" ||reqRealName=="" ){
	            	out.write("{\"success\":\"0\",\"msg\":\"参数不能为空\"}");
	            	return;
	            }
	            
	            resultSet = stmt.executeQuery("select count(*) as rowcount from person where idcard ='"+reqIDcard+"' and user_name='"+reqName+"'");
	            resultSet.next();
	            int rowcount = resultSet.getInt("rowcount");
	            if(rowcount==1){
	            	out.write("{\"success\":\"0\",\"msg\":\"该联系人已经存在\"}");
	            	return;
	            }
	            resultSet.close();
	            
	            
	            rs = stmt.executeUpdate("insert into person values(NULL,'"+reqRealName+"','"+reqIDcard+"','"+reqName+"')");
	            if(rs>=1){
	            	out.write("{\"success\":\"1\",\"msg\":\"成功\"}");
	            }
	            else{
	            	out.write("{\"success\":\"0\",\"msg\":\"未知错误\"}");
	            }
	            out.write(rs);
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
