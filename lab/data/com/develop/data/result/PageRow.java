package com.develop.data.result;

public class PageRow
{
	private int pageOrder=1;
	private int pageSize=10;
	public PageRow(int pageOrder,int pageSize)
	{
		this.pageOrder=pageOrder;
		this.pageSize=pageSize;
	}
	public int getPageOrder()
	{
		return pageOrder;
	}
	public int getPageSize()
	{
		return pageSize;
	}
}
