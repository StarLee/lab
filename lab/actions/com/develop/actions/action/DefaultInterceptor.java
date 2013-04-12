package com.develop.actions.action;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.develop.actions.statistics.WebObject;

/**
 * 
 * ����Ǳ���ģ�����Ĭ�ϵģ��Ժ�̨Ŀ��������ำֵ
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
				// ����ط��ü�������ת��,δ��(�򵥴�����)
				String propertyName = property.getDisplayName();
				/**
				 * �����ͻ���û�д�������������write�����Ĵ���
				 */
				if (PropertyUtils.getWriteMethod(property) != null&&requestWrapper.getPassValue(propertyName)!=null)
				{
					log.info(property.getPropertyType());
					//���������(��������)
					if (property.getPropertyType().isArray())
					{
						
						//��������飬�ͻ���
						if(requestWrapper.getPassValue(propertyName).getClass().isArray())
							PropertyUtils.setProperty(object,property.getDisplayName(), requestWrapper.getPassValue(propertyName));
						else//�����Ϊ���飬���ͻ���ֻ��һ��Ԫ�أ��򳤶�Ϊ1
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
		// ������õ����������������ִ�������������
		actionInvocation.invoke();
		// ������к���
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
