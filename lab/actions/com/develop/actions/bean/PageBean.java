package com.develop.actions.bean;

import java.util.Date;
import java.util.Map;

public class PageBean
{
	private String type="dispatcher";//���ͣ�������dispatcher,����redirect����json,stream
	private String name="_temp_";//���֣��Ӵ����෵�ص�ֵ ����Ϊkey,��һ��page��ǩ�б�����Ψһ��
	private Map<String,String> map;//����Ĳ���
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
