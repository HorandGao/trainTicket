package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class cancelOrder
 */
@WebServlet("/cancelOrder.action")
public class cancelOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cancelOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		//response.setContentType("text/html; charset=utf-8");
	      PrintWriter out = response.getWriter();
	      Connection conn=null;
	      Statement stmt = null;
	      int rs = 100;
	      ResultSet rs_query = null;
	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	           
	            conn.setAutoCommit(false);
	            stmt = conn.createStatement();
	            String req_orderNum = request.getParameter("orderNum");
	            String str_date="";
	            String str_longDate="";
	            String str_trainNum="";
	            String str_seatType="";
	            String str_phone="";
	            String str_realName="";
	            int str_queueNum=-1000;
	            int int_seatType=0;
	            String str_orderType="0";
	            int count=0;
	            rs_query = stmt.executeQuery("select * from bookings where  orderNum="+req_orderNum);
	            //out.write(resultSetToJson(rs_query));
	            while (rs_query.next()) {
	            	str_longDate = rs_query.getString("srcDate");
	            	str_date = rs_query.getString("srcDate").substring(0, 10);
	            	str_trainNum = rs_query.getString("trainNum");
	            	str_seatType = rs_query.getString("orderSeatType");
	            	str_orderType = rs_query.getString("orderType");
	            	str_queueNum = Integer.parseInt(rs_query.getString("queneNum"));
	            	if(!(rs_query.getString("personName1").equals(""))){
	            		count++;
	            	}
	            	if(!(rs_query.getString("personName2").equals(""))){
	            		count++;
	            	}
	            	if(!(rs_query.getString("personName3").equals(""))){
	            		count++;
	            	}
	            }
	            if(str_seatType.equals("鍟嗗姟搴�")||str_seatType.equals("杞� 鍗�")){
	            	int_seatType = 1;
	            }else if(str_seatType.equals("涓�绛夊骇")||str_seatType.equals("纭� 鍗�")){
	            	int_seatType = 2;
	            }else if(str_seatType.equals("浜岀瓑搴�")||str_seatType.equals("纭� 搴�")){
	            	int_seatType = 3;
	            }
	            
	            if (rs_query != null) {
	                rs_query.close();
	            }

	            //out.println(count+str_date+str_trainNum+int_seatType);
	            
	            if(!str_orderType.equals("0")){
	            	
	            	rs_query = stmt.executeQuery("select orderNum,max(queneNum) as maxQueueNum,phoneNum,realName from bookings,user where email=name and queneNum<0 and orderType=0 and orderDelete=0 and srcDate='"+str_longDate+"' and trainNum='"+str_trainNum+"'");
	            	
	            	if(rs_query.next() && rs_query.getString("phoneNum")!=null) {
	            			str_phone = rs_query.getString("phoneNum");
		            		str_realName = rs_query.getString("realName");
	            			rs = stmt.executeUpdate("update bookings set orderType=1,orderDate=now() where orderNum="+rs_query.getString("orderNum"));
	            			rs = stmt.executeUpdate("update bookings set orderDelete=1 where orderNum="+req_orderNum);
	            			CreateRandomCode.sendMessage(str_phone,str_realName);
	            		
	              	}else{
	              		String str_sql = "update ticketInfo set leftTicket"+int_seatType+" = leftTicket"+int_seatType+" + "+count
		            			+" where date='"+str_date+"' and trainNum='"+str_trainNum+"'";
		            	rs = stmt.executeUpdate(str_sql);
		            	
		            	rs = stmt.executeUpdate("update bookings set orderDelete=1 where orderNum="+req_orderNum);
	              	}
	            	if (rs_query != null) {
		                rs_query.close();
		            }
	            }else{
	            	rs = stmt.executeUpdate("update bookings set queneNum=queneNum+1 where queneNum<"+str_queueNum+" and orderDelete=0 and srcDate='"+str_longDate+"' and trainNum='"+str_trainNum+"'");
	            	rs = stmt.executeUpdate("update bookings set orderDelete=1 where orderNum="+req_orderNum);
	            	
	            }
	            
	            conn.commit();
	            conn.setAutoCommit(true);
	            if(rs>=0){
	            	out.write("{\"success\":\"1\"}");
	            }
	            else{
	            	out.write("{\"success\":\"0\"}");
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
	    	  try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	            e.printStackTrace();
	      }
	}

	public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
    {  
       // json鏁扮粍  
       JSONArray array = new JSONArray();  
        
       // 鑾峰彇鍒楁暟  
       ResultSetMetaData metaData = rs.getMetaData();  
       int columnCount = metaData.getColumnCount();  
        
       // 閬嶅巻ResultSet涓殑姣忔潯鏁版嵁  
        while (rs.next()) {  
            JSONObject jsonObj = new JSONObject();  
             
            // 閬嶅巻姣忎竴鍒�  
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
