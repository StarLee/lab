package com.develop.data.base;

import java.sql.Connection;

/**
 * ����Session����
 * 
 * @author starlee
 * 
 */
public interface SessionFactory
{
	/**
	 * ����һ��session
	 * 
	 * @return
	 */
	Session openSession();
	/**
	 * �����ṩ��
	 * @return
	 */
	ConnectionProvider getConnectionProvider();
}
