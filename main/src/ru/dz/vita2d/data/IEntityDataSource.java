package ru.dz.vita2d.data;

import org.json.JSONObject;

/**
 * <p>Container for one data object.<p>
 * <p>Usually - received from server.</p>
 * 
 * @author dz
 *
 */
public interface IEntityDataSource {

	public JSONObject getJson();
	
	public String getField(String fieldName );

}
