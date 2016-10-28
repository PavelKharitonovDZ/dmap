package ru.dz.vita2d.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.dz.vita2d.data.model.Model;
import ru.dz.vita2d.data.model.ModelFieldDefinition;
import ru.dz.vita2d.data.net.IRestCaller;
import ru.dz.vita2d.data.net.ServerCache;

public abstract class AbstractTypeCache implements ITypeCache  {

	protected IRestCaller rc;
	protected ServerCache sc;

	protected Model model;

	private Map <String,String> fieldNamesMap;
	private Map <String,String> fieldTypesMap;
	
	private Object modelMutex = new Object();

	public AbstractTypeCache(IRestCaller rc, ServerCache sc) {
		super();
		this.rc = rc;
		this.sc = sc;
	}

	@Override
	public ServerCache getServerCache() {
		return sc;
	}

	protected abstract void loadDataModel() throws IOException;

	
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.ITypeCache#updateFieldValuesStats(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFieldValuesStats(String fieldName, String fieldValue)
	{
		if( model == null ) return;
		
		model.updateFieldValuesStats(fieldName, fieldValue);
	}


	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.ITypeCache#getFieldModel(java.lang.String)
	 */
	@Override
	public ModelFieldDefinition getFieldModel(String fn) {
		
		return model.getFieldModel(fn);
	}

	
	Map <String,String> getFieldNamesMap() throws IOException
	{
		if( fieldNamesMap != null )
			return fieldNamesMap;

		synchronized (modelMutex) {
			if( fieldNamesMap == null )
				loadDataModel();
		}

		return fieldNamesMap;
	}
	
	
	
	protected void parseModel(JSONObject model) {
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
	

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.ITypeCache#getFieldType(java.lang.String)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.ITypeCache#getFieldName(java.lang.String)
	 */
	@Override
	public String getFieldName(String name)
	{
		Map<String, String> fnm = null;
		try {
			fnm = getFieldNamesMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(org.json.JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fnm == null ? null : fnm.get(name);
	}
	



	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.ITypeCache#getFieldIds()
	 */
	@Override
	public Set<String> getFieldIds() {
		return fieldNamesMap.keySet();		
	}
	
	
	
}