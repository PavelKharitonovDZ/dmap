package ru.dz.vita2d.data;

/**
 * </p>Reference to server data object. Has server unit type as string and object id.</p>
 * @author dz
 *
 */
public class EntityRef {

	private ServerUnitType type;
	private int id;

	public EntityRef(String reftype, int refid) {
		type = ServerUnitType.fromString( reftype );
		id = refid;
	}

	public EntityRef(ServerUnitType reftype, int refid) {
		type = reftype;
		id = refid;
	}

	public ServerUnitType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

}
