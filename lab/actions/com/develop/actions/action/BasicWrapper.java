package com.develop.actions.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.chainsaw.Main;

import com.mysql.jdbc.StringUtils;

public class BasicWrapper extends HttpServletRequestWrapper implements BasicHttpRequestWrapper
{
	private Map formValue=new HashMap();
	public BasicWrapper(HttpServletRequest request)
	{
		super(request);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getPassValue(String name)
	{
		// TODO Auto-generated method stub
		return formValue.get(name);
	}

	@Override
	public void setPassValue(String key, Object value)
	{
		//对于字符串，进行特殊符号的处理
		if(String.class.isAssignableFrom(value.getClass()))
		{
			//value=StringEscapeUtils.escapeHtml((String)value);//中文会变成具体的编码
			value=StringEscapeUtils.escapeSql((String)value);
		}
		formValue.put(key, value);
	}

	public static void main(String[] args)
	{
		String value="<a href='dfdfd;'>dfdf</a><script>" +
				"" +
				"function (){alert}		</script>";
		//value=StringEscapeUtils.escapeHtml((String)value);
		if(String.class.isAssignableFrom(value.getClass()))
		{
			
			value=StringEscapeUtils.escapeJavaScript(value);
		}

		value=StringEscapeUtils.escapeHtml("李星");//会变成utf-8的编码形式
		System.out.println(value);
		value=StringEscapeUtils.escapeSql("李星");
		System.out.println(value);
	}
	
	public class SpecialCode
	{
		private Map<String,String> map=new HashMap();
		public SpecialCode()
		{
			map.put("'", "\"");//单引号全变成双引号
			map.put("-", "——");//英文的-转为中文
			map.put(";", "；");
			//TODO
			//未完成，待续，只是对于sql注入觉得在这个地方处理并不是理想的地方，关键还是对sql语句，不要动态拼接就好了
		}
	}
	
}
