package com.develop.data.parse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.develop.actions.resource.DefaultURL;
import com.develop.actions.resource.Resource;
import com.develop.data.base.ConnectionInfo;

/**
 * 解析资源 数据库连接以及表的元数据
 * 
 * @author starlee
 * 
 *         2011-7-12
 */
public class DataResource implements Resource
{

	private Logger log = Logger.getLogger(this.getClass());
	private Document document;

	public DataResource()
	{
		this.document = new DefaultURL("data.cfg.xml").parseURL();
	}

	public Map parseData()
	{
		Element ele = this.document.getRootElement();
		Element propertyEle = ele.element("properties");//数据库的一般属性
		if(propertyEle==null)
			throw new RuntimeException("properties未发现，你是否未配置数据库相关信息?");
		Element propertyTables = ele.element("tables");//表<-->对象映射元数据
		if(propertyTables!=null)
		{
			prepareTable(propertyTables);
		}
		Element propertyMappers=ele.element("mappers");//具体操作的sql语句
		if(propertyMappers!=null)
			prepareMapper(propertyMappers);
		prepareProperties(propertyEle);
		return null;
	}

	public void prepareProperties(Element element)
	{
		ConnectionInfo info = new ConnectionInfo();
		Map<String, String> map = new HashMap();
		for (Iterator<Element> it = element.elementIterator(); it.hasNext();)
		{
			Element ele = it.next();
			if (ele.getName().equals("property"))
			{
				map.put(ele.attributeValue("name"), ele.getTextTrim());
			}
		}
		info.setDriver(map.get("driverClass"));
		info.setUrl(map.get("driverURL"));
		info.setPassword(map.get("password"));
		info.setUser(map.get("userName"));
		info.setMaxPool("maximum-connection-count");
		ResourceStatistics.getInstance().setConnectionInfo(info);
	}

	public void prepareTable(Element ele)
	{
		log.info("开始解析表");
		for (Iterator<Element> it = ele.elementIterator(); it.hasNext();)
		{
			Element urlElement = it.next();
			String url=urlElement.attributeValue("url");
			log.info("解析地址->"+url);
			Document doc=new DefaultURL(url).parseURL();
		}
	}
	
	public void prepareMapper(Element ele)
	{
		log.info("解析映射sql");
		Map mapper=new HashMap();
		for (Iterator<Element> it = ele.elementIterator(); it.hasNext();)
		{
			Element urlElement = it.next();
			String url=urlElement.attributeValue("url");
			log.info("解析映射地址->"+url);
			Document doc=new DefaultURL(url).parseURL();
			ParseMapper parseMapper=new ParseMapper(doc);
			log.info("sql空间"+parseMapper.identity());
			mapper.put(parseMapper.identity(),parseMapper.body());
		}
		ResourceStatistics.getInstance().setMapper(mapper);
	}
}
