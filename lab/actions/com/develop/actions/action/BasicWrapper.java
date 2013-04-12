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
		//�����ַ���������������ŵĴ���
		if(String.class.isAssignableFrom(value.getClass()))
		{
			//value=StringEscapeUtils.escapeHtml((String)value);//���Ļ��ɾ���ı���
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

		value=StringEscapeUtils.escapeHtml("����");//����utf-8�ı�����ʽ
		System.out.println(value);
		value=StringEscapeUtils.escapeSql("����");
		System.out.println(value);
	}
	
	public class SpecialCode
	{
		private Map<String,String> map=new HashMap();
		public SpecialCode()
		{
			map.put("'", "\"");//������ȫ���˫����
			map.put("-", "����");//Ӣ�ĵ�-תΪ����
			map.put(";", "��");
			//TODO
			//δ��ɣ�������ֻ�Ƕ���sqlע�����������ط�������������ĵط����ؼ����Ƕ�sql��䣬��Ҫ��̬ƴ�Ӿͺ���
		}
	}
	
}
