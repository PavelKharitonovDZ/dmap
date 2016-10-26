package ru.dz.vita2d.data;

import org.json.JSONObject;

public class EntityDataSource implements IEntityDataSource {

	private JSONObject dataRecord;

	public EntityDataSource(JSONObject dataRecord) {
		this.dataRecord = dataRecord;		
	}

	public JSONObject getDataRecord() {
		return dataRecord;
	}

	@Override
	public JSONObject getJson() {
		return dataRecord;
	}

	@Override
	public String getField(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
