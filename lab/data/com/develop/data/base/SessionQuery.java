package com.develop.data.base;

import java.sql.SQLException;
import java.util.List;

import com.develop.data.base.iml.JDBCContext;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;

/**
 * 主要是用来处理查询的问题，并给出对应的映射对象原则
 * @author starlee
 *
 */
public interface SessionQuery {
	/**
	 * 执行查询，默认是返回一个Object[]的List
	 * @return
	 */
	List executeQuery() throws SQLException;
	/**
	 * 执行查询
	 */
	void executeUpdate() throws SQLException;
	/**
	 * 返回一个结果，必须应该是对象了
	 * @return
	 */
	Object executeOne() throws SQLException;
	/**
	 * 获取JDBC上下文
	 * @return
	 */
	JDBCContext getJDBCContext();
	/**
	 * 执行查询
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	List executeQuery(Mapping mapping) throws SQLException;
	
	/**
	 * 设置最大取得的数量
	 * @param size
	 */
	public void setMaxAmount(int size);
	/**
	 * 设置第一个位置
	 * @param first
	 */
	public void setFirstRow(int first);
	
	/**
	 * 按分页查询
	 * @param mapping
	 * @param pageRow
	 * @return
	 * @throws SQLException
	 */
	public PageResult executePage(Mapping mapping,PageRow pageRow) throws SQLException;
}
