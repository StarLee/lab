package com.develop.data;

import java.util.Iterator;
import java.util.List;

import com.develop.data.base.iml.DefaultQuery;
import com.develop.data.base.mapping.Ds_collegeBean;

public class Test
{
	public static void main(String[] args)
	{
		DefaultQuery query=new DefaultQuery();
		AnnotationTest test=(AnnotationTest)query.getMapper(AnnotationTest.class);
		showList(test.findAll(),"all_Normal");
		Ds_collegeBean bean=new Ds_collegeBean();
		bean.setId(3);
		bean.setFlag("12");
		System.out.println("by object:"+test.find(bean).getCollegeName());
		System.out.println("by param:"+test.findByID(3,"12").getCollegeName());
		System.out.println("mql:"+test.findMql(bean).getCollegeName());
		showList(test.findAllMsql(),"all_Mql");
	}

	private static void showList(List<Ds_collegeBean> ls,String flag)
	{
		Iterator<Ds_collegeBean> it=ls.iterator();
		while(it.hasNext())
		{
			System.out.println(flag+":"+it.next().getCollegeName());
		}
	}
}
