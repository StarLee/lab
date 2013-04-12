package com.develop.data.base;
/**
 * 工厂类，产生ConnectionProvider
 * @author starlee
 *
 */
public interface ConnectionProviderFactory
{
	public ConnectionProvider createConnectionProvider(ConnectionInfo info);
}
