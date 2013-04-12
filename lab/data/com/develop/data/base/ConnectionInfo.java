package com.develop.data.base;
/**
 * 连接数据库的一些配置信息类
 * @author starlee
 *
 * 2011-7-11
 */
public class ConnectionInfo
{
	private String alias="dataConnect";//为proxool留下的的连接别名
	private String driver;//驱动器
	private String url;//连接地址
	private String user;//用户
	private String password;//密码
	private String maxPool;//最大连接池的连接数
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
