package com.develop.actions.test;

import java.util.ArrayList;
import java.util.List;
/**
 * 这是一个测试例子
 * @author starlee
 *
 */
public class Test implements Basic
{

	private String id;
	private List list;
	private String[] testStr;
	public String execute()
	{
		for(String str:testStr)
		{
			System.out.println(str);
		}
		
		
		list=new ArrayList();
		for(int i=0;i<10;i++)
			list.add(i);
		if(true)
		{
			return "ok";
		}
		else
		{
			return "fail";
		}
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public List getList()
	{
		return list;
	}
	public void setList(List list)
	{
		this.list = list;
	}
	public void setTestStr(String[] testStr)
	{
		this.testStr = testStr;
	}
}
