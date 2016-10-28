package ru.dz.vita2d.data.ref;

import java.io.IOException;

import org.json.JSONObject;

import ru.dz.vita2d.data.EntityDataSource;
import ru.dz.vita2d.data.IEntityDataSource;
import ru.dz.vita2d.data.net.ServerCache;

/**
 * <p>Abstract implementation of reference to any system data object, such as entity or unit.</p>
 * 
 * 
 * @author dz
 *
 */

public abstract class AbstractRef implements IRef {

	protected int id;

	public int getId() {		return id;	}
	
	@Override
	public IEntityDataSource instantiate(ServerCache sc) throws IOException {	
		return new EntityDataSource(sc.getDataRecord(this));
	}

	@Override
	public JSONObject getDataModel(ServerCache sc) throws IOException
	{
		return sc.getDataModel(getEntityName());
	}
	
}
