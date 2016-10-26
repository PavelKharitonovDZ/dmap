package ru.dz.vita2d.data;

/**
 * </p>Reference to server data object. Has server unit type as string and object id.</p>
 * @author dz EntityRef
 *
 */
public class UnitRef extends AbstractRef {

	private ServerUnitType type;

	public UnitRef(String reftype, int refid) {
		type = ServerUnitType.fromString( reftype );
		id = refid;
	}

	public UnitRef(ServerUnitType reftype, int refid) {
		type = reftype;
		id = refid;
	}

	public ServerUnitType getType() {
		return type;
	}

	public String getEntityName() {
		return type.getPluralTypeName();
	}

	@Override
	public String serialize() {
		return String.format("unit-%d-%s", id, type.getObjectTypeName());
	}

}
