package com.develop.data.buider;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.develop.data.base.Query;
import com.develop.data.base.mapping.DefaultMapping;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.base.mapping.PreSql;
import com.develop.data.result.PageResult;

/**
 * 
 * @author starlee
 * 
 */
public class SQLTypeSelect extends AbstractSQLType
{

	private Logger log=Logger.getLogger(SQLTypeSelect.class);
	@Override
	public Object doExecute(Query query)
	{
		PreSql parseSql=getPreSql();
		String sql=parseSql.getPureSQL();
		if(log.isDebugEnabled())
		{
			log.debug("执行："+sql);
		}
		try
		{
			Mapping mapping=null;
			if(getReturnType()==null)
			{
				Map<String, List<String>> map=parseSql.getFields();
				if(map.isEmpty())
					throw new RuntimeException("请指定属性resultType或者采用@ ");
				 mapping= new DefaultMapping(null,parseSql);
			}
			else
			{
				mapping= new DefaultMapping(Class.forName(getReturnType()).newInstance(),parseSql);
			}
			if (List.class.isAssignableFrom(getReturnTypeClass()))
			{
				return query.list(sql, mapping);

			} else if (PageResult.class.isAssignableFrom(getReturnTypeClass()))
			{
				return query.list(sql, mapping,getPageRow());
			} 
			else
			{
				return query.unique(sql, mapping);
			}
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
