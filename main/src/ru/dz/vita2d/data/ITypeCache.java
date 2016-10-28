package ru.dz.vita2d.data;

import java.util.Set;

import ru.dz.vita2d.data.model.ModelFieldDefinition;
import ru.dz.vita2d.data.net.ServerCache;

public interface ITypeCache {

	ServerCache getServerCache();

	String getFieldName(String name);

	String getFieldType(String name);

	/**
	 * Must be called for each field value when loading table so that we
	 * can gather stats on field values.
	 * 
	 * @param fieldName field short name (id)
	 * @param fieldValue
	 */
	void updateFieldValuesStats(String fieldName, String fieldValue);

	ModelFieldDefinition getFieldModel(String fn);

	Set<String> getFieldIds();

}