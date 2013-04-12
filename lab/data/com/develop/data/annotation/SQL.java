package com.develop.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 这个注解是关于查询Sql语句
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
