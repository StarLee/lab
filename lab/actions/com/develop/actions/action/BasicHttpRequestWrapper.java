package com.develop.actions.action;
/**
 * װ��һ������
 * @author starlee
 *
 */
public interface BasicHttpRequestWrapper
{
	/**
	 * ��ȡֵ
	 * @param name
	 * @return
	 */
	public Object getPassValue(String name);
	/**
	 * ����ֵ
	 * @param key
	 * @param value
	 */
	public void setPassValue(String key,Object value);
}
