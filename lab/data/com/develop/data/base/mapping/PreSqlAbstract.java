package com.develop.data.base.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��Ϊģ�巽����(ʲôģ�巽������ȫ�Ƕ���)
 * ����Ĵ�����������
 * @author starlee
 *
 */
public abstract class PreSqlAbstract implements PreSql
{
	private String sql;
	private String fullSql;//������sql,��ԭ��SQL���з�������֮��õ���SQL,����ռλ��
	private List<BeanParam> map=new ArrayList<BeanParam>();
	private int variableOrder=0;//�ܹ��ж��ٸ�����
	private TableMap table;
	private Map<String,List<String>> fields=new HashMap<String,List<String>>();//����select id@A,name@A from @A,keyΪA��List�е�ֵΪid,name,�Լ�__(�����Ĭ�ϼ���ģ���ʶ����������Զ�Ӧ)
	private Map<String,BeanAsName> asMapping=new HashMap<String,BeanAsName>();
	
	public Map<String, BeanAsName> getAsMapping()
	{
		return asMapping;
	}

	public PreSqlAbstract()
	{
		ClassInfo info = new ClassInfo();
		info.setClsName("cuc.lab.TestA");
		info.setTableName("abc");
		Map<String, String> fieldsMap = new HashMap<String, String>();
		fieldsMap.put("id", "id");
		fieldsMap.put("college_name", "cname");
		fieldsMap.put("name", "name");
		info.setFields(fieldsMap);
		
		
		ClassInfo info2 = new ClassInfo();
		info2.setClsName("com.develop.data.base.mapping.Ds_collegeBean");
		info2.setTableName("Ds_college");
		Map<String, String> fieldsMap2 = new HashMap<String, String>();
		fieldsMap2.put("id", "id");
		fieldsMap2.put("collegeName", "college_name");
		fieldsMap2.put("flag", "flag");
		info2.setFields(fieldsMap2);
		
		
		table=new TableMap();
		table.put("Ds_basic", info);
		table.put("Ds_collegeBean", info2);
	}
	
	
	public Map<String, List<String>> getFields()//�����ֶ�
	{
		return fields;
	}

	public void setFields(Map<String, List<String>> fields)
	{
		this.fields = fields;
	}

	public int getVariableOrder() {
		return variableOrder;
	}

	public void setVariableOrder(int variableOrder) {
		this.variableOrder = variableOrder;
	}

	@Override
	public String getPureSQL()
	{
		return sql;
	}
	
	public void setSql(String sql)
	{
		this.sql=sql;
	}
	
	@Override
	public TableMap getTableMap()
	{
		return this.table;
	}

	@Override
	public List<BeanParam> getParams()
	{
		return map;
	}
	public String getFullSql()
	{
		return fullSql;
	}

	public void setFullSql(String fullSql)
	{
		this.fullSql = fullSql;
	}
}
