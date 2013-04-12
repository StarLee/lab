package com.develop.data.base.mapping;

import java.sql.ResultSet;
import java.util.List;

public interface Mapping<T>
{

	//Object createTargetObject();//
	
	List<T> getResult(ResultSet rs);
	
}
