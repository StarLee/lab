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
 * ����ר�������Ը�ҳ����ת��
 * ��һ��������ĵ���
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
					throw new RuntimeException("�����action�ƺ�ֻ����ͨ��ת����ַ���ѣ������Ҳ�֪������������ҳ�棬��Ϊ���Ķ�Ӧҳ�治��һ����");
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
	 * @return ��ͨ��������jsp,jpg,js,css,�Լ�servlet(����û������Ϊ.do��׺��)֮��ģ�����ȫ������0
	 */
	private ActionBean createMapping(String requestURI)
	{
		if (log.isInfoEnabled())
			log.info("looking page " + requestURI);

		String requestResource = requestURI.substring(
				requestURI.lastIndexOf("/") + 1, requestURI.length());
		String namespace = requestURI.substring(0, requestURI.lastIndexOf("/"));

		String subfix = "do";// Ĭ�ϵĺ�׺,�����Struts�����Ļ�������ط�����д�������ļ�����ʽ��
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
			if (actionBean == null)// ��û�������Դ�����׳���Դ�����ڡ���������ط�����дҳ�治���ڣ������һ������ҳ�档��Ȼ����ط�����������һ��404����
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
	 * ���ô˴λ���
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
