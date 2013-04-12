package com.develop.data.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.develop.data.base.iml.JDBCContext;

/**
 * ������Ҫ�������������ɵ�PreparedStatement,Statement,ResultSet,CallableStatement
 * @author starlee
 *
 */
public interface JDBCBatcher {
		/**
		 * ����preparedStatement
		 * @return
		 */
		Statement preparedSQLStatement(JDBCContext context) throws SQLException ;
		/**
		 * �����
		 * @param preparedStatement
		 * @return
		 */
		ResultSet getResultSet(Statement preparedStatement,String sql)throws SQLException ;
		/**
		 * �ر�statement
		 * @param statement
		 * @throws SQLException
		 */
		public void closeStatement(Statement statement) throws SQLException;
		/**
		 * �ر�ResultSet
		 * @param rs
		 * @throws SQLException
		 */
		public void closeResultSet(ResultSet rs) throws SQLException;
}
