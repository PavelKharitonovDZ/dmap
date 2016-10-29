package ru.dz.vita2d.maps.def;

import org.json.JSONObject;

import javafx.util.Duration;
import ru.dz.vita2d.data.ref.EntityRef;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;
import ru.dz.vita2d.ui.anim.SpriteAnimation;

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
		private SpriteAnimation spriteAnimation;
		private IRef reference;

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
			if( tile.has("anim-file-base") )
			{
				String afile = tile.getString("anim-file-base");
				int acount = tile.getInt("anim-file-count");
				
				spriteAnimation = new SpriteAnimation( new Duration(1000), acount, afile);
			}
			
			if( tile.has("ref-unit-type") )
			{
				String reftype = tile.getString("ref-unit-type");
				int refid = tile.getInt("ref-unit-id");
				reference = new UnitRef(reftype, refid); 
			}

			if( tile.has("ref-entity-type") )
			{
				String reftype = tile.getString("ref-entity-type");
				int refid = tile.getInt("ref-entity-id");
				reference = new EntityRef(reftype, refid); 
			}
		}

		public String getLinkId() {			return linkId;		}
		public String getName() {			return name;		}
		public String getFile() {			return file;		}
		public int getX() {			return x;		}
		public int getY() {			return y;		}		
		public SpriteAnimation getSpriteAnimation() {			return spriteAnimation;		}
		public IRef getReference() {			return reference;		}

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

