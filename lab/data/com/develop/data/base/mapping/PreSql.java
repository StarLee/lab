package com.develop.data.base.mapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * ����sql���Ľӿڣ���beanMapper�е�sqlֻ��ԭ��������<br/>
 * @author starlee
 *
 */
public interface PreSql
{
	/**
	 *�Ѿ��Ǵ��������䣬����Ҫȥִ�е�SQL��䣬���ݲ�ͬ�Ľ�����
	 * @return
	 */
	String getPureSQL();
	/**
	 * ��ȡsql��䡣��ֵ��Ƴ���һ��sql���,��ԭ��SQL���з�������֮��õ���SQL<br/>
	 * "select *@Ds_basic from Ds_basic where ygbh='1141' and abc='#{dfdfdf}'"-><br/>
	 * select *@Ds_basic from Ds_basic where ygbh='1141' and abc='__dfdfdf_temp_1__'-><br/>
	 * select id,name,cname from Ds_basic where ygbh='1141' and abc='1'-><br/>
	 * @return
	 */
	public String getFullSql();
	/**
	 * �ڷ���ԭ����Sql���󱣴������ϵ
	 * @return
	 */
	Map<String,BeanAsName> getAsMapping();
	/**
	 * ��ȡ������Ϣ<br/>
	 * @return
	 */
	List<BeanParam> getParams();
	/**
	 * ԭ��SQL�а������ֶ���Ϣ<br/>
	 * ����select id@A,name@A from @A,keyΪA��List�е�ֵΪid,name,�Լ�__(�����Ĭ�ϼ���ģ���ʶ����������Զ�Ӧ)<br/>
	 * @return
	 */
	public Map<String, List<String>> getFields();
	/**
	 * ��ƽ�����
	 * @param interprete<br/>
	 */
	public void setSQLInterprete(SQLInterprete interprete);
	
	/**
	 * 
	 * @return
	 */
	public TableMap getTableMap();
	/**
	 * �������ȵ������<br/>
	 * @param sql ԭ����SQL<br/>
	 * @param paramValues �����б�<br/>
	 * @param parameterType ��������<br/>
	 * @param method ���÷���<br/>
	 */
	void parseSQL(String sql,Object[] paramValues,String parameterType,Method method);
}
