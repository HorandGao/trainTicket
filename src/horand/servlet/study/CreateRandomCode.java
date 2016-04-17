package horand.servlet.study;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import net.sf.json.JSONObject;

public class CreateRandomCode {

	public static String createRandom(boolean numberFlag, int length){
		 String retStr = "";
		 String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		 int len = strTable.length();
		 boolean bDone = true;
		 do {
		  retStr = "";
		  int count = 0;
		  for (int i = 0; i < length; i++) {
		  double dblR = Math.random() * len;
		  int intR = (int) Math.floor(dblR);
		  char c = strTable.charAt(intR);
		  if (('0' <= c) && (c <= '9')) {
		   count++;
		  }
		  retStr += strTable.charAt(intR);
		  }
		  if (count >= 2) {
		  bDone = false;
		  }
		 } while (bDone);
		 return retStr;
		}
	
	
	public static boolean judgePhoneNum(String phone , String code){
		 Statement stmt = null;
		 ResultSet rs = null;
		 Connection conn;
		 boolean result = false;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
			stmt = conn.createStatement();
			
			String str_sql = "select * from phoneCode where phone = "+phone+" and TIMESTAMPDIFF(MINUTE,date,now())<10 order by date desc";
			rs = stmt.executeQuery(str_sql);
			if (rs.next() && rs.getString("code").equals(code)) {
				result = true;
			}
            
			if(rs != null){
				rs.close();
			}
			if (stmt != null) {
                stmt.close();
            }
 
            if (conn != null) {
                conn.close();
            }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
		
		return result;
	}
	
	public static boolean sendMessage(String phone , String name){
		
		//发送短信
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23340812", "c4b0bf75059430a7505c2d11a847879e");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("帅客出行");
		req.setSmsParamString("{\"name\":\""+name+"\"}");
		req.setRecNum(phone);
		req.setSmsTemplateCode("SMS_7375123");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			JSONObject result_array = JSONObject.fromObject(rsp.getBody());
			if(result_array.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result").get("success").equals(true)){
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
