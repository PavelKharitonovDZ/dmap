package ru.dz.vita2d.maps;

import org.json.JSONObject;

/**
 * Tile (Overlay) definition as loaded from JSON file. 
 * TODO name is not too good - there possibly will be tiles to combine map itself from. 
 * @author dz
 *
 */
public class MapTileDefinition {

		private String linkId;
		private String name;
		private String file;
		private int x;
		private int y;
		private MapDefinition mapDefinition;

		/*
		public MapTileDefinition( String id, String name, String file ) 
		{
			this.id = id;
			this.name = name;
			this.file = file;
			
		}
		*/
		
		public MapTileDefinition(JSONObject tile) 
		{
			if( tile.has("link-id") )
				linkId = tile.getString("link-id");
			else
				linkId = "";
			
			name = tile.getString("name");
			file = tile.getString("file");
			
			x = tile.getInt("x");
			y = tile.getInt("y");
			
			//MapOverlay mo = new MapOverlay(file, x, y, hyperlink)
			
		}

		public String getLinkId() {
			return linkId;
		}

		public String getName() {
			return name;
		}

		public String getFile() {
			return file;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
		
		@Override
		public String toString() {
			return name+"@"+file;
		}

		public void setLinkedMap(MapDefinition map) {
			this.mapDefinition = map;
		}

		public MapDefinition getLinkedMap() {
			return mapDefinition;			
		}
		
	}

