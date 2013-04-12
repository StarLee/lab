package com.develop.data.buider;

import java.lang.reflect.Method;

import com.develop.data.base.Query;
import com.develop.data.parse.BeanMapper;
import com.develop.data.result.PageRow;

public interface Handler
{
	/**
	 * 当前查询器
	 * @return
	 */
	Query getQuery();
	/**
	 * 方法
	 * @return
	 */
	Method getMethod();
	/**
	 * 请求的对象
	 * @return
	 */
	Class getRequestClass();
	/**
	 * 请求的参数
	 * @return
	 */
	Object[] getRequestParams();
	/**
	 * 映射器
	 * @return
	 */
	BeanMapper getMapper();
	/**
	 * 分页器
	 * @return
	 */
	PageRow getPageRow();
}
