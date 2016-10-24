package ru.dz.vita2d.data;

import org.json.JSONObject;

public class JsonParser {

	protected String tryLoadString(JSONObject jo, String name) {
		if(jo.has(name))			
			return jo.getString(name);
		else
			return null;
	}

	protected boolean tryLoadBool(JSONObject jo, String name) {
		if(jo.has(name))			
			return jo.getString(name).equalsIgnoreCase("true");
		else
			return false;
	}

}
