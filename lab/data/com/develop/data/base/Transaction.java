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
	 * 开始事务
	 * @throws SQLException
	 */
	public void begin() throws SQLException
	{
		this.jdbcConnection.getConnectionProvider().getConnection().setAutoCommit(false);
	}
	/**
	 * 提交事务
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
			//只要提交失败就进行事务回滚
			this.jdbcConnection.getConnectionProvider().getConnection().rollback();
		}
	}
}
