package com.develop.data.buider;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.develop.data.base.Query;
import com.develop.data.base.mapping.DefaultPreSql;
import com.develop.data.base.mapping.PreSql;
import com.develop.data.base.mapping.SQLSelectInterprete;
import com.develop.data.result.PageRow;

public abstract class AbstractSQLType implements SQLType
{
	private PreSql preSql;//ʵ��Ҫִ�е�sql������
	private String returnType;//����ֵ������(��������ͣ������ļ�)
	private Class returnTypeClass;//���ݺ����ľ��巵��ֵ����
	private PageRow pageRow;//��ҳ��Ϣ
	private Logger log=Logger.getLogger(AbstractSQLType.class);
	@Override
	public Object execute(Handler handler)
	{
		/**begin**/
		/*************************************/
		 /**���ڲ�������ط��ұ�������ع�,Ŀǰ�Ȳ�������Ŀ����**/
		 /*************************************/
		Object[] paramValue=handler.getRequestParams();
		String paramType=handler.getMapper().getParameterType();//������ط����Կ���Ŀǰֻ֧�ֵ�����
		/**end**/
		PreSql preSql = new DefaultPreSql();
		if(log.isDebugEnabled())
			log.debug("�鿴������õ���"+this.getClass().getName());
		if(com.develop.data.buider.SQLTypeSelect.class.isAssignableFrom(this.getClass()))
		{
			preSql.setSQLInterprete(new SQLSelectInterprete());
		}
		preSql.parseSQL(handler.getMapper().getSql(), paramValue,paramType, handler.getMethod());
		this.preSql=preSql;
		this.returnType=handler.getMapper().getReturnType();//��Ҫ����Ϊӳ��ɶ����õģ�ע��������ļ���������ȼ�,���������Դ��
		this.returnTypeClass = handler.getMethod().getReturnType();//�ӿڷ���ʵ�ʵķ������ͣ�ͨ������ָ���ķ�������ȷ����
		this.pageRow=handler.getPageRow();//���Ҳ�ǲ�ѯ�Ż��õ���
		Query query=handler.getQuery();
		return doExecute(query);
	}
	
	public Class getReturnTypeClass()
	{
		return returnTypeClass;
	}

	public PageRow getPageRow()
	{
		return pageRow;
	}

	
	public PreSql getPreSql()
	{
		return preSql;
	}

	public String getReturnType()
	{
		return returnType;
	}

	public abstract Object doExecute(Query query);

	
}
