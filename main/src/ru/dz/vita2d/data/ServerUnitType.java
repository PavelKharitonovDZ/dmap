package ru.dz.vita2d.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This is actually a string with a server unitType name
 * @author dz
 *
 */
public class ServerUnitType 
{
	static private Set<ServerUnitType> all; 
	static {
		all = new HashSet<>();
	}

	static final public ServerUnitType OBJECTS = new ServerUnitType("obj","объект");
	static final public ServerUnitType MEANS = new ServerUnitType("mean","средство");
	static final public ServerUnitType JOBS = new ServerUnitType("job","работа");
	static final public ServerUnitType EVENTS = new ServerUnitType("event","событие");
	
	private final String plural;
	private final String single;
	private final String displayName;
	
	private ServerUnitType(String name, String displayName) {
		this.displayName = displayName;
		plural = name+"s";
		single = name;
		
		all.add(this);
	}
	
	static public void forEach(Consumer<? super ServerUnitType> action)
	{
		all.forEach(action);
	}
	
	/**
	 * Single form.
	 * @return type name as in JSON data.
	 */
	public String getObjectTypeName() { return single; }
	
	/**
	 * Plural form.
	 * @return type name as in requests.
	 */
	public String getPluralTypeName() { return plural; }
	
	public String getDisplayName() {		return displayName;	}

	@Override
	public String toString() {
		return plural;
	}
	
}
