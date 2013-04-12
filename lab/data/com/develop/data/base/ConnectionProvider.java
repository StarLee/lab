package com.develop.data.base;

import java.sql.Connection;

/**
 * 这是作为一个策略
 * 
 * @author starlee
 * 
 */
public interface ConnectionProvider
{
	/**
	 * 配置信息
	 * @param configuration
	 */
	public void configure(ConnectionInfo configuration);
	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConnection();
}
