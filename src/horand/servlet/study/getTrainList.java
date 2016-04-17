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
 * Servlet implementation class getTrainList
 */
@WebServlet("/getTrainList.action")
public class getTrainList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getTrainList() {
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
	      ResultSet rs = null;

	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
	           
	            stmt = conn.createStatement();
	            String date = request.getParameter("date");
	            String src = request.getParameter("src");
	            String des = request.getParameter("des");
	            
	            if(request.getMethod().equalsIgnoreCase("GET"))
	            {
	            	src = new String(src.getBytes("iso8859-1"),"utf-8");
	            	des = new String(des.getBytes("iso8859-1"),"utf-8");
	            }

	            //String str_temp = "select * from trainInfo join ticketInfo on train_num=trainNum where date='2016-04-01'";
	            String str_sql = "select * from trainInfo join ticketInfo on train_num=trainNum  where train_src='"+src+"' and train_des='"+des+"' and date='"+date+"'";
	            rs = stmt.executeQuery(str_sql);
	            
	            out.write(resultSetToJson(rs));
	            //out.println(date+src+des);
	           // out.println(str_sql);
	            
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
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
	
	public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
    {  

       JSONArray array = new JSONArray();  
        
       ResultSetMetaData metaData = rs.getMetaData();  
       int columnCount = metaData.getColumnCount();  
        while (rs.next()) {  
            JSONObject jsonObj = new JSONObject();  
             
            for (int i = 1; i <= columnCount; i++) {  
                String columnName =metaData.getColumnLabel(i);  
                String value = rs.getString(columnName);  
                jsonObj.put(columnName, value);  
            }   
            array.put(jsonObj);   
        }  
        
       return array.toString();  
    }  

}
