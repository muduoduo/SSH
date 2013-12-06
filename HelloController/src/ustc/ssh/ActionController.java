package ustc.ssh;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;



public class ActionController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ActionController() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);//both deal by doPost
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
	
		//Load controller.xml
		try {
			Map<String, String> map=new HashMap<String,String>();
		
			//check action
			String action=null;
			String url=request.getRequestURL().toString();
			Pattern pattern=Pattern.compile("[^///]+[//.]{1}action", Pattern.CASE_INSENSITIVE);
			Matcher matcher=pattern.matcher(url);
			if(matcher.find())
				action=matcher.group(0);
			
			//decode
			Document doc=new SAXReader().read(new File(getClass().getClassLoader().getResource("/controller.xml").getFile().replaceAll("%20", " ")));
			List<Element> actions=doc.selectNodes("/action-controller/action");
			
			for(Element e:actions)
			{
				if(e.element("name").getText().toLowerCase().equals(action.toLowerCase().replace(".action", "")))
				{
					Class<?> interceptorClass=null;
					Method interceptorMethod=null; 
					//if has intercepter than execute intercepter   -----before
					if(e.element("interceptor-ref")!=null)
					{
						String interceptorName=e.selectSingleNode("./interceptor-ref/name").getText();
						List<Element> interceptorList=doc.selectNodes("/action-controller/interceptor");
						
						for(Element intp:interceptorList)
						{
							if(intp.elementText("name").toLowerCase().equals(interceptorName.toLowerCase()))
							{
								interceptorClass=Class.forName(intp.selectSingleNode("./class/name").getText());
								interceptorMethod=interceptorClass.getMethod(intp.selectSingleNode("./class/method").getText(), HttpServletRequest.class,HttpServletResponse.class,Map.class);
							}
						}
							
						//invoke intercepter
						map.put("action", action);
						map.put("type", "before");
						map.put("id", UUID.randomUUID().toString());
						interceptorMethod.invoke(interceptorClass.newInstance(),request, response,map);
					}
					
					Class<?> cls=Class.forName(e.selectSingleNode("./class/name").getText());
					Method method=cls.getMethod(e.selectSingleNode("./class/method").getText(),HttpServletRequest.class,HttpServletResponse.class);
					String status=method.invoke(cls.newInstance(),request,response).toString();
					List<Element> resultlList=e.selectNodes("./result");
					for(Element result:resultlList)
					{
						if(result.elementText("name").toLowerCase().equals(status.toLowerCase()))
						{
							switch (result.element("type").getText().toLowerCase()) 
							{
							case "forward":
								request.getRequestDispatcher(result.elementText("value")).forward(request, response);
								break;
							case "redirect":
								response.sendRedirect(result.elementText("value"));
								break;
							default:
								break;
							}
						}
					}
					
					//if has intercepter than execute intercepter   -----after
					if(interceptorMethod!=null)
					{
						map.put("result", status);
						map.put("type", "after");
						interceptorMethod.invoke(interceptorClass.newInstance(),request, response,map);
					}
				}
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
