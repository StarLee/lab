package com.develop.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ���ע���ǹ��ڲ�ѯSql���
 * @author starlee
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQL
{
	public SQLTYPE sqlType();
	public String sql();
	public String resultType() default "";
	public String paramType() default "";
}
