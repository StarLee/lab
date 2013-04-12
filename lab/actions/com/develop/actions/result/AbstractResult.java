package com.develop.actions.result;

/**
 * 设计此类的最大的目的是让所有的其它Result类一个很好的location变量，并且此变量可以不在配置文件说明
 * @author starlee
 *
 */
public abstract class AbstractResult implements IResult
{
	private String location;//访问的地址

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
	

}
