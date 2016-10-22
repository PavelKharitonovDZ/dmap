package ru.dz.vita2d.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Model as we load with PerTypeCache.loadDataModel.</p>
 * 
 * @author dz
 *
 */
public class Model 
{
	private String name;
	private String id;
	private List<ModelFieldDefinition> items = new ArrayList<>(); 
	//private List<ModelOperationDefinition> operations = new ArrayList<>();
	
	private Map<String,ModelFieldDefinition> itemForField = new HashMap<>();
	
	public Model(JSONObject model)
	{
		if(model.has("name"))
			name = model.getString("name");
		id = model.getString("id");
		
		loadFieldDefs( model.getJSONArray("items"));
	}

	private void loadFieldDefs(JSONArray jitems) 
	{	
		for( Object ao : jitems )
		{
			if(!(ao instanceof JSONObject)) 
			{		
				System.out.println("Model.loadFieldDefs: not JSON "+ao.getClass());
				continue;		
			}
			
			try {
				JSONObject jo = (JSONObject) ao;
				
				ModelFieldDefinition fd = new ModelFieldDefinition(jo);
				items.add(fd);				
				itemForField.put(fd.getId(), fd);
				
			} catch(Throwable e)
			{
				System.out.println("Model.loadFieldDefs: can't load JSON "+ao+", e="+e);
			}
		}
	}
}
