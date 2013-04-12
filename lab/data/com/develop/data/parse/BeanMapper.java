package com.develop.data.parse;
/**
 * 映射数据库操作的一个保存类
 * @author starlee
 *
 */
public class BeanMapper
{
	private String method;//这个对应的是id
	private String returnType;//这个对应的是返回值类型
	private String operationType;//这个对应对应的是select,update,insert,delete
	private String sql;//sql语句
	private String parameterType;//参数类型
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getReturnType()
	{
		return returnType;
	}
	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}
	public String getOperationType()
	{
		return operationType;
	}
	public void setOperationType(String operationType)
	{
		this.operationType = operationType;
	}
	public String getSql()
	{
		return sql;
	}
	public void setSql(String sql)
	{
		this.sql = sql;
	}
	public String getParameterType()
	{
		return parameterType;
	}
	public void setParameterType(String parameterType)
	{
		this.parameterType = parameterType;
	}
}
