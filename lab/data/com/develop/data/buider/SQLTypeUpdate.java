package com.develop.data.buider;

import com.develop.data.base.Query;
/**
 * 
 * @author starlee
 *
 */
public class SQLTypeUpdate extends AbstractSQLType
{
	@Override
	public Object doExecute(Query query)
	{
		query.update(getPreSql().getPureSQL());
		return null;
	}
}
