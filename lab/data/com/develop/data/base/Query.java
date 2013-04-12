package com.develop.data.base;

import java.sql.SQLException;
import java.util.List;

import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;


/**
 * 查询抽象类，这个是脱离配置文件而能自定义sql语句查询的，其提供了一个JDBCOperations接口<br/>
 * 此接口是统一对外提供数据库操作的类<br/>
 * 是一个中间类。对上处理是传递过来的参数，对下是数据库的操作
 * 
 * @author starlee
 * 
 */
public interface Query 
{
	/**
	 * 
	 * @param sql 常规的sql语句
	 * @param mapping 映射成对象的接口，需要自己实现规则，后期的话元数据映射可以直接用注解的形式写入到bean中去
	 * @return
	 * @throws SQLException
	 */
	public Object unique(String sql, Mapping mapping) throws SQLException;
	/**
	 * 这个方法给用户自定义sql查询
	 * @param jdbcCallback
	 * @return
	 * @throws SQLException
	 */
	Object execute(JDBCCallback jdbcCallback)throws SQLException;
	/**
	 * 根据传入的sql语句查询对象，结果是一个数组，也是唯一数组
	 * @param sql
	 * @return
	 */
	Object find(String sql);
	/**
	 * 返回List,结果是一个数据
	 * @param sql
	 * @return
	 */
	List list(String sql);
	/**
	 * 
	 * @param sql sql语句
	 * @param mapping 映射成对象的接口, 需要自己实现规则，后期的话元数据映射可以直接用注解的形式写入到bean中去
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
	 * 这个是实现用配置文件或注解操作数据库的核心
	 * @param className 接口的class
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
	 * 更新
	 * @param sql
	 */
	public void update(String string);
	
	/**
	 * 插入
	 * @param sql
	 */
	
	public void insert(String string);
	/**
	 * 删除
	 * @param sql
	 */
	public void delete(String string);
	

}