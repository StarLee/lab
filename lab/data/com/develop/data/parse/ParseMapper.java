package com.develop.data.parse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

public class ParseMapper
{
	
	private String identity;//命名空间，其实就是类名
	private Map map;//在此命名空间下所有操作的集合
	public ParseMapper(Document document)
	{
		Element element=document.getRootElement();
		this.identity=element.attributeValue("namespace");
		this.map=parse(element);
	}
	
	public String identity()
	{
		return this.identity;
	}
	
	public Map body()
	{
		return this.map;
	}
	
	private Map parse(Element element)
	{
		Map map=new HashMap();
		for(Iterator<Element> it=element.elementIterator();it.hasNext();)
		{
			BeanMapper  bean=new BeanMapper();
			Element ele=it.next();
			String id=ele.attributeValue("id").trim();
			bean.setMethod(id);
			bean.setOperationType(ele.getName().trim());
			bean.setReturnType(ele.attributeValue("resultType"));
			bean.setSql(ele.getTextTrim());
			bean.setParameterType(ele.attributeValue("parameterType"));
			map.put(ele.attributeValue("id").trim(), bean);
		}
		return map;
	}
}
