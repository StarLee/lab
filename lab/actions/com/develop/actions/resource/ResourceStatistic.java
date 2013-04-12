package com.develop.actions.resource;

import java.util.List;
import java.util.Map;

import com.develop.actions.bean.ActionBean;

public  class ResourceStatistic
{
	private Map<String,ActionBean> actionMap;
	private Map resultTypeMap;
	private List InterceptorList;
	private Resource resource;
	private boolean flag=false;//决定是否加载配置文件
	
	private static ResourceStatistic statistic=new ResourceStatistic();
	private ResourceStatistic()
	{
		
	}
	
	public static ResourceStatistic getInstance()
	{
		return statistic;
	}
	
	public ActionBean getAction(String namespace,String name)
	{
		return actionMap.get(namespace+"/"+name);
	}
	public Map getResultTypeMap()
	{
		return resultTypeMap;
	}

	public void setActionMap(Map actionMap)
	{
		this.actionMap = actionMap;
	}

	public void setResultTypeMap(Map resultTypeMap)
	{
		this.resultTypeMap = resultTypeMap;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	public List getInterceptorList()
	{
		return InterceptorList;
	}

	public void setInterceptorList(List interceptorList)
	{
		InterceptorList = interceptorList;
	}
	
	
}
