package com.develop.data.buider;

import java.lang.reflect.Method;

import com.develop.data.base.Query;
import com.develop.data.parse.BeanMapper;
import com.develop.data.result.PageRow;

public interface Handler
{
	/**
	 * ��ǰ��ѯ��
	 * @return
	 */
	Query getQuery();
	/**
	 * ����
	 * @return
	 */
	Method getMethod();
	/**
	 * ����Ķ���
	 * @return
	 */
	Class getRequestClass();
	/**
	 * ����Ĳ���
	 * @return
	 */
	Object[] getRequestParams();
	/**
	 * ӳ����
	 * @return
	 */
	BeanMapper getMapper();
	/**
	 * ��ҳ��
	 * @return
	 */
	PageRow getPageRow();
}
