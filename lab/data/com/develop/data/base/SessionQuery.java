package com.develop.data.base;

import java.sql.SQLException;
import java.util.List;

import com.develop.data.base.iml.JDBCContext;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;

/**
 * ��Ҫ�����������ѯ�����⣬��������Ӧ��ӳ�����ԭ��
 * @author starlee
 *
 */
public interface SessionQuery {
	/**
	 * ִ�в�ѯ��Ĭ���Ƿ���һ��Object[]��List
	 * @return
	 */
	List executeQuery() throws SQLException;
	/**
	 * ִ�в�ѯ
	 */
	void executeUpdate() throws SQLException;
	/**
	 * ����һ�����������Ӧ���Ƕ�����
	 * @return
	 */
	Object executeOne() throws SQLException;
	/**
	 * ��ȡJDBC������
	 * @return
	 */
	JDBCContext getJDBCContext();
	/**
	 * ִ�в�ѯ
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	List executeQuery(Mapping mapping) throws SQLException;
	
	/**
	 * �������ȡ�õ�����
	 * @param size
	 */
	public void setMaxAmount(int size);
	/**
	 * ���õ�һ��λ��
	 * @param first
	 */
	public void setFirstRow(int first);
	
	/**
	 * ����ҳ��ѯ
	 * @param mapping
	 * @param pageRow
	 * @return
	 * @throws SQLException
	 */
	public PageResult executePage(Mapping mapping,PageRow pageRow) throws SQLException;
}
