package com.develop.data.base;

import java.sql.*;

import org.apache.log4j.Logger;

import com.develop.data.base.iml.DefaultSessionFactory;
import com.develop.data.parse.DataResource;
import com.develop.data.parse.ResourceStatistics;

/**
 * 获取连接，以及各种配置资源等
 * 
 * @author starlee
 * 
 */
public class ConnectionBase implements ConnectionProvider
{

	private ConnectionInfo configuration;
	private static Logger logger = Logger.getLogger(ConnectionBase.class);
	private Connection connection;

	public synchronized void generateConnection()
	{
		Connection connection = null;
		// ConnectionInfo
		// info=ResourceStatistics.getInstance().getConnectionInfo();
		try
		{
			logger.info("生产连接……");
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			String str = "proxool." + configuration.getAlias() + ":"
					+ configuration.getDriver() + ":" + configuration.getUrl();
			connection = DriverManager.getConnection(str,
					configuration.getUser(), configuration.getPassword());
			connection.setAutoCommit(true);// 自动提交
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		this.connection = connection;
	}

	public synchronized Connection getConnection()
	{
		try
		{
			if (this.connection == null||this.connection.isClosed())
				generateConnection();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.connection;

	}

	public void configure(ConnectionInfo configuration)
	{
		this.configuration = configuration;
	}
}
