package ru.dz.vita2d.data.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Set of field value filters.
 * @author dz
 *
 */
public class FilterSet {

	private Map<String,FieldFilter> filters = new HashMap<>();
	private Object mutex = new Object();
	private String searchText;
	private boolean searchActive;
	
	public void clear() { filters.clear(); }
	
	public boolean filter(String id, String data)
	{
		synchronized (mutex) {			
			FieldFilter f = filters.get(id);
			if( f == null) 
				{
				//System.out.println(id+"="+data+" = no f" );
				return true;
				}
			
			boolean fv = f.filter(data);
			//System.out.println(id+"="+data+" = "+(fv?"1":"0") );			
			return fv;
		}
	}
	
	public void add( String fId, String fVal )
	{
		System.out.println("add "+fId+"="+fVal );
		synchronized (mutex) {			
			FieldFilter f = filters.get(fId);
			if( f == null)
			{
				f = new FieldFilter(fId);			
				filters.put(fId, f);
			}

			f.add(fVal);
		}
	}

	public boolean isFilterActive() {
		return !filters.isEmpty();
	}

	
	
	
	public void setSearchFilter(String text) {
		if(text != null)
			text = text.toLowerCase();

		searchText = text;
		searchActive = (text != null) && (text.length() > 0);
		
		//if(searchText != null) System.out.println("srch text="+searchText);
	}

	public boolean checkSearchFilter(String text)
	{
		if(!searchActive) return true;
		
		text = text.toLowerCase();
		
		return text.indexOf(searchText) >= 0;
	}

	public boolean isSearchActive() {
		return searchActive;
	}


	
}
