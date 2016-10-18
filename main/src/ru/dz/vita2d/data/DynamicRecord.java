package ru.dz.vita2d.data;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DynamicRecord 
{	
	private Map<String,String> data = new HashMap<>();
	
	public String getValue(String fieldName) {		return data.get(fieldName);	}
	public void setValue(String fieldName, String value) { data.put(fieldName, value);	}

}
