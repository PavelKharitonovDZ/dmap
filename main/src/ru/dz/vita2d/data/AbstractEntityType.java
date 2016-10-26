package ru.dz.vita2d.data;

import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Common stuff for entity and unit type.
 * @author dz
 *
 */
public abstract class AbstractEntityType implements IEntityType {

	protected final String plural;
	protected final String single;

	protected final String displayName;
	protected final String pluralDisplayName;

	protected AbstractEntityType( String name, String displayName, String pluralDisplayName ) {
		this.displayName = displayName;
		this.pluralDisplayName = pluralDisplayName;
		plural = name+"s";
		single = name;
	}

	public String getDisplayName() {		return displayName;	}
	public String getPluralDisplayName() {		return pluralDisplayName;	}

	/**
	 * Single form.
	 * @return type name as in JSON data.
	 */
	@Override
	public String getObjectTypeName() { return single; }
	
	/**
	 * Plural form.
	 * @return type name as in requests.
	 */
	@Override
	public String getPluralTypeName() { return plural; }
	
	/**
	 * Default implementation.
	 */
	public void forEachRecord(JSONObject json, Consumer<JSONObject> jsonRecordConsumer)
	{
		JSONArray a = json.getJSONArray("list");

		a.forEach( li -> { 
			//System.out.println("li = "+li); 
			JSONObject lio = (JSONObject) li;

			if( (this == ServerUnitType.OBJECTS) || (this == ServerUnitType.SINGLE_OBJECTS))
			{
				JSONObject jRecord = lio.getJSONObject("obj");
				jsonRecordConsumer.accept(jRecord);
			}
			else
			{
				String jid = getPluralTypeName();
				if(this == ServerUnitType.DOCUMENTS) jid = "files"; // Oh, my god.
				
				JSONArray ja = lio.getJSONArray(jid);

				ja.forEach(jae -> {
					JSONObject jRecord = (JSONObject) jae;
					jsonRecordConsumer.accept(jRecord);
				} );
			}

		});

	}
}
