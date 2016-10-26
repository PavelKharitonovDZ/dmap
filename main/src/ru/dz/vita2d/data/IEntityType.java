package ru.dz.vita2d.data;

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
	
	
	/*
	 * TODO
	 * 
	 * break object down to records
	 * 
	 * forEachRecord(JSONObject json, Consumer<JSONObject> jsonRecord	  
	 * 
	 * break record down to fields
	 * 
	 * forEachRecord(JSONObject jsonRecord, BiConsumer<String fieldName,String data> jsonRecord	  
	 */
	
}
