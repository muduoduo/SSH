 package ustc.ssh;

import java.util.HashMap;
import java.util.Map;

public class UserBean {
	static Map<String, String> userList=null;
	static
	{
		userList =new HashMap<String,String>();
		userList.put("muduo", "123");
		userList.put("admin", "123");
	}
	public static boolean login(String user,String pwd)
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pwd.equals(userList.get(user));
	}
}
