package com.develop.actions.statistics;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * 封装ServletRequest,ServletResponse
 * @author starlee
 *
 * 2011-7-5
 */
public abstract class WebObject
{
	/*servlet的request*/
	public  final static String SERVLETREQUEST = "request";
	/*servlet的response*/
	public  final static String SERVLETRESPONSE = "response";
	/*servlet的servletContext*/
	public final static String SERVLETCONTEXT="servletContext";
}
