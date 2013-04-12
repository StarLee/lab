package com.develop.data.base.mapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 处理sql语句的接口，在beanMapper中的sql只是原生的那种<br/>
 * @author starlee
 *
 */
public interface PreSql
{
	/**
	 *已经是处理过的语句，最终要去执行的SQL语句，根据不同的解释器
	 * @return
	 */
	String getPureSQL();
	/**
	 * 获取sql语句。将值设计成了一个sql语句,对原生SQL进行分析完了之后得到的SQL<br/>
	 * "select *@Ds_basic from Ds_basic where ygbh='1141' and abc='#{dfdfdf}'"-><br/>
	 * select *@Ds_basic from Ds_basic where ygbh='1141' and abc='__dfdfdf_temp_1__'-><br/>
	 * select id,name,cname from Ds_basic where ygbh='1141' and abc='1'-><br/>
	 * @return
	 */
	public String getFullSql();
	/**
	 * 在翻译原生的Sql语句后保存别名关系
	 * @return
	 */
	Map<String,BeanAsName> getAsMapping();
	/**
	 * 获取参数信息<br/>
	 * @return
	 */
	List<BeanParam> getParams();
	/**
	 * 原生SQL中包含的字段信息<br/>
	 * 对于select id@A,name@A from @A,key为A，List中的值为id,name,以及__(这个是默认加入的，标识不与类的属性对应)<br/>
	 * @return
	 */
	public Map<String, List<String>> getFields();
	/**
	 * 设计解释器
	 * @param interprete<br/>
	 */
	public void setSQLInterprete(SQLInterprete interprete);
	
	/**
	 * 
	 * @return
	 */
	public TableMap getTableMap();
	/**
	 * 处理，首先调用这个<br/>
	 * @param sql 原生的SQL<br/>
	 * @param paramValues 参数列表<br/>
	 * @param parameterType 参数类型<br/>
	 * @param method 调用方法<br/>
	 */
	void parseSQL(String sql,Object[] paramValues,String parameterType,Method method);
}
