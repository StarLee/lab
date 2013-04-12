package com.develop.actions.bean;

import java.util.Date;
import java.util.Map;

public class PageBean
{
	private String type="dispatcher";//类型，比如是dispatcher,还是redirect还是json,stream
	private String name="_temp_";//名字，从处理类返回的值 ，作为key,在一个page标签中必须是唯一的
	private Map<String,String> map;//请求的参数
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Map<String, String> getMap()
	{
		return map;
	}
	public void setMap(Map<String, String> map)
	{
		this.map = map;
	}
	
}
