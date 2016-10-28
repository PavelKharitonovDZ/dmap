package ru.dz.vita2d.data.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.dz.vita2d.data.EntityTypeCache;
import ru.dz.vita2d.data.ITypeCache;
import ru.dz.vita2d.data.UnitTypeCache;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.type.AbstractEntityType;
import ru.dz.vita2d.data.type.EntityType;
import ru.dz.vita2d.data.type.IEntityType;
import ru.dz.vita2d.data.type.ServerUnitType;

/**
 * Place to keep cached data from server.
 * @author dz
 *
 */
public class ServerCache 
{
	private IRestCaller rc;

	//private Map <String,String> fieldNamesMap;
	//private Map <String,String> fieldTypesMap;

	//private Map<ServerUnitType,UnitTypeCache> caches = new HashMap<>(); 
	private Map<AbstractEntityType,ITypeCache> caches = new HashMap<>(); 

	public ServerCache(IRestCaller rc) 
	{
		this.rc = rc;
		ServerUnitType.forEach( t -> caches.put(t, new UnitTypeCache(t, rc, this)));
		EntityType.forEach( t -> caches.put(t, new EntityTypeCache(t.getPluralTypeName(), rc, this)));
		
	}


	public IRestCaller getRestCaller() {
		return rc;
	}

	public ITypeCache getTypeCache(IEntityType type) { return caches.get(type); }

	/**
	 * <p>Get field human readable name by internal name. <b>Slow!</b></p>
	 * <p>Get type specific cache with getTypeCache and call it directly for speed.</p>
	 * 
	 * @param type Server unit type
	 * @param name Field name (id)
	 * 
	 * @return Human readable name.
	 */
	public String getFieldName(AbstractEntityType type, String name)
	{
		return caches.get(type).getFieldName(name);
	}

	/**
	 * <p>Get field type (domain) by internal name. <b>Slow!</b></p>
	 * <p>Get type specific cache with getTypeCache and call it directly for speed.</p>
	 * 
	 * @param type Server unit type
	 * @param name Field name (id)
	 * 
	 * @return Data type (domain).
	 */
	public String getFieldType(AbstractEntityType type, String name)
	{
		return caches.get(type).getFieldType(name);
	}



	private Map<IRef,JSONObject> cache = new HashMap<>();

	/**
	 * <p>Get object of given type, cached.<p> 
	 * 
	 * @param type See ServerUnitType constants for types.
	 * @param id object id
	 * @return JSON with data
	 * @throws IOException
	 */

	public JSONObject getDataRecord( IEntityType type, int id ) throws IOException
	{
		IRef ref = type.makeIRef(id);//new IRef.(type, id);
		return getDataRecord( ref );
	}

	/**
	 * <p>Get object of given type, cached.<p> 
	 * 
	 * @param ref object reference (type+id)
	 * @return JSON with data
	 * @throws IOException
	 */

	public JSONObject getDataRecord( IRef ref ) throws IOException
	{
		synchronized (cache) {

			JSONObject cr = cache.get(ref);
			if( cr == null)
			{

				cr = rc.getDataRecord(ref);
				if( cr != null )
				{
					cache.put(ref, cr);
					if( cache.size() > 1000)
					{
						// kill some
						int max = cache.size();
						int rnd = (int)(Math.random() * (max-1));

						// TODO better way?
						//cache.forEach( (k, v) -> { if(rnd-- == 0) { cache.remove(k);  }} );

						for( IRef r : cache.keySet())
						{
							if(rnd-- <= 0) 
							{
								cache.remove(r);
								break;
							}
						}
					}
				}
			}

			return cr;

		}
	}


	// TODO not cached!
	public JSONObject loadUnitList(ServerUnitType type) throws IOException {
		return rc.loadUnitList(type);
	}



	// TODO not cached!
	public JSONObject getDataModel(String entityName) throws IOException {		
		return rc.getDataModel(entityName);
	}


}
