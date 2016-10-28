package ru.dz.vita2d.data.type;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;

/**
 * This is actually a string with a server unitType name
 * @author dz
 *
 */
public class ServerUnitType extends AbstractEntityType
{
	protected static Set<ServerUnitType> all;
	static {
		all = new HashSet<>();
	}

	static final public ServerUnitType OBJECTS = new ServerUnitType("obj","объект","объекты");
	static final public ServerUnitType MEANS = new ServerUnitType("mean","средство","средства");
	static final public AbstractEntityType JOBS = new ServerUnitType("job","работа","работы");
	static final public AbstractEntityType EVENTS = new ServerUnitType("event","событие","события");

	static final public AbstractEntityType SINGLE_OBJECTS = new ServerUnitType("singleObj","подобъект","подобъекты");
/* all broken
	//static final public ServerUnitType DOCUMENTS = new ServerUnitType("linkedFile","документ");
	static final public ServerUnitType CONTRACTS = new ServerUnitType("contract","контракт");
	static final public ServerUnitType NOTIFICATIONS = new ServerUnitType("notification","напоминание");
*/
	static final public ServerUnitType DOCUMENTS = new ServerUnitType("file","документ", "документы" );

	// does not work
	//static final public ServerUnitType EMPLOYEES = new ServerUnitType("employee","сотрудник");
	
	private ServerUnitType(String name, String displayName, String pluralDisplayName) {
		super(name, displayName, pluralDisplayName);
		
		all.add(this);
	}
	
	static public void forEach(Consumer<? super ServerUnitType> action)
	{
		all.forEach(action);
	}
	
	
	@Override
	public String toString() {
		return plural;
	}

	static class sutptr {
		ServerUnitType t = null;
	}
	
	public static ServerUnitType fromString(String unitType )
	{
		sutptr p = new sutptr();
		
		all.forEach( t -> { if( t.plural.equalsIgnoreCase(unitType) ) p.t = t; });
		
		if(p.t == null)
			all.forEach( t -> { if( t.single.equalsIgnoreCase(unitType) ) p.t = t; });
		
		//if(p.t == null)			System.out.println("Unknown Unit Type ="+unitType); // TODO log
		
		return p.t;
	}

	@Override
	public IRef makeIRef(int id) {		
		return new UnitRef(this,id);
	}
}
