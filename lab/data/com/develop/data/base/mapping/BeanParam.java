package com.develop.data.base.mapping;
/**
 * 主要是根据提供的sql收集变量的信息
 * 比如(#{name})
 * @author starlee
 *
 */
public class BeanParam
{
	public final static String INT="int";
	public final static String STRING="string";
	
	private int order;//位置信息（约定参数的位置）
	private String type;//类型
	private String name;//名称比如(#{name})，则名字为name
	private Object value;//值
	private String newName;//需要替换的(替换的标识)
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Object getValue()
	{
		return value;
	}
	public void setValue(Object value)
	{
		this.value = value;
	}
	public int getOrder()
	{
		return order;
	}
	public void setOrder(int order)
	{
		this.order = order;
	}
}