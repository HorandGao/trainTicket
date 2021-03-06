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
	            String reqPwd = request.getParameter("pwd");
	            String reqIDcard = request.getParameter("idcard");
	            String reqPhone = request.getParameter("phone");
	            String reqRealName = request.getParameter("realName");
	            String reqCode = request.getParameter("code");
	            
	            if(request.getMethod().equalsIgnoreCase("GET"))
	            {
	            	reqRealName = new String(reqRealName.getBytes("iso8859-1"),"utf-8");
	            }
	            if(reqName==null || reqPwd==null || reqIDcard==null || reqPhone==null || reqRealName==null || reqCode==null ||
	            		reqName=="" || reqPwd=="" || reqIDcard=="" || reqPhone=="" || reqRealName=="" ||reqCode==""){
	            	out.write("{\"success\":\"0\",\"msg\":\"参数不能为空\"}");
	            	return;
	            }
	            resultSet = stmt.executeQuery("select count(*) as rowcount from user where name ='"+reqName+"'");
	            resultSet.next();
	            int rowcount = resultSet.getInt("rowcount");
	            if(rowcount==1){
	            	out.write("{\"success\":\"0\",\"msg\":\"该邮箱已被注册\"}");
	            	return;
	            }
	            resultSet.close();
	            
	            
	            
	            resultSet = stmt.executeQuery("select count(*) as rowcount from user where phoneNum ="+reqPhone);
	            resultSet.next();
	            rowcount = resultSet.getInt("rowcount");
	            if(rowcount==1){
	            	out.write("{\"success\":\"0\",\"msg\":\"该手机号已被注册\"}");
	            	return;
	            }
	            resultSet.close();
	            
	            boolean sign_code = CreateRandomCode.judgePhoneNum(reqPhone, reqCode);
	            if(!sign_code){
	            	out.write("{\"success\":\"0\",\"msg\":\"验证码不正确\"}");
	            	return;
	            }
	            
	            resultSet = stmt.executeQuery("select count(*) as rowcount from user where idCard ='"+reqIDcard+"'");
	            resultSet.next();
	            rowcount = resultSet.getInt("rowcount");
	            if(rowcount==1){
	            	out.write("{\"success\":\"0\",\"msg\":\"该证件号已被注册\"}");
	            	return;
	            }
	            resultSet.close();
	            
	            
	            rs = stmt.executeUpdate("insert into user values(NULL,'"+reqName+"','"+reqPwd+"','"+reqIDcard+"',"+reqPhone+",'"+reqRealName+"')");
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
