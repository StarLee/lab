package com.develop.data;

import java.util.List;

import com.develop.data.base.mapping.Ds_basicBean;
import com.develop.data.base.mapping.Ds_collegeBean;
import com.develop.data.result.PageResult;


public interface TestInterface
{
	
	List findAllCollege();
	PageResult findAllCollegePage();
	void updateCollege(Ds_collegeBean collegeBean);
	void insertNew(Ds_collegeBean collegeBean);
	void deleteCollege(Ds_collegeBean collegeBean);
	Ds_basicBean findBasic(String id);
}
