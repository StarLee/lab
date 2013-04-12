package com.develop.data.base;

import java.sql.Connection;

/**
 * 生产Session的类
 * 
 * @author starlee
 * 
 */
public interface SessionFactory
{
	/**
	 * 开放一个session
	 * 
	 * @return
	 */
	Session openSession();
	/**
	 * 连接提供者
	 * @return
	 */
	ConnectionProvider getConnectionProvider();
}
