package ru.dz.vita2d.data;

import java.io.IOException;

import org.json.JSONObject;

public interface IRestCaller {

	/**
	 * Logs us in. Must be called first.
	 * @param login
	 * @param password
	 * @throws IOException
	 */
	void login(String login, String password) throws IOException;

	/**
	 * Get list of objects of given type. 
	 * @param type See ServerUnitType constants for types.
	 * @return JSON with data
	 * @throws IOException
	 */

	JSONObject loadUnitList(ServerUnitType type) throws IOException;

	/** God knows what differs units from other entities */
	JSONObject loadOtherList(String entityType) throws IOException;

	/** God knows what differs units from other entities */
	JSONObject loadDictList(String entityType) throws IOException;

	/**
	 * <p>Get object of given type.<p> 
	 * @param type entity type?
	 * @param id object id
	 * @return JSON with data
	 * @throws IOException
	 */

	JSONObject getDataRecord(IRef ref) throws IOException;

	/**
	 * <p>Get object of given type.<p> 
	 * @param type See ServerUnitType constants for types.
	 * @param id object id
	 * @return JSON with data
	 * @throws IOException
	 */

	JSONObject getDataRecord(ServerUnitType type, int id) throws IOException;

	/**
	 * <p>Get object of given type.<p> 
	 * @param ref object reference (type+id)
	 * @return JSON with data
	 * @throws IOException
	 */

	JSONObject getDataRecord(UnitRef ref) throws IOException;

	/**
	 * Get data model (field names, types, etc) for given type.
	 * @param unitType See ServerUnitType constants for types.
	 * @return JSON with model
	 * @throws IOException
	 */
	JSONObject getDataModel(ServerUnitType unitType) throws IOException;

	JSONObject getDataModel(String entityName) throws IOException;

	/**
	 * <p>Get server version by parsing web site index page.</p>
	 * <p>TODO: make REST API call for that!</p>
	 * @return Server version string.
	 */
	String getServerVersion();

	String getLoggedInUser();

	String getServerURL();

}