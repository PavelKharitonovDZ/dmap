package ru.dz.vita2d.maps;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import ru.dz.vita2d.maps.def.MapTileDefinition;
import ru.dz.vita2d.maps.over.IMapAddendum;
import ru.dz.vita2d.maps.over.MapOverlay;
import ru.dz.vita2d.maps.over.MapPath;
import ru.dz.vita2d.ui.anim.SpriteAnimation;

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

	protected List<IMapAddendum> overlays = new LinkedList<IMapAddendum>();
	
	public void addOverlay(MapTileDefinition mtd, IMapData hyperlink ) {
		//MapOverlay mo = new MapOverlay(mtd.getFile(), mtd.getX(), mtd.getY(), hyperlink);
		//mo.setTileDefinition( mtd );
		MapOverlay mo = new MapOverlay(mtd, hyperlink);
		
		SpriteAnimation sa = mtd.getSpriteAnimation();
		if( sa != null) mo.setAnimation(sa);
		
		overlays.add(mo);		
	}

	public void addPath( MapPath mp )
	{
		overlays.add(mp);
	}
	
	@Override
	public Image putOverlays( Image in )
	{
		ImageView bottom = new ImageView( in );
		Group blend = new Group( bottom );
		
		for( IMapAddendum o : overlays)
		{			
			if(o.isLayerEnabled())
			{
				//blend.getChildren().add(o.getImageView());
				blend.getChildren().add(o.getContentNode());
			}
		}
		
		//SnapshotParameters parameters = new SnapshotParameters();
        WritableImage wi = new WritableImage((int)in.getWidth(), (int)in.getHeight());
        WritableImage snapshot = blend.snapshot(new SnapshotParameters(), wi);

        return snapshot;
	}
	
	@Override
	public IMapAddendum getOverlayByRectangle(double x, double y) 
	{
		for( IMapAddendum o : overlays)
		{			
			boolean in = o.isInside( x, y );
			if( in )
				return o;
		}
			
		return null;
	}

}
