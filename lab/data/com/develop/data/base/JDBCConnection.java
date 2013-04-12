package com.develop.data.base;

import org.apache.log4j.Logger;


/**
 * �����࣬��ʵû�кܴ�ı�Ҫ
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
			log.info("��ȡ����");
		return this.connectionProvider;
	}
}
