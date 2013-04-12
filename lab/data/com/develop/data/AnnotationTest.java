package com.develop.data;

import java.util.List;

import com.develop.data.annotation.SQL;
import com.develop.data.annotation.SQLTYPE;
import com.develop.data.base.mapping.Ds_collegeBean;

public interface AnnotationTest
{
	@SQL(sqlType=SQLTYPE.SELECT,sql="select id,college_name as collegeName from Ds_college ",resultType="com.develop.data.base.mapping.Ds_collegeBean")
	public List findAll();
	
	@SQL(sqlType=SQLTYPE.SELECT,sql="select id,college_name as collegeName from Ds_college where id=#{id} and flag='#{flag@2}'",resultType="com.develop.data.base.mapping.Ds_collegeBean")
	public Ds_collegeBean findByID(int id,String flag);
	
	@SQL(sqlType=SQLTYPE.SELECT,sql="select id,college_name as collegeName from Ds_college where id=#{id} and flag='#{flag}'",resultType="com.develop.data.base.mapping.Ds_collegeBean")
	public Ds_collegeBean find(Ds_collegeBean bean);
	
	//@SQL(sqlType=SQLTYPE.SELECT,sql="select *@Ds_collegeBean from @Ds_collegeBean where id@Ds_collegeBean=#{id} and flag@Ds_collegeBean='#{flag}'",resultType="com.develop.data.base.mapping.Ds_collegeBean")
	@SQL(sqlType=SQLTYPE.SELECT,sql="select *@Ds_collegeBean from @Ds_collegeBean where id@Ds_collegeBean=#{id} and flag@Ds_collegeBean='#{flag}'")
	public Ds_collegeBean findMql(Ds_collegeBean bean);
	@SQL(sqlType=SQLTYPE.SELECT,sql="select *@Ds_collegeBean from @Ds_collegeBean")
	public List<Ds_collegeBean> findAllMsql();
}
