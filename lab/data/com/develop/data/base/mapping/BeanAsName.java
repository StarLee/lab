package com.develop.data.base.mapping;
/**
 * 主要是在查找时用到，指明比如select name as name_temp中name_temp与真正对象字段名的映射关系
 * @author StarLee
 *
 */
public class BeanAsName
{
	private String asName;//sql中的别名（它也是key）
	private String cls;//类
	private String clsProperty;//类成员名
	public String getAsName()
	{
		return asName;
	}
	public void setAsName(String asName)
	{
		this.asName = asName;
	}
	public String getCls()
	{
		return cls;
	}
	public void setCls(String cls)
	{
		this.cls = cls;
	}
	public String getClsProperty()
	{
		return clsProperty;
	}
	public void setClsProperty(String clsProperty)
	{
		this.clsProperty = clsProperty;
	}
	
}
