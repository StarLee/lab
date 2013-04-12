package com.develop.actions.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.develop.actions.action.ActionInterceptor;
import com.develop.actions.bean.ActionBean;
import com.develop.actions.bean.PageBean;
import com.develop.actions.bean.ResultTypeBean;

/**
 * 
 * 这个是解析action层配置文件的核心地方 这个类可以用多态来重构
 * 
 * @author starlee
 * 
 */
public class XMLDefaultResource implements Resource {
	private Logger log = Logger.getLogger(this.getClass());
	private final static String ACTIONS = "actions";
	private final static String RESULTTYPE = "results";
	private final static String INTERCEPTORS = "interceptors";
	private Document document = null;

	public XMLDefaultResource(ResourceURL url) {
		this.document = url.parseURL();
	}

	public XMLDefaultResource() {
		this(new DefaultURL());
	}

	public Map parseData() {
		Element root = document.getRootElement();
		Map map = new HashMap();
		ResourceStatistic resourceStatistic = ResourceStatistic.getInstance();
		Map actionsMap = new HashMap();
		try {
			for (Iterator it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				log.info(element.getName() + " "
						+ element.attributeValue("name"));
				if (ACTIONS.equals(element.getName())) {
					actionsMap.putAll(prepareAction(element));

				}
				if (RESULTTYPE.equals(element.getName()))// 解析返回类型
				{
					resourceStatistic
							.setResultTypeMap(prepareResultType(element));
				}
				if (INTERCEPTORS.equals(element.getName()))// 解析拦截器
				{
					resourceStatistic
							.setInterceptorList(prepareInterceptors(element));
				}
			}
			resourceStatistic.setActionMap(actionsMap);// 多个怎么处理
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件加载时出现错误，请检查配置文件是否满足要求");
		}
		resourceStatistic.setFlag(true);
		return map;
	}

	public List<ActionInterceptor> prepareInterceptors(Element element) {
		List<ActionInterceptor> list = new ArrayList();
		try {
			for (Iterator<Element> it = element.elementIterator(); it.hasNext();) {
				Element interceptorEle = it.next();
				String className = interceptorEle.attributeValue("class");
				list.add((ActionInterceptor) Class.forName(className)
						.newInstance());
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException("执行过滤器的加载时出错");
		}
	}

	public Map prepareAction(Element element) {
		Map map = new HashMap();

		for (Iterator it = element.elementIterator(); it.hasNext();) {
			Element ele = (Element) it.next();
			ActionBean actionBean = new ActionBean();
			actionBean.setClassName(ele.attributeValue("class"));
			StringBuilder builder = new StringBuilder();
			String namespace = element.attributeValue("namespace");
			builder.append(namespace);
			if (!"/".equals(namespace.trim()))
				builder.append("/");
			builder.append(ele.attributeValue("name"));
			actionBean.setName(ele.attributeValue("name"));
			actionBean.setPages(preparePage(ele));
			actionBean.setMethod(ele.attributeValue("method"));
			if (log.isInfoEnabled()) {
				log.info("action " + builder + " added " + "mapping class "
						+ ele.attributeValue("class"));
			}
			map.put(builder.toString(), actionBean);
		}
		return map;
	}

	public Map preparePage(Element element) {
		Map map = new HashMap();
		for (Iterator it = element.elementIterator(); it.hasNext();) {
			Element ele = (Element) it.next();
			PageBean pageBean = new PageBean();
			if (ele.attributeValue("name") != null)
				pageBean.setName(ele.attributeValue("name"));
			if (ele.attributeValue("type") != null)
				pageBean.setType(ele.attributeValue("type"));
			Map<String, String> mapParam = new HashMap<String, String>();
			if (ele.isTextOnly())// 默认就是location
			{
				mapParam.put("location", ele.getTextTrim());
			} else {
				for (Iterator iit = ele.elementIterator(); iit.hasNext();) {
					Element eleNest = (Element) iit.next();
					mapParam.put(eleNest.attributeValue("name"),
							eleNest.getTextTrim());
				}
			}
			pageBean.setMap(mapParam);
			log.info("Text delete " + ele.getTextTrim());
			log.info("page " + ele.attributeValue("name") + " added");
			map.put(ele.attributeValue("name"), pageBean);
		}
		return map;
	}

	public Map prepareResultType(Element element) {
		Map map = new HashMap();
		for (Iterator it = element.elementIterator(); it.hasNext();) {
			Element ele = (Element) it.next();
			ResultTypeBean bean = new ResultTypeBean();
			bean.setName(ele.attributeValue("name"));
			bean.setClassName(ele.attributeValue("class"));
			log.info("resultType " + ele.attributeValue("name") + " added");
			map.put(ele.attributeValue("name"), bean);
		}
		return map;
	}
}
