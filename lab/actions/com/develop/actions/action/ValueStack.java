package com.develop.actions.action;
/**
 * �˶��󱣴���Ǵ����ദ���������,ȡ�����������struts 2һ�µģ����֪��struts2��ԭ�룬��ᷢ���������Ǹ�ʲô����
 * @author starlee
 *
 */
public interface ValueStack
{
	/**
	 * ���Ҵ����������ֵ
	 * @param name
	 * @return
	 */
	Object findValue(String name);
	/**
	 * ����������ϵ�
	 * @param key
	 * @param value
	 */
	public void setValue(String key,Object value);
}
