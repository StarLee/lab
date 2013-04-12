package com.develop.data.parse;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.develop.data.annotation.SQL;
import com.develop.data.base.ConnectionInfo;
import com.develop.data.base.ConnectionProvider;
import com.develop.data.base.SessionFactory;
import com.develop.data.base.iml.ConnectionProviderFactoryIml;
import com.develop.data.base.iml.DefaultSessionFactory;

/**
 * 
 * 获取数据库相关的数据资源,是单例模式。
 * 对于Map的范型，最大的问题是类型不检查，根本无法知道里面到底是放的什么东西（除非看源代码），
 * 这个问题是可以解决的，在Think in java有一种办法，我没有用，希望后面的人用上
 * @author starlee
 * 
 */
public class ResourceStatistics
{
	private ConnectionInfo connectionInfo;// 属性
	private Map tables;// 表
	private Map mapper;// sql语句映射
	private static ResourceStatistics resource = new ResourceStatistics();

	private ResourceStatistics()
	{

	}

	static
	{
		/**
		 * 解析配置文件，这是第一步
		 */
		DataResource data = new DataResource();
		data.parseData();
	}

	/**
	 * 根据配置文件建立SessionFactory
	 * 
	 * @return
	 */
	public SessionFactory buildSessionFactory()
	{
		/*
		 * 如果有其它的连接提供，请修改这个地方的实现类 ConnectionProviderFactoryIml
		 * 本来为了解这个耦最好的办法是设计一个Settings类，让其来持有连接。
		 */
		ConnectionProvider factoryProvider = new ConnectionProviderFactoryIml()
				.createConnectionProvider(connectionInfo);
		return new DefaultSessionFactory(factoryProvider);
	}

	public static ResourceStatistics getInstance()
	{
		return resource;
	}

	/**
	 * 第一个其实就是包名，第二个是操作方法
	 * 
	 * @param namespace
	 *            包名
	 * @param id
	 *            操作方法
	 * @return
	 */
	public BeanMapper getMapper(String namespace, String id)
	{
		try
		{
		return (BeanMapper) ((Map) mapper.get(namespace)).get(id);
		}
		catch (Exception e) {
			throw new RuntimeException("在"+namespace+"没有指定name"+id);
		}
	}

	
	private Map getNamespace(String namespace)
	{
		Map map=(Map)mapper.get(namespace);
		return map;
	}

	/**
	 * 
	 * @param className
	 *            类名
	 * @param method
	 *            所在类的方法
	 * @return
	 */
	public BeanMapper getMapper(Class className, Method method)
	{
		Map map=getNamespace(className.getName());
		if(map==null)
		{
			map=new HashMap();
			BeanMapper localMapper=getAnnotationMapper(method);
			map.put(method.getName(),localMapper);
			mapper.put(className.getName(), map);
			return localMapper;
		}
		else
		{
			if(getMapper(className.getName(),method.getName())==null)
			{
				BeanMapper localMapper=getAnnotationMapper(method);
				map.put(method.getName(),localMapper);
				return localMapper;
			}
		}
		return getMapper(className.getName(), method.getName());
	}

	private BeanMapper getAnnotationMapper(Method method)
	{
		BeanMapper annotationMapper = new BeanMapper();
		SQL sql = (SQL) method.getAnnotation(SQL.class);
		if (sql == null)
			throw new RuntimeException("未发现任何与你的接口相关的配置文件或者注解，将不会执行任何数据库操作");
		annotationMapper.setMethod(method.getName());
		annotationMapper.setSql(sql.sql());
		if (!sql.paramType().equals(""))
			annotationMapper.setParameterType(sql.paramType());
		if (!sql.resultType().equals(""))
			annotationMapper.setReturnType(sql.resultType());
		switch (sql.sqlType())
		{
		case SELECT:
			annotationMapper.setOperationType("select");
			break;
		case DELETE:
			annotationMapper.setOperationType("delete");
			break;
		case UPDATE:
			annotationMapper.setOperationType("update");
			break;
		case INSERT:
			annotationMapper.setOperationType("insert");
			break;
		default:
			break;
		}
		return annotationMapper;
	}

	public void setMapper(Map mapper)
	{
		this.mapper = mapper;
	}

	public ConnectionInfo getConnectionInfo()
	{
		return connectionInfo;
	}

	public void setConnectionInfo(ConnectionInfo connectionInfo)
	{
		this.connectionInfo = connectionInfo;
	}

	public String getTables(String name)
	{
		return (String) tables.get(name);
	}

	public void setTables(Map tables)
	{
		this.tables = tables;
	}
}
