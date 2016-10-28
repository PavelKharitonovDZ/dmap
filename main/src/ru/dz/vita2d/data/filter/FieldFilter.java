package ru.dz.vita2d.data.filter;

import java.util.HashSet;
import java.util.Set;

/** 
 * <p>Field value filter.</p>
 * <p>Can be used to filter data by field values.</p>
 * @author dz
 *
 */
public class FieldFilter 
{
	private String	id;
	private Set<String> enabledValues = new HashSet<>();
	
	// TODO ranges, dates, datetimes
	
	public FieldFilter(String id )
	{
		this.id = id;		
	}
	
	
	public void add(String value)
	{
		enabledValues.add(value);
	}

	public boolean filter(String data)
	{
		return  enabledValues.isEmpty() || enabledValues.contains(data);
	}
	
}
