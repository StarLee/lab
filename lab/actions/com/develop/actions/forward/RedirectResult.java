package com.develop.actions.forward;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.develop.actions.action.ActionInvocation;
import com.develop.actions.result.AbstractResult;
import com.develop.actions.result.IResult;
import com.develop.actions.statistics.ActionStatistics;
import com.develop.actions.statistics.WebObject;

public class RedirectResult extends AbstractResult
{
	private Logger log=Logger.getLogger(this.getClass()); 
	/**
	 * #1 对于用户来说，String 类型的url是很让人困惑的，所以必须传入的是一个包装好的类才好。这样信息量也多，觉得还是上下文对象好点
	 * #1问题进行的重新设计8月18日,String url已经不存在了
	 */
	public void execute(ActionInvocation invocation)
	{
		HttpServletResponse response=(HttpServletResponse)ActionStatistics.getInstance().get(WebObject.SERVLETRESPONSE);
		HttpServletRequest request=(HttpServletRequest)ActionStatistics.getInstance().get(WebObject.SERVLETREQUEST);
		try
		{
			log.info("跳转路径"+getLocation());
			response.sendRedirect(request.getContextPath()+getLocation());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
