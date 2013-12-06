package ustc.ssh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class LogWriter {
	@SuppressWarnings("deprecation")
	public void Log(HttpServletRequest request, HttpServletResponse response,Map<String ,String> map) throws IOException, DocumentException
	{
		File file=new File(getClass().getClassLoader().getResource("").getFile().replaceAll("%20", " ")+"/log.xml");
		Document document=null;
		XMLWriter xmlWriter=null;
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		
		//check file exists
		if(!file.exists())
		{
			document=DocumentHelper.createDocument();
			document.addElement("log");
			xmlWriter=new XMLWriter(new FileOutputStream(file),format);
			xmlWriter.write(document);
			xmlWriter.close();
		}
		
		SAXReader saxReader=new SAXReader();
		document=saxReader.read(file);
		Element rootElement=(Element) document.getRootElement().selectSingleNode("/log");
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=simpleDateFormat.format(new Date()).toString();
		
		if(map.get("type").equals("before"))
		{
			Element action=rootElement.addElement("action");
			action.setAttributeValue("id", map.get("id"));
			action.addElement("name").setText(map.get("action"));
			action.addElement("s-time").setText(time);
		}
		
		if(map.get("type").equals("after"))
		{
			Element action=(Element) document.selectSingleNode("//action[@id='"+map.get("id")+"']");
			action.addElement("e-time").setText(time);
			action.addElement("result").setText(map.get("result"));
		}
		
		//write back
		xmlWriter=new XMLWriter(new FileOutputStream(file),format);
		xmlWriter.write(document);
		xmlWriter.close();
	}
}
