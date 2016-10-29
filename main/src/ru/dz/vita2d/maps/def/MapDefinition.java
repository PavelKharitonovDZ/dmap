package ru.dz.vita2d.maps.def;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.OutoorMapData;
import ru.dz.vita2d.maps.over.MapPath;

/**
 * Map definition as loaded from json file
 * @author dz
 *
 */
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
		
		/*
		// TODO test kill me
		MapPath mp = new MapPath( "оптическая линия",
				new Point2D(1042,1272),
				new Point2D(1200,1272),
				new Point2D(1200,1162)
				);
		mp.setLayerId("cable-optics");
		mapData.addPath(mp);

		/*
		MapPath mp = new MapPath(
				new Point2D(400,400),
				new Point2D(200,100),
				new Point2D(200,200),
				new Point2D(100,200)
				);

		mapData.addPath(mp);
		*/
	}

	
	private List<MapTileDefinition> tileDefs = new LinkedList<MapTileDefinition>();
	
	public void addTileDefinition(MapTileDefinition mtd) 
	{
		tileDefs.add(mtd);		
	}

	private List<MapPathDefinition> pathDefs = new LinkedList<MapPathDefinition>();
	public void addPathDefinition(MapPathDefinition mpd) {
		pathDefs.add(mpd);
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
			mapData.addOverlay( mtd, linkedMap.getMapData() );		
		}

		for( MapPathDefinition mpd : pathDefs )
		{
			mapData.addPath(mpd.getMapPath()); 			
		}
	}

	public IMapData getMapData() {
		return mapData;
	}

	public String getName() {
		return name;		
	}

}
