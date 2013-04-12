package com.develop.data.base.iml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.develop.data.base.JDBCBatcher;
import com.develop.data.base.JDBCConnection;
import com.develop.data.base.Session;
import com.develop.data.base.SessionFactory;
import com.develop.data.base.SessionQuery;
import com.develop.data.base.Transaction;
import com.develop.data.parse.ResourceStatistics;

/**
 * 默认的session类，事物就是在这里进行的，所以这里面就一个问题就是
 * 对于PreparedStatement这些本来应该还要往其它类转移，而不是在这个地方。导致此类的作用变得
 * 下放时注意将sessionFactory传过去，因为连接在这里。本人就不做设计了。
 * 
 * @author starlee
 * 
 */
public class DefaultSession extends JDBCConnection implements JDBCBatcher
{
	private Logger log = Logger.getLogger(this.getClass());
	private Set statement = (Set) Collections.synchronizedSet(new HashSet());
	private Set resultSet = (Set) Collections.synchronizedSet(new HashSet());

	public DefaultSession(SessionFactory sessionFactory)
	{
		super(sessionFactory);// 保证连接是保存在JDBCConnection中的
	}

	@Override
	public void close() throws SQLException
	{
		if (log.isInfoEnabled())
			log.info("尝试关闭数据库连接");
		Connection connection = getConnectionProvider().getConnection();
		connection.close();
		if (log.isInfoEnabled())
			log.info("关闭成功");
	}

	@Override
	public Transaction createTransaction()
	{
		return new Transaction(this);
	}

	@Override
	public SessionQuery createSessionQuery(String string) throws SQLException
	{
		JDBCContext context=new JDBCContext(this, string,false);
		return new SessionQueryIml(context, this);
	}

	@Override
	public Statement preparedSQLStatement(JDBCContext context)
			throws SQLException
	{
		Statement st = context.getPsm();
		this.statement.add(st);
		return st;
	}

	@Override
	public ResultSet getResultSet(Statement preparedStatement, String hql)
			throws SQLException
	{
		ResultSet rs = preparedStatement.executeQuery(hql);
		// 处理rs，查询的首位置
		this.resultSet.add(rs);
		return rs;
	}

	@Override
	public void closeStatement(Statement statement) throws SQLException
	{

		if (statement != null)
		{
			if (this.statement.remove(statement))
			{
				log.info("关闭对应prepareStatement");
				statement.close();
			}
		}
	}

	@Override
	public void closeResultSet(ResultSet rs) throws SQLException
	{

		if (rs != null)
		{
			if (this.resultSet.remove(rs))
			{
				log.info("关闭ResultSet");
				rs.close();
			}
		}

	}

	@Override
	public void rollback()
	{
		try
		{
			log.info("数据rollback");
			getConnectionProvider().getConnection().rollback();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException
	{
		SessionFactory factory = ResourceStatistics.getInstance()
				.buildSessionFactory();
		Session session = factory.openSession();
		SessionQuery query = session
				.createSessionQuery("select count(*) from Ds_basic");
		// Transaction tranaction = session.createTransaction();
		System.out.println(query.executeOne());
		// tranaction.begin();
		// tranaction.commit();
		session.close();
	}

}
