package ru.dz.vita2d.data;

import java.io.IOException;

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


}
