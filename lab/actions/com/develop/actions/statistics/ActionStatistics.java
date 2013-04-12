package com.develop.actions.statistics;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类涉及是与servlet相关的一些对象，比如ServletRequest,ServletResponse,ServletSession等
 * 
 * @author starlee
 * 
 *         2011-7-5
 */
public class ActionStatistics
{
	static ThreadLocal context =new ThreadLocal();
	private Map container=new HashMap();
	public ActionStatistics(Map context)
	{
		this.container=context;
	}
	/**
	 * 获取当前这个ActionStatistic。
	 * @return
	 */
	public static ActionStatistics getInstance()
	{
		return (ActionStatistics)context.get();
	}
	/**
	 * 与WebObject的字段对应
	 * @param name
	 * @return
	 */
	public Object get(String name)
	{
		return container.get(name);
	}
	/**
	 * 加入一些其它的特性（或修改）
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void set(String key,Object value)
	{
		container.put(key, value);
	}
	public void setActionStatistcs(ActionStatistics actionStatistics)
	{
		context.set(actionStatistics);
	}
}
