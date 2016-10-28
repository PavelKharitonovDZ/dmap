package ru.dz.vita2d.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.dz.vita2d.data.model.Model;
import ru.dz.vita2d.data.net.IRestCaller;
import ru.dz.vita2d.data.net.ServerCache;
import ru.dz.vita2d.data.type.ServerUnitType;

public class UnitTypeCache extends AbstractTypeCache {

	private ServerUnitType type;
	
	public UnitTypeCache(ServerUnitType type, IRestCaller rc, ServerCache sc) {
		super(rc,sc);
		this.type = type;
	}	

	@Override	
	protected void loadDataModel() throws IOException
	{
		JSONObject mdm = rc.getDataModel(type);
		parseModel(mdm);
		model = new Model(mdm);

	}
	
	
}
