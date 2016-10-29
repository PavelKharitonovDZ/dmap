package ru.dz.vita2d.maps.def;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Point2D;
import ru.dz.vita2d.data.model.JsonParser;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;
import ru.dz.vita2d.maps.over.MapPath;

public class MapPathDefinition extends JsonParser{

	private String layerId;
	private String title;
	private IRef reference;
	private List<Point2D> pointsList;

	public MapPathDefinition(JSONObject path) {

		layerId = tryLoadString( path, "layer-id" );
		title = tryLoadString( path, "title" );

		if( path.has("ref-unit-type") )
		{
			String reftype = path.getString("ref-unit-type");
			int refid = path.getInt("ref-unit-id");
			reference = new UnitRef(reftype, refid); // TODO IRef? IRef.fromString()? 
		}

		pointsList = new ArrayList<>();
		JSONArray points = path.getJSONArray("points");
		for( Object point: points )
		{
			JSONObject p = (JSONObject) point;
			int x = p.getInt("x");
			int y = p.getInt("y");
			pointsList.add(new Point2D(x, y));
		}
		
		System.out.println("Path "+title+" layer="+layerId);
	}

	public MapPath getMapPath() {
		MapPath mp = new MapPath(title, pointsList);
		mp.setLayerId(layerId);
		mp.setReference(reference);
		return mp;
	}

}
