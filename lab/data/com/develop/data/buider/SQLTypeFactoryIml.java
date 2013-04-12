package com.develop.data.buider;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author starlee
 * 
 */
public class SQLTypeFactoryIml
{

	private static Map<String, Class> sqlTypeMap = new HashMap<String, Class>();

	static
	{
		sqlTypeMap.put("select", SQLTypeSelect.class);
		sqlTypeMap.put("insert", SQLTypeInsert.class);
		sqlTypeMap.put("delete", SQLTypeDelete.class);
		sqlTypeMap.put("update", SQLTypeUpdate.class);
	}

	public static SQLType create(String string)
	{
		try
		{
			if (sqlTypeMap.containsKey(string))
			{
				return (SQLType) sqlTypeMap.get(string).newInstance();
			}
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RuntimeException("没有对应该的操作，你是要select,update还是delete或者insert");
	}
}
