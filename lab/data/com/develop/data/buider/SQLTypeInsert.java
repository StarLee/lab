package com.develop.data.buider;

import com.develop.data.base.Query;
/**
 * 
 * @author starlee
 *
 */
public class SQLTypeInsert extends AbstractSQLType
{
	@Override
	public Object doExecute(Query query)
	{
		query.insert(getPreSql().getPureSQL());
		return null;
	}
}
