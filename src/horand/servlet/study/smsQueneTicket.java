package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class smsQueneTicket
 */
@WebServlet("/smsQueneTicket.action")
public class smsQueneTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public smsQueneTicket() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String phoneNum = request.getParameter("phone");
		String str_name = request.getParameter("name");
		PrintWriter out = response.getWriter();
		if(request.getMethod().equalsIgnoreCase("GET"))
        {
			str_name = new String(str_name.getBytes("iso8859-1"),"utf-8");
        }
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23340812", "c4b0bf75059430a7505c2d11a847879e");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("帅客出行");
		req.setSmsParamString("{\"name\":\""+str_name+"\"}");
		req.setRecNum(phoneNum);
		req.setSmsTemplateCode("SMS_7375123");
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
