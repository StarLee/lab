package com.develop.actions.bean;

import java.util.Map;

/**
 * ���bean���Ƕ�Ӧһ���������<br/>
 * ϵͳ�б��뱣֤Ψһ��nameֵ����������ط�����ü��</br>
 * @author starlee
 *
 * 2011-7-4
 */
public class ActionBean
{
	private String className;
	private String name;//the identity;
	private String method="execute";//Ĭ��Ϊexecute����
	
	private Map pages;
	public String getClassName()
	{
		return className;
	}
	public void setClassName(String className)
	{
		this.className = className;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Map getPages()
	{
		return pages;
	}
	public void setPages(Map pages)
	{
		this.pages = pages;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
}
