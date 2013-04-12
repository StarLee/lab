package com.develop.data.base;

import java.sql.SQLException;
import java.util.List;

import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;


/**
 * ��ѯ�����࣬��������������ļ������Զ���sql����ѯ�ģ����ṩ��һ��JDBCOperations�ӿ�<br/>
 * �˽ӿ���ͳһ�����ṩ���ݿ��������<br/>
 * ��һ���м��ࡣ���ϴ����Ǵ��ݹ����Ĳ��������������ݿ�Ĳ���
 * 
 * @author starlee
 * 
 */
public interface Query 
{
	/**
	 * 
	 * @param sql �����sql���
	 * @param mapping ӳ��ɶ���Ľӿڣ���Ҫ�Լ�ʵ�ֹ��򣬺��ڵĻ�Ԫ����ӳ�����ֱ����ע�����ʽд�뵽bean��ȥ
	 * @return
	 * @throws SQLException
	 */
	public Object unique(String sql, Mapping mapping) throws SQLException;
	/**
	 * ����������û��Զ���sql��ѯ
	 * @param jdbcCallback
	 * @return
	 * @throws SQLException
	 */
	Object execute(JDBCCallback jdbcCallback)throws SQLException;
	/**
	 * ���ݴ����sql����ѯ���󣬽����һ�����飬Ҳ��Ψһ����
	 * @param sql
	 * @return
	 */
	Object find(String sql);
	/**
	 * ����List,�����һ������
	 * @param sql
	 * @return
	 */
	List list(String sql);
	/**
	 * 
	 * @param sql sql���
	 * @param mapping ӳ��ɶ���Ľӿ�, ��Ҫ�Լ�ʵ�ֹ��򣬺��ڵĻ�Ԫ����ӳ�����ֱ����ע�����ʽд�뵽bean��ȥ
	 * @return
	 * @throws SQLException
	 */
	public List list(String sql, Mapping mapping) throws SQLException;
	/**
	 * 
	 * @param sql
	 * @param mapping
	 * @param pageRow
	 * @return
	 */
	public PageResult list(String sql,Mapping mapping,PageRow pageRow);
	/**
	 * �����ʵ���������ļ���ע��������ݿ�ĺ���
	 * @param className �ӿڵ�class
	 * @return
	 */
	public  Object getMapper(Class className);
	/**
	 * 
	 * @param className
	 * @param pageRow
	 * @return
	 */
	public Object getMapper(Class className,PageRow pageRow);
	/**
	 * ����
	 * @param sql
	 */
	public void update(String string);
	
	/**
	 * ����
	 * @param sql
	 */
	
	public void insert(String string);
	/**
	 * ɾ��
	 * @param sql
	 */
	public void delete(String string);
	

}