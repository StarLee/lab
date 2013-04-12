package com.develop.actions.action;
/**
 * 此对象保存的是处理类处理完的数据,取这个名字是与struts 2一致的，如果知道struts2的原码，你会发现她到底是个什么东西
 * @author starlee
 *
 */
public interface ValueStack
{
	/**
	 * 查找储存在里面的值
	 * @param name
	 * @return
	 */
	Object findValue(String name);
	/**
	 * 这两个是配合的
	 * @param key
	 * @param value
	 */
	public void setValue(String key,Object value);
}
