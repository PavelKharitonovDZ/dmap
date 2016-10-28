package ru.dz.vita2d.data.ref;

import ru.dz.vita2d.data.ITypeCache;
import ru.dz.vita2d.data.net.ServerCache;
import ru.dz.vita2d.data.type.ServerUnitType;

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

	/** deserialize */
	public UnitRef(String s) {
		s = s.substring(5);
		int col = s.indexOf('-');
		if( col < 0 )
			throw new RuntimeException("wrong format: "+s);

		id = Integer.parseInt(s.substring(0,col));
		String unitName = s.substring(col+1);
		type = ServerUnitType.fromString( unitName );
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

	@Override
	public ITypeCache getPerTypeCache(ServerCache sc) {		
		return sc.getTypeCache(type);
	}
	
}
