package ru.dz.vita2d.data;

import java.util.function.Consumer;

import org.json.JSONObject;

/**
 * Interface for entity/unit type common methods.
 * 
 * Server knows list of data types. Each of them is represented with such object on our side.
 * 
 * NB! Exactly one object per type! Factory!
 * 
 * @author dz
 *
 */
public interface IEntityType {

	
	public static IEntityType fromString(String unitType )
	{
		IEntityType ret;
		
		ret =  ServerUnitType.fromString(unitType);
		if( ret != null ) return ret;
		
		ret =  EntityType.fromString(unitType);
		
		return ret;
	}

	public String getDisplayName();
	public String getPluralDisplayName();
	
	/**
	 * Single form.
	 * @return type name as in JSON data.
	 */
	public String getObjectTypeName();
	
	/**
	 * Plural form.
	 * @return type name as in requests.
	 */
	public String getPluralTypeName();
	
	
	/**
	 * <p>Break down server JSON to single records.</p> 
	 * <p>Note that server JSON can contain other (related) record 
	 * types that will be just ignored by this processing.</p>
	 * 
	 * @param json Source JSON as from server
	 * @param jsonRecordConsumer Consumer to get and process one JSON record.
	 */
	void forEachRecord(JSONObject json, Consumer<JSONObject> jsonRecordConsumer);
	
	/*
	 * TODO
	 * 
	 * break object down to records returning IRef?
	 * 
	 * forEachRecord(JSONObject json, Consumer<IRef> referenceConsumer	  
	 * 
	 * break record down to fields
	 * 
	 * forEachRecord(JSONObject jsonRecord, BiConsumer<String fieldName,String data> jsonRecord	  
	 */
	
}
