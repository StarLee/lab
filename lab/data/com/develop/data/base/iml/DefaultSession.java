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
 * Ĭ�ϵ�session�࣬���������������еģ������������һ���������
 * ����PreparedStatement��Щ����Ӧ�û�Ҫ��������ת�ƣ�������������ط������´�������ñ��
 * �·�ʱע�⽫sessionFactory����ȥ����Ϊ������������˾Ͳ�������ˡ�
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
		super(sessionFactory);// ��֤�����Ǳ�����JDBCConnection�е�
	}

	@Override
	public void close() throws SQLException
	{
		if (log.isInfoEnabled())
			log.info("���Թر����ݿ�����");
		Connection connection = getConnectionProvider().getConnection();
		connection.close();
		if (log.isInfoEnabled())
			log.info("�رճɹ�");
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
		// ����rs����ѯ����λ��
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
				log.info("�رն�ӦprepareStatement");
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
				log.info("�ر�ResultSet");
				rs.close();
			}
		}

	}

	@Override
	public void rollback()
	{
		try
		{
			log.info("����rollback");
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
