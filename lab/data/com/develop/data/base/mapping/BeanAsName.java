package com.develop.data.base.mapping;
/**
 * ��Ҫ���ڲ���ʱ�õ���ָ������select name as name_temp��name_temp�����������ֶ�����ӳ���ϵ
 * @author StarLee
 *
 */
public class BeanAsName
{
	private String asName;//sql�еı�������Ҳ��key��
	private String cls;//��
	private String clsProperty;//���Ա��
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
