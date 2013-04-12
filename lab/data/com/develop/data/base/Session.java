package com.develop.data.base;

import java.sql.SQLException;

import com.develop.data.base.iml.JDBCContext;
/**
 *	�Ự�࣬��Ҫ������:���ز�ѯ���󣨽��в�ѯ�������񣨻�ȡ�� ,�Լ���ȡConnection����
 * @author starlee
 *
 */
public interface Session 
{
	/**
	 * �ر�session
	 * @throws SQLException
	 */
	void close() throws SQLException ; 
	/**
	 * ��������
	 * @return
	 */
	Transaction createTransaction();
	/**
	 * �����ѯ,���£�ɾ��������
	 * @param string
	 * @return
	 * @throws SQLException
	 */
	SessionQuery createSessionQuery(String string) throws SQLException;
	/**
	 * ��ȡ���ӵ��ṩ��
	 * @return
	 */
	public ConnectionProvider getConnectionProvider();
	/**
	 * ���ݿ�ع�
	 */
	public void rollback();
	
}
