package com.develop.data.base.iml;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;

import com.develop.data.base.JDBCCallback;
import com.develop.data.base.Query;
import com.develop.data.base.Session;
import com.develop.data.base.SessionQuery;
import com.develop.data.base.Transaction;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.buider.QueryHandler;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;

/**
 * ͨ��ע��һ����������� sql���ŵ���һ�㴦������ģ��ģʽ��Ŀ�ľ��Ǹ�����Session��Ĺ�ϵ �ṩ���ϲ���Աʹ��
 * 
 * @author starlee
 * 
 */
public class DefaultQuery implements Query
{
	public Object unique(String sql, Mapping mapping) throws SQLException
	{
		List listt=list(sql, mapping);
		if(listt.isEmpty())
			return null;
		return listt.get(0);
	}

	public List list(String sql, Mapping mapping) throws SQLException
	{
		Session session = SessionUtil.createSession();
		try
		{
			SessionQuery query = session.createSessionQuery(sql);
			return query.executeQuery(mapping);
		} finally
		{
			SessionUtil.closeSession();
		}
	}

	public Object find(final String sql)
	{
		try
		{
			return execute(new JDBCCallback()
			{
				@Override
				public Object doExecute(Session session)
				{
					try
					{
						SessionQuery query = session.createSessionQuery(sql);
						Object obj = query.executeOne();
						return obj;
					} catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ķ���
	 */
	@Override
	public Object execute(JDBCCallback jdbcCallback) throws SQLException
	{
		// ���������Ļ���Ҫ��һ��ͳһ���������,������Ƿ��̰߳�ȫ(��Ȼ��Servlet�����̰߳�ȫ��)�ģ�������java
		// web��servlet���Ƿ��ǰ�ȫ���أ�
		// ֱ�����ÿ϶������̰߳�ȫ�ģ��������ʣ���
		Session session = SessionUtil.createSession();// ȡ�����ݿ�����
		try
		{
			Object result = jdbcCallback.doExecute(session);
			return result;
		} finally
		{
			SessionUtil.closeSession();// �رմ˴λỰ
		}
	}

	@Override
	public List list(final String sql)
	{
		try
		{
			return (List) execute(new JDBCCallback()
			{

				@Override
				public Object doExecute(Session session)
				{
					SessionQuery query;
					try
					{
						query = session.createSessionQuery(sql);
						return query.executeQuery();
					} catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}
			});
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Object getMapper(Class className)
	{
		return getMapper(className, null);
	}

	@Override
	public void update(final String string)
	{
		executeUpate(string);
	}

	@Override
	public void insert(final String string)
	{
		executeUpate(string);

	}

	public void delete(final String string)
	{
		executeUpate(string);
	}

	private void executeUpate(final String string)
	{
		try
		{
			execute(new JDBCCallback()
			{

				@Override
				public Object doExecute(Session session)
				{
					try
					{
						SessionQuery query = session.createSessionQuery(string);
						query.executeUpdate();
						Transaction ts = session.createTransaction();
						ts.begin();
						ts.commit();
					} catch (Exception e)
					{
						session.rollback();
						e.printStackTrace();
					} finally
					{
						try
						{
							session.close();
						} catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
					return null;
				}
			});
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public PageResult list(final String sql, final Mapping mapping,
			final PageRow pageRow)
	{
		try
		{
			return (PageResult) execute(new JDBCCallback()
			{

				@Override
				public Object doExecute(Session session)
				{
					try
					{
						SessionQuery query = session.createSessionQuery(sql);
						PageResult result = query.executePage(mapping, pageRow);
						return result;
					} catch (Exception e)
					{
						throw new RuntimeException(e.getMessage());
					} finally
					{
						SessionUtil.closeSession();
					}
				}
			});
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Object getMapper(Class className, PageRow pageRow)
	{
		QueryHandler handler = new QueryHandler(this,pageRow);
		return Proxy.newProxyInstance(className.getClassLoader(), new Class[]
		{ className }, handler);
	}
	/**
	 * �򵥵Ĳ���һ�·������ݿ��Ƿ�����
	 * @param args
	 */
	public static void main(String[] args)
	{
		DefaultQuery query = new DefaultQuery();
		Object[] object = (Object[]) query
				.find("select count(*) from Ds_college");
		System.out.println(object[0]);
		// ��һ�������
		System.out
				.println(((Object[]) query.find("select ygbh from DS_basic"))[0]);
	}
}
