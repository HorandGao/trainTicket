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
 * Servlet implementation class addOrder
 */
@WebServlet("/addOrder.action")
public class addOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addOrder() {
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
	      ResultSet rs_query = null;
	      int rs =0;
	      Connection conn=null;
	      try {
	            Class.forName("com.mysql.jdbc.Driver");
	            conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	           
	            conn.setAutoCommit(false);
	            stmt = conn.createStatement();
	            int leftTicket=0;
	            String[] idcard = request.getParameter("idcards").split("a");
	            String str_trainNum = request.getParameter("trainNum");
	            String str_email = request.getParameter("email");
	            String str_srcStation="";
	            String str_desStation="";
	            String str_date = request.getParameter("date");
	            String str_seatType = request.getParameter("seatType");//1 2 3
	            String str_seatPrice = request.getParameter("seatPrice");
//	            String str_personNum = request.getParameter("personNum");
	            String str_startDateTime = "";
	            String str_endDateTime = "";
	            String str_name1=request.getParameter("name1");
	            String str_name2=request.getParameter("name2");
	            String str_name3=request.getParameter("name3");
	            String str_seatName="";
	            if(request.getMethod().equalsIgnoreCase("GET"))
	            {
	            	str_name1 = new String(str_name1.getBytes("iso8859-1"),"utf-8");
	            	str_name2 = new String(str_name2.getBytes("iso8859-1"),"utf-8");
	            	str_name3 = new String(str_name3.getBytes("iso8859-1"),"utf-8");
	            }
	            
	            
	            int str_totalPrice = Integer.parseInt(str_seatPrice)*(idcard.length-1);
	            String str_query="select * from trainInfo join ticketInfo on trainNum=train_num where trainNum='"+str_trainNum+"' and date='"+str_date+"' ";
	            rs_query = stmt.executeQuery(str_query);
	            //out.println(resultSetToJson(rs_query));

	            while (rs_query.next()) {
	                 //out.println(rs.getString("name")+"         "+rs.getString("sex")+"<br/>");
	            	str_srcStation = rs_query.getString("train_src");
	            	str_desStation = rs_query.getString("train_des");
	            	str_startDateTime = str_date+" "+ rs_query.getString("train_goTime");
	            	str_endDateTime = str_date +" "+ rs_query.getString("train_doneTime");
	            	leftTicket = Integer.parseInt(rs_query.getString("leftTicket"+str_seatType));
	            	str_seatName = rs_query.getString("seatType"+str_seatType);
	            }
	            if (rs_query != null) {
	                rs_query.close();
	            }
	            
	            
	            String str_updateTicketNum = "update ticketInfo set leftTicket"+str_seatType+"=leftTicket"+str_seatType+"-"+(idcard.length-1)
	            		+ " where trainNum='"+str_trainNum+"' and date='"+str_date+"'";
	            String str_insertOrder="";
	            String id1="",id2="",id3="";
	            if(idcard.length==4){
	            	id1 = idcard[1];
	            	id2 = idcard[2];
	            	id3 = idcard[3];
	            }
	            else if(idcard.length==3){
	            	id1 = idcard[1];
	            	id2 = idcard[2];
	            	id3 = "";
	            }else if(idcard.length==2){
	            	id1 = idcard[1];
	            	id2 = "";
	            	id3 = "";
	            }else if(idcard.length==1){
	            	id1 = id2 = id3 = "";
	            }
	            if(leftTicket >= idcard.length-1){
	            	rs = stmt.executeUpdate(str_updateTicketNum);
	            	str_insertOrder = "insert into bookings values(NULL,"
		            		+ "'"+str_trainNum+"','"+str_srcStation+"','"+str_desStation+"',"
		            		+ "'"+str_startDateTime+"','"+str_endDateTime+"',now(),'"
		            		+str_email+"',1,0,0,'"
		            		+str_totalPrice+"','"
		            		+id1+"','"+id2+"','"+id3+"','"+str_name1+"','"+str_name2+"','"+str_name3+"','"+str_seatName+"')";
	            	 rs = stmt.executeUpdate(str_insertOrder);
	 	            conn.commit();
	 	            conn.setAutoCommit(true);
	 	            if(rs>=1){
	 	            	out.write("{\"success\":\"1\",\"msg\":\"成功\"}");
	 	            }
	 	            else{
	 	            	out.write("{\"success\":\"0\",\"msg\":\"未知错误\"}");
	 	            }
	            }else{
	            	//表面该类型座次已经售罄
	            	//todo!!!
	            	int queueNum = 0;
	            	rs_query = stmt.executeQuery("select min(queneNum) as queueNum from bookings where orderDelete=0 and trainNum='"+str_trainNum+"' and srcDate = '"+str_startDateTime+"'");
	            	while (rs_query.next()) {
	            		queueNum = Integer.parseInt(rs_query.getString("queueNum")) - 1;
		            }
		            if (rs_query != null) {
		                rs_query.close();
		            }
	            	
	            	str_insertOrder = "insert into bookings values(NULL,"
		            		+ "'"+str_trainNum+"','"+str_srcStation+"','"+str_desStation+"',"
		            		+ "'"+str_startDateTime+"','"+str_endDateTime+"',now(),'"
		            		+str_email+"',0,"+queueNum+",0,'"
		            		+str_totalPrice+"','"
		            		+id1+"','"+id2+"','"+id3+"','"+str_name1+"','"+str_name2+"','"+str_name3+"','"+str_seatName+"')";
	            	
	            	rs = stmt.executeUpdate(str_insertOrder);
	 	            conn.commit();
	 	            conn.setAutoCommit(true);
	            	out.write("{\"success\":\"0\",\"msg\":\"余票不足,已进入排队系统\"}");
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
