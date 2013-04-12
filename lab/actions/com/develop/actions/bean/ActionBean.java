package com.develop.actions.bean;

import java.util.Map;

/**
 * 这个bean类是对应一个请求的类<br/>
 * 系统中必须保证唯一的name值，所以这个地方必须得检查</br>
 * @author starlee
 *
 * 2011-7-4
 */
public class ActionBean
{
	private String className;
	private String name;//the identity;
	private String method="execute";//默认为execute方法
	
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
