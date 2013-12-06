package ustc.ssh;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction {
	public String Login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if(UserBean.login(request.getParameter("user"), request.getParameter("pwd")))
			return "success";
		else 
			return "failure";
	}
}
