package com.develop.actions.action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.develop.actions.statistics.WebObject;

/**
 * ��HttpServletRequest���д����Ժ���ܻ�������ع�
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
			if(request.getParameterValues(name).length>=2)//����
				wrapper.setPassValue(name, request.getParameterValues(name));
			else
				wrapper.setPassValue(name, request.getParameter(name));
		}
		invocation.getActionStatistics().set(WebObject.SERVLETREQUEST,wrapper);
		invocation.invoke();
	}
	
}
