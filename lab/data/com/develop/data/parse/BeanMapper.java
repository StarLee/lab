package com.develop.data.parse;
/**
 * ӳ�����ݿ������һ��������
 * @author starlee
 *
 */
public class BeanMapper
{
	private String method;//�����Ӧ����id
	private String returnType;//�����Ӧ���Ƿ���ֵ����
	private String operationType;//�����Ӧ��Ӧ����select,update,insert,delete
	private String sql;//sql���
	private String parameterType;//��������
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
