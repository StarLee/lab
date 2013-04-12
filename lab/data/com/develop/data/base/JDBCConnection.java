package com.develop.data.base;

import org.apache.log4j.Logger;


/**
 * 抽像类，其实没有很大的必要
 * @author starlee
 *
 */
public abstract class JDBCConnection implements Session
{
	private Logger log=Logger.getLogger(this.getClass());
	private ConnectionProvider connectionProvider; 
	public JDBCConnection(SessionFactory factory)
	{
		this.connectionProvider=factory.getConnectionProvider();
	}
	public ConnectionProvider getConnectionProvider()
	{
		if(log.isInfoEnabled())
			log.info("获取连接");
		return this.connectionProvider;
	}
}
