package ru.dz.vita2d.maps;

import java.util.LinkedList;
import java.util.List;

public class MapDefinition {

	private String id;
	private String name;
	private String file;

	private IMapData mapData; 
	
	public MapDefinition( String id, String name, String file ) 
	{
		this.id = id;
		this.name = name;
		this.file = file;
		mapData = new OutoorMapData(file, name);
		
	}

	
	private List<MapTileDefinition> tileDefs = new LinkedList<MapTileDefinition>();
	
	public void addTileDefinition(MapTileDefinition mtd) 
	{
		tileDefs.add(mtd);		
	}

	public void resolveLinks(MapList mapList) 
	{
		for( MapTileDefinition mtd : tileDefs )
		{
			String linkId = mtd.getLinkId();
			MapDefinition map = mapList.findMapById( linkId );
			if( map == null )
			{
				System.out.println("tile "+mtd+" refers to undefinded map "+linkId);
			}
			
			mtd.setLinkedMap(map);
		}
	}	
	
	@Override
	public String toString() {
		return name+"@"+file+", id = "+id;
	}
	
	
	public void dump() 
	{
		System.out.println(this);		
		for( MapTileDefinition mtd : tileDefs )
		{
			System.out.println("\t"+mtd);		
		}
	}

	public void generateInternalData() 
	{
		// TODO choose class?
		for( MapTileDefinition mtd : tileDefs )
		{
			MapDefinition linkedMap = mtd.getLinkedMap();
			mapData.addOverlay(mtd.getFile(), mtd.getX(), mtd.getY(), linkedMap.getMapData() );		
		}
		
	}

	public IMapData getMapData() {
		return mapData;
	}
}
