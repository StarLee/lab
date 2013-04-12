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
	private PreSql preSql;//实际要执行的sql解析器
	private String returnType;//返回值的类型(对象的类型，配置文件)
	private Class returnTypeClass;//根据函数的具体返回值生成
	private PageRow pageRow;//分页信息
	private Logger log=Logger.getLogger(AbstractSQLType.class);
	@Override
	public Object execute(Handler handler)
	{
		/**begin**/
		/*************************************/
		 /**关于参数这个地方我必须进行重构,目前先不做这项目工作**/
		 /*************************************/
		Object[] paramValue=handler.getRequestParams();
		String paramType=handler.getMapper().getParameterType();//从这个地方可以看出目前只支持单参数
		/**end**/
		PreSql preSql = new DefaultPreSql();
		if(log.isDebugEnabled())
			log.debug("查看具体调用的类"+this.getClass().getName());
		if(com.develop.data.buider.SQLTypeSelect.class.isAssignableFrom(this.getClass()))
		{
			preSql.setSQLInterprete(new SQLSelectInterprete());
		}
		preSql.parseSQL(handler.getMapper().getSql(), paramValue,paramType, handler.getMethod());
		this.preSql=preSql;
		this.returnType=handler.getMapper().getReturnType();//主要是做为映射成对象用的（注解或配置文件是最高优先级,是这个的来源）
		this.returnTypeClass = handler.getMethod().getReturnType();//接口方法实际的返回类型（通过函数指定的返回类型确定）
		this.pageRow=handler.getPageRow();//这个也是查询才会用到的
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
