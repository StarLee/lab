package com.develop.data;

import java.util.List;
/**
 * 这个要采用注入
 * @author starlee
 *
 */
public class TestLookData
{
	TestInterface test;
	
	Object find()
	{
		return test.findAllCollege();
	}
}
