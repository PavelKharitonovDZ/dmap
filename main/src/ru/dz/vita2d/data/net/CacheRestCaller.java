package ru.dz.vita2d.data.net;

import java.io.IOException;

import org.json.JSONObject;

import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;
import ru.dz.vita2d.data.store.LocalFileStorage;
import ru.dz.vita2d.data.store.RestSupplier;
import ru.dz.vita2d.data.type.IEntityType;
import ru.dz.vita2d.data.type.ServerUnitType;

public class CacheRestCaller implements IRestCaller {

	private IRestCaller src;
	private String user;

	public CacheRestCaller(IRestCaller src) {
		this.src = src;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void login(String login, String password) throws IOException {
		try {
			src.login(login, password);
			user = login;
		} catch (IOException e) {

			offline_login(login, password);
			user = login;
		}

	}

	private void offline_login(String login, String password) throws IOException {
		JSONObject users = loadOtherList("users");
		
		// TODO Notify additionally about offline login
		throw new java.net.ProtocolException("offline loin failed");
	}

	@Override
	public String getLoggedInUser() {
		return user;
	}

	
	
	
	@Override
	public JSONObject loadUnitList(ServerUnitType type) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.loadUnitList(type); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "ulist_"+type.getPluralTypeName()+".rest");
		return out;
	}

	@Override
	public JSONObject loadOtherList(String entityType) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.loadOtherList(entityType); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "elist_"+entityType+".rest");
		return out;
	}

	@Override
	public JSONObject loadDictList(String entityType) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.loadDictList(entityType); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "dict_"+entityType+".rest");
		return out;
	}

	@Override
	public JSONObject getDataRecord(IRef ref) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.getDataRecord(ref); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "data_record_"+ref.serialize()+".rest");
		return out;
	}

	@Override
	public JSONObject getDataRecord(ServerUnitType type, int id) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.getDataRecord(type,id); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "data_record_"+type.getPluralTypeName()+"-"+id+".rest");
		return out;
	}

	@Override
	public JSONObject getDataRecord(UnitRef ref) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.getDataRecord(ref); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "data_record_"+ref.serialize()+".rest");
		return out;
	}

	
	
	@Override
	public JSONObject getDataModel(IEntityType unitType) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.getDataModel(unitType); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "unit_data_model_"+unitType.getPluralTypeName()+".rest");
		return out;
	}

	
	/*@Override
	public JSONObject getDataModel(String entityName) throws IOException {
		RestSupplier<String> s = () -> { return src.getDataModel(entityName).toString(); };
		LocalFileStorage.cachedLoader(s, "data_model_"+entityName+".rest");
		return new JSONObject(s);
	}*/

	@Override
	public JSONObject getDataModel(String entityName) throws IOException {
		RestSupplier<JSONObject> s = () -> { return src.getDataModel(entityName); };
		JSONObject out = LocalFileStorage.JSONCachedLoader(s, "data_model_"+entityName+".rest");
		return out;
	}


	@Override
	public String getServerVersion() {
		return LocalFileStorage.cachedLoader( () -> src.getServerVersion(), "server_version.txt");
	}


	@Override
	public String getServerURL() {
		return LocalFileStorage.cachedLoader( () -> src.getServerURL(), "server_url.txt");
	}

}
