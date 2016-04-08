package horand.servlet.study;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class verifyPhoneCode
 */
@WebServlet("/verifyPhoneCode.action")
public class verifyPhoneCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public verifyPhoneCode() {
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
		
		String str_phone = request.getParameter("phone");
		String str_code = request.getParameter("code");
		boolean result = CreateRandomCode.judgePhoneNum(str_phone, str_code);
		
		if(result){
			out.write("{\"success\":\"1\"}");
		}else{
			out.write("{\"success\":\"0\"}");
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
