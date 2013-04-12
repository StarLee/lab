package com.develop.data.base.iml;

import com.develop.data.base.ConnectionBase;
import com.develop.data.base.ConnectionInfo;
import com.develop.data.base.ConnectionProvider;
import com.develop.data.base.ConnectionProviderFactory;

/**
 * 工厂类，产生ConnectionProvider,这个是最基本的，加了proxool,这个工厂现在还没有选择性，以后重构应该加上，根据不同的需求生成不同的对象
 * @author starlee
 *
 */
public class ConnectionProviderFactoryIml implements ConnectionProviderFactory
{
	public ConnectionProvider createConnectionProvider(ConnectionInfo info)
	{
		ConnectionBase base=new ConnectionBase();
		base.configure(info);
		return base;
	}
}