package com.develop.data.buider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.develop.data.base.Query;
import com.develop.data.parse.BeanMapper;
import com.develop.data.parse.ResourceStatistics;
import com.develop.data.result.PageRow;

/**
 * 这实现数据库操作的类，利用的是代理
 * 
 * @author starlee
 * 
 */
public class QueryHandler implements InvocationHandler, Handler
{

	private Logger log = Logger.getLogger(this.getClass());
	private Method requestMethod;
	private Query requestQuery;
	private Class requestClass;
	private Object[] paraValues;
	private BeanMapper mapper;
	private PageRow page;
	
	public QueryHandler(Query query)
	{
		this(query,null);
	}
	
	public QueryHandler(Query query,PageRow page)
	{
		this.page=page;
		this.requestQuery=query;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		String className = proxy.getClass().getInterfaces()[0].getName();
		String methodName = method.getName();
		if (log.isInfoEnabled())
		{
			log.info("方法名:" + methodName);
			log.info("方法返回值:" + method.getReturnType());
			log.info("类名:" + className);
		}
		this.mapper = ResourceStatistics.getInstance().getMapper(
				proxy.getClass().getInterfaces()[0], method);
		this.requestMethod = method;
		this.paraValues = args;
		this.requestClass = proxy.getClass();
		String type = mapper.getOperationType();// 获取操作类型select/update/delete/insert
		SQLType sqlType = (SQLType) SQLTypeFactoryIml.create(type);
		return sqlType.execute(this);
	}

	@Override
	public Query getQuery()
	{
		return this.requestQuery;
	}

	@Override
	public Method getMethod()
	{
		return this.requestMethod;
	}

	@Override
	public Class getRequestClass()
	{
		return this.requestClass;
	}

	@Override
	public Object[] getRequestParams()
	{
		return this.paraValues;
	}

	@Override
	public BeanMapper getMapper()
	{
		return this.mapper;
	}

	@Override
	public PageRow getPageRow()
	{
		return this.page;
	}
}
