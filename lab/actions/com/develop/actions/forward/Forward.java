package com.develop.actions.forward;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.develop.actions.bean.ActionBean;
import com.develop.actions.bean.PageBean;
import com.develop.actions.resource.ResourceStatistic;
import com.develop.actions.statistics.ActionStatistics;
import com.develop.actions.statistics.WebObject;

/**
 * 此类专门用来对付页面跳转的
 * 是一个处理核心的类
 * @author starlee
 *         2011-7-6
 */
public class Forward
{
	private Logger log = Logger.getLogger(this.getClass());
	private ActionBean actionBean =null;
	private ServletRequest req;
	private ServletResponse resp;
	private ServletContext context;
	private FilterChain filterChain;
	
	public Forward(ServletRequest req, ServletResponse resp,
			ServletContext context, FilterChain filterChain)
	{
		this.req=req;
		this.resp=resp;
		this.context=context;
		this.filterChain=filterChain;
		wrapContext(req, resp,context);
		HttpServletRequest request = (HttpServletRequest) req;
		String requestURI = request.getRequestURI();
		this.actionBean=createMapping(requestURI.substring(request
				.getContextPath().length(), requestURI.length()));
	}

	public void forwardAction()
			throws IOException, ServletException
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if (this.actionBean == null)
		{
			filterChain.doFilter(req, resp);
			return;
		}
		else
			if(actionBean.getClassName()==null)
			{
				Map actionMap=actionBean.getPages();
				if(actionMap.size()!=1)
				{
					throw new RuntimeException("你这个action似乎只是普通的转换地址而已，但是我不知道如果处理你的页面，因为它的对应页面不是一个。");
				}
				else
				{
					Set<Map.Entry> set=actionMap.entrySet();
					Iterator<Map.Entry> it= set.iterator();
					if(it.hasNext())
					{
						Map.Entry entry=it.next();
						PageBean url=(PageBean)entry.getValue();
						req.getRequestDispatcher(url.getMap().get("location")).forward(req, response);
						return ;
					}
				}
			}
		DefaultActionInvocation actionInvocation=new DefaultActionInvocation(actionBean,ResourceStatistic.getInstance());
		actionInvocation.invoke();
	}

	/**
	 * @param requestURI
	 * @return 普通的请求（如jsp,jpg,js,css,以及servlet(名字没有命名为.do后缀的)之类的），则全部返回0
	 */
	private ActionBean createMapping(String requestURI)
	{
		if (log.isInfoEnabled())
			log.info("looking page " + requestURI);

		String requestResource = requestURI.substring(
				requestURI.lastIndexOf("/") + 1, requestURI.length());
		String namespace = requestURI.substring(0, requestURI.lastIndexOf("/"));

		String subfix = "do";// 默认的后缀,如果像Struts那样的话，这个地方可以写成配置文件的形式。
		String prefix = requestResource;
		if (requestResource.contains("."))
		{
			int pointPosi = requestResource.lastIndexOf(".");
			subfix = requestResource.substring(pointPosi + 1,
					requestResource.length());
			prefix = requestResource.substring(0, pointPosi);

			if (!subfix.equals("do") || prefix.equals(""))
			{
				return null;
			}
			
			
			this.actionBean = (ActionBean) ResourceStatistic.getInstance().getAction(namespace, prefix);
			if (actionBean == null)// 并没有这个资源，则抛出资源不存在。所以这个地方可以写页面不存在，则给出一个友情页面。当然这个地方甚至可以用一个404错误
				throw new RuntimeException("the url doesn't exist,at the namespace: "
						+ namespace + ", name: " + prefix);
			return actionBean;
		} else
			return null;
	}

	
	public ActionBean getMapping()
	{
		return this.actionBean;
	}
	/**
	 * 设置此次环境
	 * @param req
	 * @param resp
	 */
	private void wrapContext(ServletRequest req, ServletResponse resp,ServletContext servletContext)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(WebObject.SERVLETREQUEST, req);
		map.put(WebObject.SERVLETRESPONSE, resp);
		map.put(WebObject.SERVLETCONTEXT, servletContext);
		ActionStatistics statistics = new ActionStatistics(map);
		statistics.setActionStatistcs(statistics);
	}

	
}
