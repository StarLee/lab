package com.develop.data.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.develop.data.base.iml.JDBCContext;

/**
 * 此类主要是用来保存生成的PreparedStatement,Statement,ResultSet,CallableStatement
 * @author starlee
 *
 */
public interface JDBCBatcher {
		/**
		 * 返回preparedStatement
		 * @return
		 */
		Statement preparedSQLStatement(JDBCContext context) throws SQLException ;
		/**
		 * 结果集
		 * @param preparedStatement
		 * @return
		 */
		ResultSet getResultSet(Statement preparedStatement,String sql)throws SQLException ;
		/**
		 * 关闭statement
		 * @param statement
		 * @throws SQLException
		 */
		public void closeStatement(Statement statement) throws SQLException;
		/**
		 * 关闭ResultSet
		 * @param rs
		 * @throws SQLException
		 */
		public void closeResultSet(ResultSet rs) throws SQLException;
}
