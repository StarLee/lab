package com.develop.actions.forward;
/**
 * 返回的是JSONResult格式
 */
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import com.develop.actions.action.ActionInvocation;
import com.develop.actions.action.ValueStack;
import com.develop.actions.result.IResult;
import com.develop.actions.statistics.WebObject;

public class JSONResult implements IResult
{
	
	private Logger log=Logger.getLogger(this.getClass());
	
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void execute(ActionInvocation actionInvocation)
	{
		log.info("json格式数据");
		HttpServletResponse response = (HttpServletResponse) actionInvocation
				.getActionStatistics().get(WebObject.SERVLETRESPONSE);
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Expires", "Mon,12 May 1970 00:00:00 GMT");// 此网页永远是过期
		response.setHeader("Pragma", "no-cache");// 永远不要从缓存读数据
		ValueStack valueStack = actionInvocation.getValueStack();
		Object object = valueStack.findValue(name);
		System.out.println(object);
		JSONArray jsonArray = JSONArray.fromObject(object);
		PrintWriter out =null;
		try
		{
			out = response.getWriter();
			out.print(jsonArray.toString());
			out.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(out!=null)
				out.close();
		}
	}
}
