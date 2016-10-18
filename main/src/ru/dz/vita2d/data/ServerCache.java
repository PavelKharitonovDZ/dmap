package ru.dz.vita2d.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Place to keep cached data from server.
 * @author dz
 *
 */
public class ServerCache 
{
	private RestCaller rc;

	//private Map <String,String> fieldNamesMap;
	//private Map <String,String> fieldTypesMap;

	private Map<ServerUnitType,PerTypeCache> caches = new HashMap<>(); 
	
	public ServerCache(RestCaller rc) 
	{
		this.rc = rc;
		
		ServerUnitType.forEach( t -> caches.put(t, new PerTypeCache(t, rc)));
		
	}


	public PerTypeCache getTypeCache(ServerUnitType type) { return caches.get(type); }
	
	/**
	 * <p>Get field human readable name by internal name. <b>Slow!</b></p>
	 * <p>Get type specific cache with getTypeCache and call it directly for speed.</p>
	 * 
	 * @param type Server unit type
	 * @param name Field name (id)
	 * 
	 * @return Human readable name.
	 */
	public String getFieldName(ServerUnitType type, String name)
	{
		return caches.get(type).getFieldName(name);
	}
	
	/**
	 * <p>Get field type (domain) by internal name. <b>Slow!</b></p>
	 * <p>Get type specific cache with getTypeCache and call it directly for speed.</p>
	 * 
	 * @param type Server unit type
	 * @param name Field name (id)
	 * 
	 * @return Data type (domain).
	 */
	public String getFieldType(ServerUnitType type, String name)
	{
		return caches.get(type).getFieldType(name);
	}
	
	
	/*
	
	public String getFieldName(String name)
	{
		Map<String, String> fnm = null;
		try {
			fnm = getFieldNamesMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fnm == null ? null : fnm.get(name);
	}

	Map <String,String> getFieldNamesMap() throws IOException
	{
		if( fieldNamesMap != null )
			return fieldNamesMap;

		synchronized (this) {
			if( fieldNamesMap == null )
				loadMeansDataModel();
		}

		return fieldNamesMap;
	}


	
	
	public String getFieldType(String name)
	{
		Map<String, String> ftm = null;
		try {
			ftm = getFieldTypesMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ftm == null ? null : ftm.get(name);
	}

	Map <String,String> getFieldTypesMap() throws IOException
	{
		if( fieldTypesMap != null )
			return fieldTypesMap;

		synchronized (this) {
			if( fieldTypesMap == null )
				loadMeansDataModel();
		}

		return fieldTypesMap;
	}

	
	
	
	
	private void loadMeansDataModel() throws IOException
	{
		JSONObject mdm = rc.getMeansDataModel();
		//RestCaller.dumpJson(mdm);

		//System.out.println();

		fieldNamesMap = new HashMap<String,String>();
		fieldTypesMap = new HashMap<String,String>();

		JSONArray fieldNames = mdm.getJSONArray("items");

		for( Object ao : fieldNames )
		{
			if (ao instanceof JSONObject) {
				JSONObject jo = (JSONObject) ao;
				//RestCaller.dumpJson(jo);

				String fieldId = null;
				String fieldName = null;

				// Get id (internal field name)
				try 
				{
					fieldId = jo.getString("id");
				}
				catch(JSONException je)
				{
					//System.out.println("JSON exception: "+je);
					continue; // Can't go on if no id
				}

				try 
				{
					// Get name on top level
					fieldName = jo.getString("name");
					fieldNamesMap.put(fieldId, fieldName);
				}
				catch(JSONException je)
				{
					//System.out.println("JSON exception: "+je);
					// Ignore, it is ok
				}


				JSONObject attrs = null;

				// Get extended attrs
				try 
				{
					attrs = jo.getJSONObject("attrs");
				}
				catch(JSONException je)
				{
					//System.out.println("JSON exception: "+je);
				}

				if( (attrs != null) && (fieldName == null) ) try 
				{
					// Retry insinde of attrs
					fieldName = attrs.getString("name");
					fieldNamesMap.put(fieldId, fieldName);
				}
				catch(JSONException je)
				{
					//System.out.println("JSON exception: "+je);
				}


				if( attrs != null ) try 
				{
					String fieldType = attrs.getString("domain");
					fieldTypesMap.put(fieldId, fieldType);
				}
				catch(JSONException je)
				{
					System.out.println("JSON exception: "+je+"for "+fieldId);
				}


			}
		}

	}
	*/
}
