package com.develop.data.base.iml;

import java.sql.Connection;

import com.develop.data.base.ConnectionBase;
import com.develop.data.base.ConnectionProvider;
import com.develop.data.base.Session;
import com.develop.data.base.SessionFactory;
/**
 * 提供Connection连接
 * @author starlee
 *
 */
public class DefaultSessionFactory implements SessionFactory
{
	private ConnectionProvider base;
	
	public DefaultSessionFactory(ConnectionProvider base)
	{
		this.base=base;
	}
	
	@Override
	public  Session openSession()
	{
		Session session = new DefaultSession(this);
		return session;
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return base;
	}
}
