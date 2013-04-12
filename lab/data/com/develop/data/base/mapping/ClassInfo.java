package com.develop.data.base.mapping;

import java.util.Map;

/**
 * 类与实际表的映射关系
 * @author StarLee
 *
 */
public class ClassInfo
{
	private String clsName;
	private String tableName;
	private Map<String, String> fields;

	public String getClsName()
	{
		return clsName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public void setClsName(String clsName)
	{
		this.clsName = clsName;
	}

	public Map<String, String> getFields()
	{
		return fields;
	}

	public void setFields(Map<String, String> fields)
	{
		this.fields = fields;
	}
}