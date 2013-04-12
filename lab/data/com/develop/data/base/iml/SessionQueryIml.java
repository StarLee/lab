package com.develop.data.base.iml;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.develop.data.base.JDBCBatcher;
import com.develop.data.base.Session;
import com.develop.data.base.SessionQuery;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;

/**
 * 实现的sql的基本功能
 * 
 * @author starlee
 * 
 */
public class SessionQueryIml implements SessionQuery
{
	private JDBCBatcher batcher;
	private JDBCContext context;
	private Logger log = Logger.getLogger(this.getClass());
	private int maxAmount = 0;// 查询
	private int firstRow = 0;// 查询
	private PageRow pageRow;

	public SessionQueryIml(JDBCContext context, JDBCBatcher batcher)
	{
		this.batcher = batcher;
		this.context = context;
	}

	@Override
	public List executeQuery() throws SQLException
	{
		return execute();
	}

	@Override
	public void executeUpdate() throws SQLException
	{
		Statement psm = batcher.preparedSQLStatement(context);
		if (log.isInfoEnabled())
		{
			log.info(context.getSql());
		}
		psm.executeUpdate(context.getSql());
		
	}

	@Override
	public Object executeOne() throws SQLException
	{
		return execute().get(0);
	}

	/**
	 * 执行查询,这一层要往上移动，在这个地方会出问题 是一个Object数组
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List execute() throws SQLException
	{

		Mapping mapping = new Mapping()
		{
			@Override
			public List getResult(ResultSet rs)
			{
				try
				{
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();
					List<Object> list = new ArrayList<Object>();
					int n = 0;

					while (rs.next())
					{
						Object[] object = new Object[columnCount];
						for (int i = 1; i <= columnCount; i++)
						{
							object[i - 1] = rs.getObject(i);
						}
						list.add(object);
					}
					return list;
				} catch (Exception e)
				{
					e.printStackTrace();
					throw new RuntimeException("查询出错" + e.getMessage());
				}
			}
		};
		return executeQuery(mapping);

	}

	@Override
	public List executeQuery(Mapping mapping) throws SQLException
	{
		Statement state = null;
		ResultSet rs = null;
		try
		{
			state = createStatement();

			return doQuery(mapping, state);
		} finally
		{
			closeStatement(state);
		}
	}

	public PageResult executePage(Mapping mapping, PageRow pageRow)
			throws SQLException
	{
		
		try
		{
			int pageSize = pageRow.getPageSize();// 页面容量
			int pageOrder = pageRow.getPageOrder();// 页码
			int rows = rowsNum(context.getSql());
			int pages = (rows + pageSize - 1) / pageSize;
			if (pageOrder > pages)
			{
				pageOrder = pages;
			}
			int firstRow = (pageOrder - 1) * pageSize;
			if (firstRow < 0)
				firstRow = 1;
			
			this.firstRow = firstRow;
			this.maxAmount = pageSize;
			
			PageResult pageResult = new PageResult();
			Statement state = createStatement();
			pageResult.addAll(doQuery(mapping, state));
			
			pageResult.setCurrent(pageOrder);
			
			pageResult.setPageSize(pageSize);
			pageResult.setRows(rows);
			pageResult.setPages(pages);
			return pageResult;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("出错啦" + e.getMessage());
		} 
	}
	/**
	 * 这个方法可以简化，直接用rs.first(),rs.last(),传参数当然就rs了
	 * @param sqlCount
	 * @return
	 * @throws SQLException
	 */
	private int rowsNum(String sqlCount) throws SQLException
	{
		String[] sqls = sqlCount.split(" ");// 以空格分隔
		boolean start = false;
		boolean end = false;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sqls.length; i++)
		{
			String sql = sqls[i];
			if ("select".equals(sql.trim()) && !start)
			{
				builder.append("select count(*) ");
				start = true;
			}
			if ("from".equals(sql) && !end)
			{
				end = true;
			}
			if (start && end)
			{
				builder.append(sql);
				builder.append(" ");
			}
		}

		Object[] object = (Object[]) new SessionQueryIml(
				this.context,this.batcher)
				.executeOne();
		int rows=0;
		if(object[0].getClass().isAssignableFrom(java.lang.Long.class))
			rows= ((Long) object[0]).intValue();
		else if(object[0].getClass().isAssignableFrom(java.lang.Integer.class))
		{
			rows=(Integer)object[0];
		}
			return rows;
	}

	private List doQuery(Mapping mapping, Statement statement)
	{
		ResultSet rs=null;
		try
		{
			statement.setMaxRows(firstRow+maxAmount);
			rs = createResultSet(statement);
			if (!(firstRow <= 0))
			{
				rs.absolute(firstRow);
			}
			if(log.isInfoEnabled())
				log.info("映射成对象");
			List list = mapping.getResult(rs);
			return list;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
			try
			{
				closeResultSet(rs);
				closeStatement(statement);
			} catch (SQLException e)
			{
				
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	@Override
	public JDBCContext getJDBCContext()
	{
		return this.context;
	}

	@Override
	public void setMaxAmount(int size)
	{
		this.maxAmount = size;
	}

	@Override
	public void setFirstRow(int first)
	{
		this.firstRow = first;
	}


	private Statement createStatement() throws SQLException
	{
		Statement state;
		state = batcher.preparedSQLStatement(context);
		return state;
	}

	private ResultSet createResultSet(Statement statment) throws SQLException
	{
		ResultSet rs;
		
		rs = batcher.getResultSet(statment, context.getSql());
		return rs;
	}

	private void closeStatement(Statement statement) throws SQLException
	{
		batcher.closeStatement(statement);
	}

	private void closeResultSet(ResultSet resultSet) throws SQLException
	{
		batcher.closeResultSet(resultSet);
	}
}
