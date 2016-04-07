package horand.servlet.study;

import java.io.IOException;
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

/**
 * Servlet implementation class smsTest
 */
@WebServlet("/smsTest.action")
public class smsTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public smsTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String phoneNum = request.getParameter("phone");
		String name = request.getParameter("name");
		if(request.getMethod().equalsIgnoreCase("GET"))
        {
        	name = new String(name.getBytes("iso8859-1"),"utf-8");
        }
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23340812", "c4b0bf75059430a7505c2d11a847879e");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("大鱼测试");
		req.setSmsParamString("{\"code\":\"1234\",\"product\":\"alidayu\",\"customer\":\""+name+"\"}");
		req.setRecNum(phoneNum);
		req.setSmsTemplateCode("SMS_7330052");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			response.getWriter().append("true ").append(rsp.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			response.getWriter().append("error ");
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
