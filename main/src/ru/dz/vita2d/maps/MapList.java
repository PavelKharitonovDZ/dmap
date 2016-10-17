package ru.dz.vita2d.maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import application.MapScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ru.dz.vita2d.data.RestCaller;

/**
 * List of all maps - loaded from JSON file
 * @author dz
 *
 */
public class MapList 
{
	//List<MapDefinition> mapDefs = new LinkedList<MapDefinition>();
	Map<String,MapDefinition> mapDefs = new HashMap<String,MapDefinition>();
	private String mainMapId;
	private MapDefinition rootMap;


	public MapList() {

		//URL mapsFile = new URL("") 

		File file = new File("maps.json");


		try {

			JSONTokener loader = new JSONTokener(new FileInputStream( file));
			JSONObject top = new JSONObject(loader);

			//RestCaller.dumpJson(top);

			mainMapId = top.getString("main-id");

			JSONArray list = top.getJSONArray("maps");
			list.forEach( map -> loadMap(map) );

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for( MapDefinition md : mapDefs.values() )
		{
			md.resolveLinks( this );
		}

		for( MapDefinition md : mapDefs.values() )
		{
			md.generateInternalData();
		}

		rootMap = findMapById(mainMapId);
		if(rootMap == null)
			System.out.println("main-id refers to undefinded map id "+mainMapId);

	}

	public MapDefinition findMapById(String linkId) {
		return mapDefs.get(linkId);
	}

	private Object loadMap(Object omap) {
		if (omap instanceof JSONObject) 
		{
			try {
				JSONObject map = (JSONObject) omap;

				// TODO attrs/type=outdoor/indoor/device
				String name = map.getString("name");
				String id = map.getString("id");
				String file = map.getString("file");

				MapDefinition md = new MapDefinition(id, name, file);

				try {
					JSONArray tiles = map.getJSONArray("tiles");
					//loadTiles( md, tiles );
					tiles.forEach( tile -> loadTile((JSONObject)tile, md) );		
				}
				catch(JSONException je)
				{
					System.out.println("(ignored) JSON exception loading map tiles: "+je);
				}

				mapDefs.put(id,md);
			}
			catch(Throwable e)
			{
				System.out.println("(ignored) JSON exception loading map: "+omap+"\nException: "+e);
			}
			return null;
		}
		System.out.println("unknown type "+omap.getClass());
		return null;
	}



	private Object loadTile(JSONObject tile, MapDefinition md) 
	{

		MapTileDefinition mtd = new MapTileDefinition(tile);
		md.addTileDefinition( mtd );

		return null;
	}

	private void dump() {
		for( MapDefinition md : mapDefs.values() )
		{
			md.dump();			
		}
	}

	public IMapData getRootMap() {
		return rootMap.getMapData();
	}

	public void fillMapsMenu(Menu navMaps, MapScene mapScene) 
	{
		for( MapDefinition md : mapDefs.values() )
		{
			IMapData mdata = md.getMapData();
			String name = md.getName();
			
			MenuItem mi = new MenuItem(name);
			mi.setOnAction(actionEvent -> mapScene.setMapData(mdata));

			navMaps.getItems().add(mi);
		}
	}



	public static void main(String[] args) 
	{
		MapList ml = new MapList();
		ml.dump();
	}


}
