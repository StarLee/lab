package com.develop.data.base;
/**
 * �����࣬����ConnectionProvider
 * @author starlee
 *
 */
public interface ConnectionProviderFactory
{
	public ConnectionProvider createConnectionProvider(ConnectionInfo info);
}
