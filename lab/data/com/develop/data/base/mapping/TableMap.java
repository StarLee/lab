package com.develop.data.base.mapping;

import java.util.HashMap;
import java.util.Map;
/**
 * tableMap�Ĵ�����
 * @author StarLee
 *
 */
public class TableMap
{
	private  Map<String, ClassInfo> tableMap = new HashMap<String, ClassInfo>();
	public Map<String, ClassInfo> getTableMap()
	{
		return tableMap;
	}

	public void setTableMap(Map<String, ClassInfo> tableMap)
	{
		this.tableMap = tableMap;
	}
	
	public ClassInfo get(String key)
	{
		if(!tableMap.containsKey(key))
		{
			throw new RuntimeException("û���ҵ���ؼ���"+key+"���������Ϣ");
		}
		return tableMap.get(key);
	}
	
	public void put(String key,ClassInfo value)
	{
		tableMap.put(key, value);
	}
	
}
