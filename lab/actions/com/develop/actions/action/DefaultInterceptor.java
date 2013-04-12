package com.develop.actions.action;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.develop.actions.statistics.WebObject;

/**
 * 
 * 这个是必须的，且是默认的，对后台目标类里的类赋值
 * 
 * @author starlee
 * 
 */
public class DefaultInterceptor implements ActionInterceptor
{

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public void invoke(ActionInvocation actionInvocation)
	{
		//HttpServletRequest request = (HttpServletRequest) actionInvocation
			//	.getActionStatistics().get(WebObject.SERVLETREQUEST);
		BasicHttpRequestWrapper requestWrapper=(BasicHttpRequestWrapper)actionInvocation
				.getActionStatistics().get(WebObject.SERVLETREQUEST);
		Object object = actionInvocation.getRequestObject();
		
		try
		{
			PropertyDescriptor[] properties = PropertyUtils
					.getPropertyDescriptors(object);

			for (PropertyDescriptor property : properties)
			{
				// 这个地方得加上类型转换,未加(简单处理了)
				String propertyName = property.getDisplayName();
				/**
				 * 修正客户端没有传，但服务器端write方法的错误
				 */
				if (PropertyUtils.getWriteMethod(property) != null&&requestWrapper.getPassValue(propertyName)!=null)
				{
					log.info(property.getPropertyType());
					//如果是数组(服务器端)
					if (property.getPropertyType().isArray())
					{
						
						//如果是数组，客户端
						if(requestWrapper.getPassValue(propertyName).getClass().isArray())
							PropertyUtils.setProperty(object,property.getDisplayName(), requestWrapper.getPassValue(propertyName));
						else//服务端为数组，但客户端只有一个元素，则长度为1
						{
							Object obje=Array.newInstance(requestWrapper.getPassValue(propertyName).getClass(), 1);
							Array.set(obje, 0, requestWrapper.getPassValue(propertyName));
							PropertyUtils.setProperty(object,property.getDisplayName(),obje);
						}
						
					} else
					{
						PropertyUtils.setProperty(object, property.getDisplayName(),
								requestWrapper.getPassValue(propertyName));
					}
				//	PropertyUtils.setProperty(object, property.getDisplayName(),
					//		requestWrapper.getPassValue(propertyName));
					
				}
				// Object[].class.is property.getClass();

			}
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		// 都必须得调用这个，这样才能执行下面的拦截器
		actionInvocation.invoke();
		// 如果还有后续
	}

	public static void main(String[] args)
	{
		//String[] a =
		//{ "1", "2", "2" };
		//int[] b =
		//{ 1, 2 };
	//	String c="1";
	//	System.out.println(c.getClass());
	//	System.out.println(a.getClass());
	//	System.out.println(int[].class.isAssignableFrom(b.getClass()));
	//	System.out.println(Object[].class.isAssignableFrom(a.getClass()));
	//	System.out.println(a.getClass().isAssignableFrom(Object[].class));
		
		
		Object obje=Array.newInstance(String.class, 1);
		Array.set(obje, 0, "122");
		System.out.println(((String[])obje)[0]);
	}
}
