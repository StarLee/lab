package com.develop.actions.result;

/**
 * ��ƴ��������Ŀ���������е�����Result��һ���ܺõ�location���������Ҵ˱������Բ��������ļ�˵��
 * @author starlee
 *
 */
public abstract class AbstractResult implements IResult
{
	private String location;//���ʵĵ�ַ

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
	

}
