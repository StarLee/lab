package com.develop.actions.statistics;

import java.util.HashMap;
import java.util.Map;

/**
 * �����漰����servlet��ص�һЩ���󣬱���ServletRequest,ServletResponse,ServletSession��
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
	 * ��ȡ��ǰ���ActionStatistic��
	 * @return
	 */
	public static ActionStatistics getInstance()
	{
		return (ActionStatistics)context.get();
	}
	/**
	 * ��WebObject���ֶζ�Ӧ
	 * @param name
	 * @return
	 */
	public Object get(String name)
	{
		return container.get(name);
	}
	/**
	 * ����һЩ���������ԣ����޸ģ�
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
