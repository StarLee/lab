package com.develop.data.base.iml;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.develop.data.base.ConnectionBase;
import com.develop.data.base.Session;
import com.develop.data.base.SessionFactory;
import com.develop.data.parse.ResourceStatistics;
/**
 * 获取Session的工厂
 * @author starlee
 * 是对外使用的
 */
public class SessionUtil
{
	private static final ThreadLocal<Session> localSession=new ThreadLocal<Session>();
	private static final SessionFactory factory;
	private static final Logger log=Logger.getLogger("com.develop.data.base.iml.SessionUtil");
	static 
	{
		/**
		 * 这样写的原因是我们已经把配置文件固定了，
		 * 不需要了，否则这样设计是有问题。对于资源是唯一的，帮单例就可以了
		 */
		factory = ResourceStatistics.getInstance().buildSessionFactory();
	}
	/**
	 * 由特定的SessionFactory生成
	 * @param sessionFactory
	 * @return
	 */
	public static Session createSession(SessionFactory sessionFactory)
	{
		log.info("线程："+Thread.currentThread().getName());
		Session session=localSession.get();
		if(session==null)
		{
			session=sessionFactory.openSession();
			localSession.set(session);
		}
		return session;
	}
	
	/**
	 * 开session
	 * @return
	 */
	public static Session createSession()
	{
		if(log.isInfoEnabled())
			log.info("线程："+Thread.currentThread().getName());
		Session session=localSession.get();
		if(session==null)
		{
			session=factory.openSession();
			localSession.set(session);
		}
		return session;
	}
	/**
	 * 关闭
	 */
	public static void closeSession()
	{
		Session session=localSession.get();
		if(session!=null)
		{
			try
			{
				session.close();
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			localSession.set(null);
		}
	}
}
