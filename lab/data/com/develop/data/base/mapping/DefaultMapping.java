package com.develop.data.base.mapping;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 默认就以bean对象来实现类型转换，只要字段与所在类型一致即行 当然这个是可以设置的
 * 
 * @author starlee
 * 
 */
public class DefaultMapping implements Mapping
{
	// private Ruler ruler;

	/*
	 * public DefaultMapping(SessionQuery sessionQuery) { //this.ruler=ruler;
	 * 
	 * }
	 */
	private Object targetObject;
	private PreSql preSql;

	public DefaultMapping(Object targetObject, PreSql preSql)
	{
		this.targetObject = targetObject;
		this.preSql = preSql;
	}

	@Override
	public List getResult(ResultSet rs)
	{
		try
		{
			ResultSetMetaData metaData = rs.getMetaData();
			int columnSize = metaData.getColumnCount();
			List list = new ArrayList();
			Map<String, Object> objMap = new HashMap<String, Object>();
			while (rs.next())
			{
				/**
				 * 规则在这个地方取得
				 */
				if (targetObject == null)// 即没有配置文件，
				{
					Set<String> keySet = preSql.getFields().keySet();
					for (String key : keySet)
					{
						try
						{
							String cls = preSql.getTableMap().get(key)
									.getClsName();
							objMap.put(cls, Class.forName(cls).newInstance());
						} catch (ClassNotFoundException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else
				// 有返回参数配置
				{
					Object obj = targetObject.getClass().newInstance();// 在while循环中创建一个新的对象
					objMap.put(targetObject.getClass().getName(), obj);
				}
				if (objMap.size() == 1)//始终只有一个
				{
					Object object = objMap.values().toArray()[0];
					for (int i = 1; i <= columnSize; i++)
					{
						String columnName = metaData.getColumnLabel(i);

						if (preSql.getAsMapping().containsKey(columnName))
						{
							columnName = preSql.getAsMapping().get(columnName)
									.getClsProperty();
						}
						Class classs = PropertyUtils.getPropertyType(object,
								columnName);
						if (classs != null)
						{
							if (classs.isPrimitive())
							{
								if (classs == Integer.TYPE)
									PropertyUtils.setSimpleProperty(object,
											columnName, rs.getInt(i));
							} else
								PropertyUtils.setSimpleProperty(object,
										columnName, rs.getString(i));
						}
					}
					list.add(object);
				}
				else{
					throw new RuntimeException("对于映射多个类，请实现");
				}
			}
			return list;
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
