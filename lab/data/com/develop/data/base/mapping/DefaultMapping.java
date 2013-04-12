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
 * Ĭ�Ͼ���bean������ʵ������ת����ֻҪ�ֶ�����������һ�¼��� ��Ȼ����ǿ������õ�
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
				 * ����������ط�ȡ��
				 */
				if (targetObject == null)// ��û�������ļ���
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
				// �з��ز�������
				{
					Object obj = targetObject.getClass().newInstance();// ��whileѭ���д���һ���µĶ���
					objMap.put(targetObject.getClass().getName(), obj);
				}
				if (objMap.size() == 1)//ʼ��ֻ��һ��
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
					throw new RuntimeException("����ӳ�����࣬��ʵ��");
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
