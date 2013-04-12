package com.develop.actions.forward;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.develop.actions.action.ActionInterceptor;
import com.develop.actions.action.ActionInvocation;
import com.develop.actions.action.DefaultValueStack;
import com.develop.actions.action.ValueStack;
import com.develop.actions.bean.ActionBean;
import com.develop.actions.bean.PageBean;
import com.develop.actions.bean.ResultTypeBean;
import com.develop.actions.resource.ResourceStatistic;
import com.develop.actions.result.AbstractResult;
import com.develop.actions.result.IResult;
import com.develop.actions.statistics.ActionStatistics;
import com.develop.actions.statistics.WebObject;

/**
 * ����Ǻ��ĵ��࣬����ҳ����ת��������أ��Լ���Ӧ�Ĵ�����
 * 
 * @author starlee
 * 
 */
public class DefaultActionInvocation implements ActionInvocation
{

	private Logger log = Logger.getLogger(this.getClass());
	private ActionBean actionBean;

	private Iterator<ActionInterceptor> it = null;
	private Object targetObject;
	private ValueStack valueStack;
	private PageBean pageBean;
	private ResourceStatistic configuration;
	
	
	public DefaultActionInvocation(ActionBean actionBean,ResourceStatistic configuration)
	{
		this.valueStack = new DefaultValueStack();
		this.actionBean = actionBean;
		it = configuration.getInterceptorList().iterator();
		targetObject = generateRequestObject();
		this.configuration=configuration;
	}

	@Override
	public String getResult()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueStack getValueStack()
	{

		return this.valueStack;
	}

	@Override
	public void invoke()
	{
		if (it.hasNext())
		{
			ActionInterceptor inter = it.next();
			inter.invoke(this);
		} else
		{
			try
			{
				
				String resultTarget = executeTarget();
				beforeResult();
				PageBean pageBean=getPageBean(resultTarget);
				IResult result=executeResult(pageBean);
				//Todo���һ��result�ļ��
				result.execute(this);

			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	/**
	 * ������Ŀ�����󽫽������request��valueStack��
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private void beforeResult() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		ServletRequest request = (ServletRequest) getActionStatistics().get(
				WebObject.SERVLETREQUEST);
		PropertyDescriptor[] properties = PropertyUtils
				.getPropertyDescriptors(getRequestObject());
		for (PropertyDescriptor property : properties)
		{
			// ����ط��ü�������ת��
			if (PropertyUtils.getReadMethod(property) != null)
			{

				String propertyName = property.getDisplayName();

				request.setAttribute(propertyName, PropertyUtils.getProperty(
						getRequestObject(), propertyName));
				log.info("target����:"+propertyName);
				valueStack.setValue(propertyName, PropertyUtils.getProperty(
						getRequestObject(), propertyName));
			}
		}
	}

	private IResult executeResult(PageBean page) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			InvocationTargetException, NoSuchMethodException
	{
		String type = page.getType();
		Map map = ResourceStatistic.getInstance().getResultTypeMap();
		ResultTypeBean resultType = (ResultTypeBean) map.get(type);
		IResult resultForward = (IResult) (Class.forName(resultType
				.getClassName()).newInstance());// ���������ļ�����ʽ��������ֵ��Ӧ�Ľ��
		/*if (AbstractResult.class.isAssignableFrom(resultForward.getClass()))
		{
			// ��ط�������д�ɷ�����Ƶģ��������֣��������У���Ϊ�����Ļ��������ʧȥ�˱�����Ĺ���
			// ���һ���ǲ�̫��ȡ��
			AbstractResult resultSpecial = (AbstractResult) resultForward;
			if (!page.getMap().containsKey("location"))
				throw new RuntimeException("no location,please check your configure file");
			resultSpecial.setLocation((String) page.getMap().get("location"));
			resultForward = resultSpecial;
		} 
		*����ǲ��ǿ����Ƶ���һ���ط�ȥ
		*/
		PropertyDescriptor[] propertiesOther = PropertyUtils
				.getPropertyDescriptors(resultForward);
		for (PropertyDescriptor property : propertiesOther)
		{
			if (property.getWriteMethod() != null)
			{
				log.info("result����:"+property.getDisplayName());
				PropertyUtils.setProperty(resultForward,
						property.getDisplayName(),
						page.getMap().get(property.getDisplayName()));
			}
		}
		return resultForward; 

	}

	/**
	 * ִ��Ŀ�귽��
	 * 
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private String executeTarget() throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException
	{
		Method method = getRequestObject().getClass().getMethod(
				getRequestMethod(), new Class[]
				{});
		String result = (String) method.invoke(getRequestObject(), new Object[]
		{});
		return result;
	}

	/**
	 * ���ҷ�������
	 * 
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private PageBean getPageBean(String result)
	{
		PageBean page = (PageBean) actionBean.getPages().get(result);
		return page;
	}

	public void setActionBean(ActionBean actionBean)
	{
		this.actionBean = actionBean;
	}

	@Override
	public ActionStatistics getActionStatistics()
	{
		return ActionStatistics.getInstance();
	}

	@Override
	public String getRequestMethod()
	{
		String method = actionBean.getMethod();
		if (method == null)
			return "execute";
		return method;
	}

	@Override
	public Object getRequestObject()
	{
		return this.targetObject;
	}

	private Object generateRequestObject()
	{
		try
		{
			targetObject = Class.forName(actionBean.getClassName())
					.newInstance();
			return targetObject;
		} catch (ClassNotFoundException e)
		{
			if (log.isInfoEnabled())
				log.info("The class that the name is"
						+ actionBean.getClassName() + " doesn't exist");
			e.printStackTrace();

		} catch (InstantiationException e)
		{
			if (log.isInfoEnabled())
				log.error("no special constructor exist,please check your class");
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResourceStatistic getConfiguration()
	{
		return this.configuration;
	}

}
