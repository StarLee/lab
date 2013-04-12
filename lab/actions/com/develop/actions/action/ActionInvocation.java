package com.develop.actions.action;


import com.develop.actions.bean.PageBean;
import com.develop.actions.resource.ResourceStatistic;
import com.develop.actions.statistics.ActionStatistics;

/**
 * 对一个Action被调用，这个地方，是用来持有返回值，返回类型的地方。
 * @author starlee
 *
 */
public interface ActionInvocation
{
	/**
	 * 
	 * @return 结果的类型
	 */
	public String getResult();
	/**
	 * 值就是保存这个地方的
	 * @return
	 */
	public ValueStack getValueStack();
	/**
	 * 触发过滤器的动作
	 * @return
	 */
	public void invoke();
	/**
	 * 取得整个环境,http环境
	 * @return
	 */
	public ActionStatistics getActionStatistics();
	/**
	 * 获取请求的方法
	 * @return
	 */
	public String getRequestMethod();
	/**
	 * 获取请求的类
	 * @return
	 */
	public Object getRequestObject(); 
	/**
	 * 获取xml配置信息生成的数据结构
	 * @return
	 */
	public ResourceStatistic getConfiguration();
	
}
