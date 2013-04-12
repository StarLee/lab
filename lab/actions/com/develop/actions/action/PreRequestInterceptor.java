package com.develop.actions.action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.develop.actions.statistics.WebObject;

/**
 * 对HttpServletRequest进行处理，以后可能还会进行重构
 * @author starlee
 *
 */
public class PreRequestInterceptor implements ActionInterceptor
{

	@Override
	public void invoke(ActionInvocation invocation)
	{
		HttpServletRequest request = (HttpServletRequest) invocation
				.getActionStatistics().get(WebObject.SERVLETREQUEST);
		Enumeration<String> names=  request.getParameterNames();
		BasicWrapper wrapper=new BasicWrapper(request);
		
		while(names.hasMoreElements())
		{
			String name=names.nextElement();
			if(request.getParameterValues(name).length>=2)//数组
				wrapper.setPassValue(name, request.getParameterValues(name));
			else
				wrapper.setPassValue(name, request.getParameter(name));
		}
		invocation.getActionStatistics().set(WebObject.SERVLETREQUEST,wrapper);
		invocation.invoke();
	}
	
}
