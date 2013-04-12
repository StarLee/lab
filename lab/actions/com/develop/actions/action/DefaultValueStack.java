package com.develop.actions.action;

import java.util.HashMap;
import java.util.Map;

public class DefaultValueStack implements ValueStack
{
	

	private Map<String,Object> map=new HashMap<String,Object>();
	public DefaultValueStack()
	{
		
	}
	
	@Override
	public Object findValue(String name)
	{
		return map.get(name);
	}

	
	public void setValue(String key,Object value)
	{
		map.put(key, value);
		
	}
}
