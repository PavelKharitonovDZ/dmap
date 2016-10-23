package ru.dz.vita2d.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PerTypeCache {

	private ServerUnitType type;

	private RestCaller rc;
	private ServerCache sc;

	private Map <String,String> fieldNamesMap;
	private Map <String,String> fieldTypesMap;

	private Model model;

	
	
	public PerTypeCache(ServerUnitType type, RestCaller rc, ServerCache sc) {
		this.type = type;
		this.rc = rc;
		this.sc = sc;
	}
	
	
	public ServerCache getServerCache()
	{
		return sc;
	}
	
	
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
				loadDataModel();
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
				loadDataModel();
		}

		return fieldTypesMap;
	}
	
	
	
	

	
	
	
	private void loadDataModel() throws IOException
	{
		JSONObject mdm = rc.getDataModel(type);
		parseModel(mdm);
		model = new Model(mdm);

	}


	private void parseModel(JSONObject model) {
		fieldNamesMap = new HashMap<String,String>();
		fieldTypesMap = new HashMap<String,String>();

		JSONArray fieldNames = model.getJSONArray("items");

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
	
	
	
	/**
	 * Must be called for each field value when loading table so that we
	 * can gather stats on field values.
	 * 
	 * @param fieldName field short name (id)
	 * @param fieldValue
	 */
	public void updateFieldValuesStats(String fieldName, String fieldValue)
	{
		if( model == null ) return;
		
		model.updateFieldValuesStats(fieldName, fieldValue);
	}


	public ModelFieldDefinition getFieldModel(String fn) {
		
		return model.getFieldModel(fn);
	}


	public Set<String> getFieldIds() {
		return fieldNamesMap.keySet();		
	}
	
	
	
	
	
	
	
	
	
}
