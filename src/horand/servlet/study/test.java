package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      response.setContentType("application/json; charset=utf-8");
      PrintWriter out = response.getWriter();
      Statement stmt = null;
      ResultSet rs = null;
      String req_name = request.getParameter("name");
      String req_password = request.getParameter("password");

      try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
           
            stmt = conn.createStatement();
            
            rs = stmt.executeQuery("select *,count(*) as rowcount from user where name ='"+ req_name+"' and password='"+req_password+"'");
            out.write(resultSetToJson(rs));
//            int rowcount = rs.getInt("rowcount");
//            while (rs.next()) {
//                 //out.println(rs.getString("name")+"         "+rs.getString("sex")+"<br/>");
//            	user_name = rs.getString("name");
//            	user_pwd = rs.getString("password");
//            }
//            String result = "{\"success\",\"0\"}";
            
//            if(req_name.equals(user_name) && req_password.equals(user_pwd)){
//            result= "{\"success\":\"1\"}";
//            }
//            if(rowcount>0){
//            	result= "{\"success\":\"1\"}";
//            }
            
            
         // 关闭记录集
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
  
            // 关闭声明
            if (stmt != null) {
                try {
                	stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
 
            // 关闭链接对象
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
    public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
    {  
       // json数组  
       JSONArray array = new JSONArray();  
        
       // 获取列数  
       ResultSetMetaData metaData = rs.getMetaData();  
       int columnCount = metaData.getColumnCount();  
        
       // 遍历ResultSet中的每条数据  
        while (rs.next()) {  
            JSONObject jsonObj = new JSONObject();  
             
            // 遍历每一列  
            for (int i = 1; i <= columnCount; i++) {  
                String columnName =metaData.getColumnLabel(i);  
                String value = rs.getString(columnName);  
                jsonObj.put(columnName, value);  
            }   
            array.put(jsonObj);   
        }  
        
       return array.toString();  
    }  
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
