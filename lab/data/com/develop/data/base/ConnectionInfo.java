package com.develop.data.base;
/**
 * �������ݿ��һЩ������Ϣ��
 * @author starlee
 *
 * 2011-7-11
 */
public class ConnectionInfo
{
	private String alias="dataConnect";//Ϊproxool���µĵ����ӱ���
	private String driver;//������
	private String url;//���ӵ�ַ
	private String user;//�û�
	private String password;//����
	private String maxPool;//������ӳص�������
	public String getDriver()
	{
		return driver;
	}
	public void setDriver(String driver)
	{
		this.driver = driver;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getMaxPool()
	{
		return maxPool;
	}
	public void setMaxPool(String maxPool)
	{
		this.maxPool = maxPool;
	}
	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	
}
