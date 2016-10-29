package ru.dz.vita2d.maps.over;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.LayerSet;

public interface IMapAddendum {

	int getX();

	int getY();

	/** 
	 * 
	 * @return True if x,y is inside this addendum's picture.
	 */
	boolean isInside(double x, double y);

	/**
	 * 
	 * @return Title of object, for displaying on mouseover.
	 */
	String getTitle();

	/**
	 * 
	 * @return Image that represents this overlay, for displaying on mouseover.
	 */
	Image getImage();
	
	/**
	 * 
	 * @return Reference to DB object to show on mouseover or null.
	 */
	IRef getReference();
	
	/** 
	 * 
	 * @return Link to map to switch to or null.
	 */
	IMapData getHyperlink();
	
	/**
	 * <p>Returns cached instance of imageview for us - must be used once, just in 
	 * main map draw.</p>
	 * <p>If you need more instances, use getImage()</p>
	 * 
	 * @return Cached imageview.
	 * 
	 * @see MapOverlay.getImage()
	 */
	//ImageView getImageView();

	Node getContentNode();

	/** Is layer we belong to enabled. */
	boolean isLayerEnabled();

	/** Set id of layer we belong to. */
	void setLayerId(String layerId);
}