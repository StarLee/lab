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
 * ��ȡ���ݿ���ص�������Դ,�ǵ���ģʽ��
 * ����Map�ķ��ͣ��������������Ͳ���飬�����޷�֪�����浽���Ƿŵ�ʲô���������ǿ�Դ���룩��
 * ��������ǿ��Խ���ģ���Think in java��һ�ְ취����û���ã�ϣ�������������
 * @author starlee
 * 
 */
public class ResourceStatistics
{
	private ConnectionInfo connectionInfo;// ����
	private Map tables;// ��
	private Map mapper;// sql���ӳ��
	private static ResourceStatistics resource = new ResourceStatistics();

	private ResourceStatistics()
	{

	}

	static
	{
		/**
		 * ���������ļ������ǵ�һ��
		 */
		DataResource data = new DataResource();
		data.parseData();
	}

	/**
	 * ���������ļ�����SessionFactory
	 * 
	 * @return
	 */
	public SessionFactory buildSessionFactory()
	{
		/*
		 * ����������������ṩ�����޸�����ط���ʵ���� ConnectionProviderFactoryIml
		 * ����Ϊ�˽��������õİ취�����һ��Settings�࣬�������������ӡ�
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
	 * ��һ����ʵ���ǰ������ڶ����ǲ�������
	 * 
	 * @param namespace
	 *            ����
	 * @param id
	 *            ��������
	 * @return
	 */
	public BeanMapper getMapper(String namespace, String id)
	{
		try
		{
		return (BeanMapper) ((Map) mapper.get(namespace)).get(id);
		}
		catch (Exception e) {
			throw new RuntimeException("��"+namespace+"û��ָ��name"+id);
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
	 *            ����
	 * @param method
	 *            ������ķ���
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
			throw new RuntimeException("δ�����κ�����Ľӿ���ص������ļ�����ע�⣬������ִ���κ����ݿ����");
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
