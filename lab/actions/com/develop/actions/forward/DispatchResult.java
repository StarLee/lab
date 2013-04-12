package com.develop.actions.forward;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.develop.actions.action.ActionInvocation;
import com.develop.actions.result.AbstractResult;
import com.develop.actions.result.IResult;
import com.develop.actions.statistics.ActionStatistics;
import com.develop.actions.statistics.WebObject;

public class DispatchResult extends AbstractResult
{
	private Logger log=Logger.getLogger(this.getClass());
	public void execute(ActionInvocation actionInvocation)
	{
		HttpServletRequest request=(HttpServletRequest)ActionStatistics.getInstance().get(WebObject.SERVLETREQUEST);
		HttpServletResponse response=(HttpServletResponse)ActionStatistics.getInstance().get(WebObject.SERVLETRESPONSE);
		try
		{
			try
			{
				log.info("Ìø×ªÂ·¾¶"+getLocation());
				request.getRequestDispatcher(getLocation()).forward(request, response);
			} catch (ServletException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
