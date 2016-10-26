package ru.dz.vita2d.data;

/**
 * Common stuff for entity and unit type.
 * @author dz
 *
 */
public abstract class AbstractEntityType implements IEntityType {

	protected final String plural;
	protected final String single;

	protected final String displayName;
	protected final String pluralDisplayName;

	protected AbstractEntityType( String name, String displayName, String pluralDisplayName ) {
		this.displayName = displayName;
		this.pluralDisplayName = pluralDisplayName;
		plural = name+"s";
		single = name;
	}

	public String getDisplayName() {		return displayName;	}
	public String getPluralDisplayName() {		return pluralDisplayName;	}

	/**
	 * Single form.
	 * @return type name as in JSON data.
	 */
	@Override
	public String getObjectTypeName() { return single; }
	
	/**
	 * Plural form.
	 * @return type name as in requests.
	 */
	@Override
	public String getPluralTypeName() { return plural; }
}
