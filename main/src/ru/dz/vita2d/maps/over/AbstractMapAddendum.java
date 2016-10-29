package ru.dz.vita2d.maps.over;

import javafx.scene.image.ImageView;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.LayerSet;
import ru.dz.vita2d.maps.MapLayer;

public abstract class AbstractMapAddendum implements IMapAddendum {

	protected int xPos;
	protected int yPos;
	protected ImageView iv = null;
	protected IMapData hyperlink;
	protected String layerId;
	protected MapLayer ml;

	public AbstractMapAddendum() {
		super();
	}

	@Override
	public int getX() {		return xPos;	}

	@Override
	public int getY() {		return yPos;	}

	public ImageView getImageView() {		return iv;	}

	public IMapData getHyperlink() {		return hyperlink;	}

	@Override
	public void setLayerId( String layerId )
	{
		this.layerId = layerId;
		ml = LayerSet.findById(layerId);
	}
	
	/** Is layer we belong to enabled. */
	@Override
	public boolean isLayerEnabled() 
	{  
		if( ml == null ) return true;
		return ml.isEnabled();
	}
}