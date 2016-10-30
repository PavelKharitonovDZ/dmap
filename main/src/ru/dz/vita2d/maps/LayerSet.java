package ru.dz.vita2d.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.paint.Color;

/**
 * <p>Set of map layers.</p>
 * <p>Used to store layer-specific info and turn layers on/off.</p>
 * 
 * @author dz
 *
 */
public class LayerSet 
{
	static private List<MapLayer> layers = new ArrayList<>();

	static {
		// TODO get colors from config?
		layers.add( new MapLayer("cable-optics", "Оптические лини связи", Color.BURLYWOOD.toString(), 10) );
		layers.add( new MapLayer("cable-copper", "Проводные лини связи", Color.GREEN.toString(), 11) );
		layers.add( new MapLayer("maintenance-path", "Маршруты обхода", Color.MEDIUMORCHID.toString(), 12) );
	}

	static public void forEach(Consumer<MapLayer> action)
	{
		layers.forEach(action);
	}
	
	static public MapLayer findById(String id)
	{
		for( MapLayer l : layers )
		{
			if(l.getId().equalsIgnoreCase(id))
				return l;
		}
		
		System.out.println("Unknown map layer id: "+id);
		return null;
	}

}
