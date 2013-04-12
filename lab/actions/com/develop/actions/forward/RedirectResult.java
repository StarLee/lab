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
	 * #1 �����û���˵��String ���͵�url�Ǻ���������ģ����Ա��봫�����һ����װ�õ���źá�������Ϣ��Ҳ�࣬���û��������Ķ���õ�
	 * #1������е��������8��18��,String url�Ѿ���������
	 */
	public void execute(ActionInvocation invocation)
	{
		HttpServletResponse response=(HttpServletResponse)ActionStatistics.getInstance().get(WebObject.SERVLETRESPONSE);
		HttpServletRequest request=(HttpServletRequest)ActionStatistics.getInstance().get(WebObject.SERVLETREQUEST);
		try
		{
			log.info("��ת·��"+getLocation());
			response.sendRedirect(request.getContextPath()+getLocation());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
