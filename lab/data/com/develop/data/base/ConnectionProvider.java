package com.develop.data.base;

import java.sql.Connection;

/**
 * ������Ϊһ������
 * 
 * @author starlee
 * 
 */
public interface ConnectionProvider
{
	/**
	 * ������Ϣ
	 * @param configuration
	 */
	public void configure(ConnectionInfo configuration);
	/**
	 * ��ȡ����
	 * @return
	 */
	public Connection getConnection();
}
