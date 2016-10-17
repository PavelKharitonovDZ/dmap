package ru.dz.vita2d.data;

/**
 * This is actually a string with a server unitType name
 * @author dz
 *
 */
public class ServerUnitType 
{
	/*
	static final public String UNIT_TYPE_OBJECTS = "objs";
	static final public String UNIT_TYPE_MEANS = "means";
	static final public String UNIT_TYPE_JOBS = "jobs";
	static final public String UNIT_TYPE_EVENTS = "events";
	*/

	static final public ServerUnitType OBJECTS = new ServerUnitType("objs");
	static final public ServerUnitType MEANS = new ServerUnitType("means");
	static final public ServerUnitType JOBS = new ServerUnitType("jobs");
	static final public ServerUnitType EVENTS = new ServerUnitType("events");
	
	
	private final String type;
	
	private ServerUnitType(String name) {
		type = name;
	}
	
	public String getTypeName() { return type; }
	
	@Override
	public String toString() {
		return type;
	}
	
}
