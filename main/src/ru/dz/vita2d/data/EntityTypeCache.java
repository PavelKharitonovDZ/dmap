package ru.dz.vita2d.data;

import java.io.IOException;

import org.json.JSONObject;

import ru.dz.vita2d.data.model.Model;
import ru.dz.vita2d.data.net.IRestCaller;
import ru.dz.vita2d.data.net.ServerCache;

public class EntityTypeCache extends AbstractTypeCache {
	
	private String type;
	
	public EntityTypeCache(String type, IRestCaller rc, ServerCache sc) {
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
