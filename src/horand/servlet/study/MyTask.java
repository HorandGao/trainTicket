package horand.servlet.study;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletContext;  

public class MyTask extends java.util.TimerTask{  
  
    private ServletContext context = null;  
    public MyTask(ServletContext context) {  
        this.context = context;  
    }  
  
    public void run() {  
    	Statement stmt = null;
		 ResultSet rs = null;
		 int rs_update=100;
		 Connection conn=null;
		 int dateLength= 14;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			ArrayList<String> strOrderNum = new ArrayList<String> ();
			String str_sql = "select orderNum from bookings  where orderDelete=0 and orderType=1 and TIMESTAMPDIFF(MINUTE,orderDate,now())>"+dateLength;
			rs = stmt.executeQuery(str_sql);
			while (rs.next()) {
				strOrderNum.add(rs.getString("orderNum"));			
			}
			if(rs != null){
				rs.close();
			}

			for(int i=0 ;i<strOrderNum.size();i++){
				String path="http://115.28.158.46/cancelOrder.action?orderNum="+strOrderNum.get(i);
				 try {

                     URL url = new URL(path);

                     url.openStream();

              } catch (MalformedURLException e) {

                     e.printStackTrace();

              } catch (IOException e) {

                     e.printStackTrace();

              }
			}
			
			conn.commit();
	        conn.setAutoCommit(true);
			
			if (stmt != null) {
               stmt.close();
           }

           if (conn != null) {
               conn.close();
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
}  