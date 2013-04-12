package com.develop.actions.result;

import com.develop.actions.action.ActionInvocation;

public interface IResult
{
	
	/**
	 * 按正常应该要有一个环境参数传入，现在还没有做这一项
	 */
	void execute(ActionInvocation actionInvocation);
}
