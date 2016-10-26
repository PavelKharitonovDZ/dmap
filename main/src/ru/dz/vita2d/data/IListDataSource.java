package ru.dz.vita2d.data;

import java.util.function.Consumer;

import org.json.JSONObject;

public interface IListDataSource {

	JSONObject getJson();

	void forEach(Consumer<IEntityDataSource> recordSink);

}