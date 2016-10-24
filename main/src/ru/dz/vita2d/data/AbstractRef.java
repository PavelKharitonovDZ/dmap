package ru.dz.vita2d.data;

/**
 * <p>Abstract implementation of reference to any system data object, such as entity or unit.</p>
 * 
 * 
 * @author dz
 *
 */

public abstract class AbstractRef implements IRef {

	protected int id;

	public int getId() {		return id;	}
	
	
}
