package com.develop.data.buider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.develop.data.AnnotationTest;
import com.develop.data.TestInterface;
import com.develop.data.base.Query;
import com.develop.data.base.iml.DefaultQuery;
import com.develop.data.base.mapping.Ds_basicBean;
import com.develop.data.base.mapping.Ds_collegeBean;
import com.develop.data.base.mapping.Mapping;
import com.develop.data.result.PageResult;
import com.develop.data.result.PageRow;

/**
 * 用于形成正式之前的测试
 * 
 * @author starlee
 * 
 */
public class DefaultBuilder
{

	public static void main(String[] args)
	{
		Query query = new DefaultQuery();
		TestInterface face = (TestInterface) query.getMapper(TestInterface.class);
		List list = face.findAllCollege();
		for (Iterator<Ds_collegeBean> it = list.iterator(); it.hasNext();)
		{
			Ds_collegeBean ds=it.next();
			System.out.println("查询:" + ds.getId());
			System.out.println("名字:" + ds.getCollegeName());
		}
		Ds_basicBean bs = face.findBasic("1453");
		System.out.println("名字:" + bs.getName() + "|" + bs.getBirth() + "|"
				+ bs.getYgbn() + "|" + bs.getPolicy());
		
		Object[] object=(Object[])query.find("select count(*) from Ds_college");
		System.out.println(object[0]);
		
		/*Ds_collegeBean college=new Ds_collegeBean();
		college.setCollegeName("测试一下");
		college.setId(1);
		face.updateCollege(college);
		
		Ds_collegeBean college2=new Ds_collegeBean();
		
		college2.setCollegeName("测试一下插入");
		college2.setId(19);
		face.insertNew(college2);*/
		
		/*Ds_collegeBean college3=new Ds_collegeBean();
		
		//college2.setCollegeName("测试一下插入");
		college2.setId(19);
		face.deleteCollege(college2);
		*/
		
	AnnotationTest anno=(AnnotationTest)query.getMapper(AnnotationTest.class);
		List annoList = anno.findAll();
		for (Iterator<Ds_collegeBean> it = annoList.iterator(); it.hasNext();)
		{
			Ds_collegeBean ds=it.next();
			System.out.println("查询:" + ds.getId());
			System.out.println("名字:" + ds.getCollegeName());
		}
		
		PageRow pageRow=new PageRow(3,5);
		TestInterface face2 = (TestInterface)query.getMapper(TestInterface.class, pageRow);
		
		PageResult result=face2.findAllCollegePage();
		/*PageResult result=query.list("select * from Ds_college", new Mapping()
		{

			@Override
			public List getResult(ResultSet rs)
			{
				List list=new ArrayList();
				try
				{
					while(rs.next())
					{
						Ds_collegeBean bean=new Ds_collegeBean();
						bean.setId(rs.getInt(1));
						bean.setCollegeName(rs.getString(2));
						list.add(bean);
					}
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return list;
			}
		}, pageRow);*/
		System.out.println(result.getPages());
		System.out.println(result.getCurrent());
		System.out.println(result.getPageSize());
		System.out.println(result.getRows());
		Iterator<Ds_collegeBean> it=result.iterator();
		while(it.hasNext())
		{
			System.out.println(it.next().getCollegeName());
		}
		
		
	}
}
