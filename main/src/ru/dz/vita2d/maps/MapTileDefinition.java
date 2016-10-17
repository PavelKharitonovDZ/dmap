package ru.dz.vita2d.maps;

import org.json.JSONObject;

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
			name = tile.getString("name");
			linkId = tile.getString("link-id");
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

