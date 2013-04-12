package com.develop.actions.action;
/**
 * 装饰一下请求
 * @author starlee
 *
 */
public interface BasicHttpRequestWrapper
{
	/**
	 * 获取值
	 * @param name
	 * @return
	 */
	public Object getPassValue(String name);
	/**
	 * 设置值
	 * @param key
	 * @param value
	 */
	public void setPassValue(String key,Object value);
}
