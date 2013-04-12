package com.develop.data.base.mapping;

import java.util.Map;

/**
 * 解释原生的SQL语句
 * @author StarLee
 *
 */
public interface SQLInterprete
{
	public String parse(PreSql preSql);
}
