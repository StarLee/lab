package com.develop.data.base.mapping;
/**
 * ��Ҫ�Ǹ����ṩ��sql�ռ���������Ϣ
 * ����(#{name})
 * @author starlee
 *
 */
public class BeanParam
{
	public final static String INT="int";
	public final static String STRING="string";
	
	private int order;//λ����Ϣ��Լ��������λ�ã�
	private String type;//����
	private String name;//���Ʊ���(#{name})��������Ϊname
	private Object value;//ֵ
	private String newName;//��Ҫ�滻��(�滻�ı�ʶ)
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