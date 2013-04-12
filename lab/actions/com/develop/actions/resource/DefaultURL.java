package com.develop.actions.resource;

import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 默认的解析的action层
 * 
 * @author starlee
 * 
 *         2011-7-11
 */
public class DefaultURL implements ResourceURL
{
	private final static String URL = "action.xml";

	private Document document = null;

	public DefaultURL()
	{
		this(URL);
	}

	public DefaultURL(String url)
	{
		ClassLoader loader = this.getClass().getClassLoader();
		java.net.URL defaultURL = loader.getResource(url);
		SAXReader reader = new SAXReader();
		try
		{
			this.document = reader.read(defaultURL);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Document parseURL()
	{
		return document;
	}

}
