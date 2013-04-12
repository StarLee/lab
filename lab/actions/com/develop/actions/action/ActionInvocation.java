package com.develop.actions.action;


import com.develop.actions.bean.PageBean;
import com.develop.actions.resource.ResourceStatistic;
import com.develop.actions.statistics.ActionStatistics;

/**
 * ��һ��Action�����ã�����ط������������з���ֵ���������͵ĵط���
 * @author starlee
 *
 */
public interface ActionInvocation
{
	/**
	 * 
	 * @return ���������
	 */
	public String getResult();
	/**
	 * ֵ���Ǳ�������ط���
	 * @return
	 */
	public ValueStack getValueStack();
	/**
	 * �����������Ķ���
	 * @return
	 */
	public void invoke();
	/**
	 * ȡ����������,http����
	 * @return
	 */
	public ActionStatistics getActionStatistics();
	/**
	 * ��ȡ����ķ���
	 * @return
	 */
	public String getRequestMethod();
	/**
	 * ��ȡ�������
	 * @return
	 */
	public Object getRequestObject(); 
	/**
	 * ��ȡxml������Ϣ���ɵ����ݽṹ
	 * @return
	 */
	public ResourceStatistic getConfiguration();
	
}
