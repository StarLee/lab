package com.develop.actions.request;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.develop.actions.forward.Forward;
import com.develop.actions.resource.Resource;
import com.develop.actions.resource.ResourceStatistic;
import com.develop.actions.resource.XMLDefaultResource;

/**
 * 
 * @author starlee
 * 
 *         2011-7-1
 */
public class BasicFilter implements Filter
{

	private FilterConfig filterConfig;
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException
	{
		Forward forward=new Forward(arg0, arg1, filterConfig.getServletContext(), arg2);
		forward.forwardAction();
		return;
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		this.filterConfig=arg0;
		loadConfig();
	}
	/**
	 * º”‘ÿ◊ ‘¥
	 */
	private void loadConfig()
	{
		if(!ResourceStatistic.getInstance().isFlag())
		{
			Resource resource=new XMLDefaultResource();
			resource.parseData();
		}
		
	}
}
