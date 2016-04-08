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

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class smsVerify
 */
@WebServlet("/getVerifyCode.action")
public class getVerifyCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getVerifyCode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String phoneNum = request.getParameter("phone");
		String str_type = request.getParameter("type");
		PrintWriter out = response.getWriter();
		Statement stmt = null;
		int rs = 0;
		
		if(request.getMethod().equalsIgnoreCase("GET"))
        {
			str_type = new String(str_type.getBytes("iso8859-1"),"utf-8");
        }
		String str_code = CreateRandomCode.createRandom(true, 4);
		
		//code插入数据库
		try{
			Class.forName("com.mysql.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://115.28.158.46:3306/train?user=root&password=horand&useUnicode=true&characterEncoding=utf8");
	       
	        stmt = conn.createStatement();
	        rs = stmt.executeUpdate("insert into phoneCode values(NULL,"+phoneNum+",now(),'"+str_code+"')");
	        if (stmt != null) {
                stmt.close();
            }
 
            if (conn != null) {
                conn.close();
            }
		 } catch (Exception e) {
	            e.printStackTrace();
	            return;
	      }
		
		
		
		//发送短信
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23340812", "c4b0bf75059430a7505c2d11a847879e");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("身份验证");
		req.setSmsParamString("{\"code\":\""+str_code+"\",\"product\":\""+str_type+"\"}");
		req.setRecNum(phoneNum);
		req.setSmsTemplateCode("SMS_7330053");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			JSONObject result_array = JSONObject.fromObject(rsp.getBody());
			if(result_array.getJSONObject("alibaba_aliqin_fc_sms_num_send_response").getJSONObject("result").get("success").equals(true)){
				out.write("{\"success\":\"1\"}");
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			out.write("{\"success\":\"0\"}");
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
