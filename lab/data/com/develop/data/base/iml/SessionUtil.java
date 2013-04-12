package com.develop.data.base.iml;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.develop.data.base.ConnectionBase;
import com.develop.data.base.Session;
import com.develop.data.base.SessionFactory;
import com.develop.data.parse.ResourceStatistics;
/**
 * ��ȡSession�Ĺ���
 * @author starlee
 * �Ƕ���ʹ�õ�
 */
public class SessionUtil
{
	private static final ThreadLocal<Session> localSession=new ThreadLocal<Session>();
	private static final SessionFactory factory;
	private static final Logger log=Logger.getLogger("com.develop.data.base.iml.SessionUtil");
	static 
	{
		/**
		 * ����д��ԭ���������Ѿ��������ļ��̶��ˣ�
		 * ����Ҫ�ˣ�������������������⡣������Դ��Ψһ�ģ��ﵥ���Ϳ�����
		 */
		factory = ResourceStatistics.getInstance().buildSessionFactory();
	}
	/**
	 * ���ض���SessionFactory����
	 * @param sessionFactory
	 * @return
	 */
	public static Session createSession(SessionFactory sessionFactory)
	{
		log.info("�̣߳�"+Thread.currentThread().getName());
		Session session=localSession.get();
		if(session==null)
		{
			session=sessionFactory.openSession();
			localSession.set(session);
		}
		return session;
	}
	
	/**
	 * ��session
	 * @return
	 */
	public static Session createSession()
	{
		if(log.isInfoEnabled())
			log.info("�̣߳�"+Thread.currentThread().getName());
		Session session=localSession.get();
		if(session==null)
		{
			session=factory.openSession();
			localSession.set(session);
		}
		return session;
	}
	/**
	 * �ر�
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
