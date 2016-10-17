package ru.dz.vita2d.maps;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 * Abstract implementation of map data - class that keeps internal representation
 * of a map - picture to display to user with active zones to navigate to other maps or data screens.
 * @author dz
 *
 */
public abstract class AbstractMapData implements IMapData 
{

	private Image image;
	private String title;

	public AbstractMapData(String imageUrl, String title ) 
	{
        this.title = title;
		image = new Image(imageUrl);
	}
	
	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public String getTitle() {
		return title;
	}

	List<MapOverlay> overlays = new LinkedList<MapOverlay>();
	
	public void addOverlay(MapTileDefinition mtd, IMapData hyperlink ) {
		MapOverlay mo = new MapOverlay(mtd.getFile(), mtd.getX(), mtd.getY(), hyperlink);
		mo.setTileDefinition( mtd );
		overlays.add(mo);		
	}
	
	@Override
	public Image putOverlays( Image in )
	{
		ImageView bottom = new ImageView( in );
		Group blend = new Group( bottom );
		
		for( MapOverlay o : overlays)
		{			
			blend.getChildren().add(o.getImageView());
		}
		
		//SnapshotParameters parameters = new SnapshotParameters();
        WritableImage wi = new WritableImage((int)in.getWidth(), (int)in.getHeight());
        WritableImage snapshot = blend.snapshot(new SnapshotParameters(), wi);

        return snapshot;
	}
	
	@Override
	public MapOverlay getOverlayByRectangle(double x, double y) 
	{
		for( MapOverlay o : overlays)
		{			
			boolean in = o.isInside( x, y );
			if( in )
				return o;
		}
			
		return null;
	}

}
