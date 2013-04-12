package com.develop.data.base.iml;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.develop.data.base.ConnectionProvider;
import com.develop.data.base.Session;

/**
 * 保存当前使用的连接，resultSet，Preparedment
 * 
 * @author starlee
 * 
 */
public class JDBCContext
{
	private ConnectionProvider provider;
	private Session session;
	private String sql;
	private boolean prepared;
	public JDBCContext(Session session,String sql,boolean prepared){
		this.session=session;
		this.sql=sql;
		this.prepared=prepared;
		provider=session.getConnectionProvider();
		
	};

	public String getSql()
	{
		return sql;
	}

	public ConnectionProvider getProvider()
	{
		return provider;
	}
	/**
	 * 
	 * @param prepared，true返回的是PreparedStatement,false则为statement
	 * @return
	 */
	public Statement getPsm()
	{
		try
		{
			if (prepared)

				return provider.getConnection().prepareStatement(sql);

			else
				return provider.getConnection().createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
}
