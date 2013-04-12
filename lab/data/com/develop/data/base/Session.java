package com.develop.data.base;

import java.sql.SQLException;

import com.develop.data.base.iml.JDBCContext;
/**
 *	会话类，主要功能有:返回查询对象（进行查询），事务（获取） ,以及获取Connection连接
 * @author starlee
 *
 */
public interface Session 
{
	/**
	 * 关闭session
	 * @throws SQLException
	 */
	void close() throws SQLException ; 
	/**
	 * 创造事务
	 * @return
	 */
	Transaction createTransaction();
	/**
	 * 创造查询,更新，删除，插入
	 * @param string
	 * @return
	 * @throws SQLException
	 */
	SessionQuery createSessionQuery(String string) throws SQLException;
	/**
	 * 获取连接的提供者
	 * @return
	 */
	public ConnectionProvider getConnectionProvider();
	/**
	 * 数据库回滚
	 */
	public void rollback();
	
}
