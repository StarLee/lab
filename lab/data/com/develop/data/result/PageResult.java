package com.develop.data.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 返回的是分页的结果
 * @author starlee
 *
 */
public class  PageResult<T> implements Collection<T>
{
	private List<T> list=new ArrayList();//当前的结果 
	private int current;//当前页码
	private int pages;//总的页数
	private int rows;//总的记录数
	private int pageSize;//页容量	
	public int getCurrent()
	{
		return current;
	}
	public void setCurrent(int current)
	{
		this.current = current;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	@Override
	public int size()
	{
		return this.list.size();
	}

	@Override
	public boolean isEmpty()
	{
		return this.list.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return this.list.contains(o);
	}

	@Override
	public Object[] toArray()
	{

		return this.list.toArray();
	}

	

	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.list.toArray(a);
	}
	@Override
	public boolean add(T e)
	{
		return this.list.add(e);
	}

	@Override
	public boolean remove(Object o)
	{
		return this.list.remove(o);
	}

	@Override
	public boolean containsAll(Collection c)
	{
		return this.list.contains(c);
	}

	@Override
	public boolean addAll(Collection c)
	{
		return this.list.addAll(c);
	}

	@Override
	public boolean removeAll(Collection c)
	{
		return this.list.retainAll(c);
	}

	@Override
	public boolean retainAll(Collection c)
	{
		return this.list.retainAll(c);
	}

	@Override
	public void clear()
	{
		this.list.clear();
		
	}
}
