package ru.dz.vita2d.data.model;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import ru.dz.vita2d.data.DataConvertor;

/**
 * <p>Definition (name, readable name, type, etc) of field as read by 
 * RestCaller.getDataModel (resources/models/<type-name>-form.js).</p>
 * 
 * <p>Used in field visualization, search and filering.</p>
 * 
 * <p>Can be populated with list of existing values and/or value range.</p>
 * 
 * @author dz
 *
 */
public class ModelFieldDefinition extends JsonParser
{
	/**
	 * Internal field name, as in model's items.id field.
	 */
	private String		id;			

	/**
	 * Human readable name, as in model's items.name or items.attrs.name field.
	 */
	private String		name;		

	/**
	 * Domain (data type) of field, as in items.attrs.domain.
	 */
	private String		domain;		

	/**
	 * Entity this field belongs to, as in items.attrs.entity.
	 * Field is ref and must have id in value?
	 * Domain must be reference?
	 */
	private String		entity;		

	private boolean		required;
	private boolean		readonly;
	private boolean		sortable;
	private boolean		search;

	private String		filter;
	private String		depends;
	private String		columns;
	private String		card;

	//private List<Model> subModels = new ArrayList<>();
	private Model 		subModel;

	// ----------------------------------------------------------
	// Generated data
	// ----------------------------------------------------------

	/** All the values we've seen in data. Generated on table load. */
	private Set<String> values = new HashSet<>();

	/** For numeric fields - minimal value we've seen. */
	private double min;

	/** For numeric fields - maximal value we've seen. */
	private double max;

	/**
	 * Do we have values/min/max data.
	 */
	private boolean	haveValueStats = false;

	
	// ----------------------------------------------------------
	// Loaders
	// ----------------------------------------------------------
	

	public ModelFieldDefinition(JSONObject jo) 
	{
		id = jo.getString("id");

		loadName(jo);

		if(jo.has("attrs"))
			loadAttrs(jo.getJSONObject("attrs"));

		try {
			if(jo.has("model"))
			{
				subModel = new Model(jo.getJSONObject("model"));
			}
		} catch(Throwable e)
		{
			System.out.println("Model.loadFieldDefs: can't load JSON submodel "+jo+", e="+e);
		}
	}




	private void loadAttrs(JSONObject attrs) 
	{
		loadName(attrs);

		domain = tryLoadString( attrs, "domain" );
		entity = tryLoadString( attrs, "entity" );

		filter = tryLoadString( attrs, "filter" );
		depends = tryLoadString( attrs, "depends" );
		columns = tryLoadString( attrs, "columns" );
		card = tryLoadString( attrs, "card" );

		required = tryLoadBool( attrs, "required" );		
		readonly = tryLoadBool( attrs, "readonly" );
		sortable = tryLoadBool( attrs, "sortable" );
		search = tryLoadBool( attrs, "search" );
	}


	/** Name can be on items or attrs level, so don't use tryLoad here. It will nullify field. */
	private void loadName(JSONObject jo) 
	{
		if(jo.has("name"))			
			name = jo.getString("name");
	}

	


	
	// ----------------------------------------------------------
	// Getters
	// ----------------------------------------------------------



	public String getId() {		return id;	}
	public String getName() {		return name;	}
	
	public String getDomain() {		return domain;	}
	/** Entity (table name) reference leads to. Relevant for reference (isReference()) fields.*/
	public String getEntity() {		return entity;	}
	
	public boolean isRequired() {		return required;	}
	public boolean isReadonly() {		return readonly;	}
	public boolean isSortable() {		return sortable;	}
	public boolean isSearch() {		return search;	}
	
	public String getFilter() {		return filter;	}
	public String getDepends() {		return depends;	}
	public String getColumns() {		return columns;	}
	public String getCard() {		return card;	}
	
	public Model getSubModel() {		return subModel;	}
	
	public Set<String> getValues() {		return values;	}
	public double getMin() {		return min;	}
	public double getMax() {		return max;	}
	public boolean isHaveValueStats() {		return haveValueStats;	}




	public void updateValuesStats(String fieldValue) 
	{
		// TODO must process following ones in a specific way
		// 		case "date": 		case "datetime":		case "bool":
		
		if(isNumeric())
		{
			double v = Double.parseDouble(fieldValue);
			min = Math.min(min, v);
			max = Math.min(max, v);
		}
		else
			values.add(fieldValue);

		haveValueStats = true; // At least one :)
	}



	/** 
	 * 
	 * @return true if field is numeric.
	 */
	public boolean isNumeric() {
		if(domain == null)
			return false;
		return DataConvertor.isNumericDomain(domain);
	}


	public boolean isDateOrTime() {
		if(domain == null)
			return false;
		return DataConvertor.isDateOrTime(domain);
	}

	public boolean isNonFilterable() {
		if(domain == null)
			return false;
		return DataConvertor.isNonFilterable(domain);
	}



	/** The field we represent is reference to other record. Checks <i>domain</i> field.*/
	public boolean isReference() {
		if(domain == null)
			return false;
		return DataConvertor.isReference(domain);
	}
	

}
