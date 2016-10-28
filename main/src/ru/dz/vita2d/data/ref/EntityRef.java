package ru.dz.vita2d.data.ref;

import java.io.IOException;

import ru.dz.vita2d.data.ITypeCache;
import ru.dz.vita2d.data.net.ServerCache;
import ru.dz.vita2d.data.type.AbstractEntityType;
import ru.dz.vita2d.data.type.EntityType;
import ru.dz.vita2d.data.type.IEntityType;

public class EntityRef extends AbstractRef {
	private String entityName;

	public EntityRef(String reftype, int refid) {
		entityName = reftype;
		id = refid;
	}

	// deserialize
	protected EntityRef(String s) {
		s = s.substring(5);
		int col = s.indexOf('-');
		if( col < 0 )
			throw new RuntimeException("wrong format: "+s);
		
		id = Integer.parseInt(s.substring(0,col));
		entityName = s.substring(col+1);
	}

	@Override
	public String serialize() {
		return String.format("enti-%d-%s", id, entityName);
	}
	
	public String getEntityName() {
		return entityName;
	}

	@Override
	public ITypeCache getPerTypeCache(ServerCache sc) {		
		return sc.getTypeCache( IEntityType.fromString(entityName) );
	}

	@Override
	public AbstractEntityType getType() {
		return EntityType.fromString(entityName);
	}
	
}
