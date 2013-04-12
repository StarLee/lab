package com.develop.data.base;

import java.sql.SQLException;

public class Transaction
{
	private JDBCConnection jdbcConnection;
	public Transaction(JDBCConnection jdbcConnection)
	{
		this.jdbcConnection=jdbcConnection;
	}
	/**
	 * ��ʼ����
	 * @throws SQLException
	 */
	public void begin() throws SQLException
	{
		this.jdbcConnection.getConnectionProvider().getConnection().setAutoCommit(false);
	}
	/**
	 * �ύ����
	 * @throws SQLException
	 */
	public void commit() throws SQLException
	{
		try
		{
			this.jdbcConnection.getConnectionProvider().getConnection().commit();
		}
		catch (Exception e)
		{
			//ֻҪ�ύʧ�ܾͽ�������ع�
			this.jdbcConnection.getConnectionProvider().getConnection().rollback();
		}
	}
}
