package ru.dz.vita2d.maps;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Overlay (icon, on top image) to be put on map. Displayed and clickable.
 * @author dz
 *
 */
public class MapOverlay 
{

	private String iconUrl;
	private int xPos;
	private int yPos;
	private IMapData hyperlink;

	private ImageView iv;
	private final double xSize;
	private final double ySize;

	
	public MapOverlay(String iconUrl, int x, int y, IMapData hyperlink ) {
		this.iconUrl = iconUrl;
		this.xPos = x;
		this.yPos = y;
		this.hyperlink = hyperlink;
		
		Image image = new Image(iconUrl);
		iv = new ImageView( image );	
		
		iv.setX(x);
		iv.setY(y);
		
		xSize = image.getWidth();
		ySize = image.getHeight();
		
	}

	public String getIconUrl() { 		return iconUrl;	}
	public int getX() {		return xPos;	}
	public int getY() {		return yPos;	}
	public IMapData getHyperlink() {		return hyperlink;	}

	public ImageView getImageView() {		return iv;	}

	public boolean isInside(double x, double y) 
	{
		
		if( (x < xPos) || (y < yPos) )	
			return false;
		
		if( (x > xPos+xSize) || (y > yPos+ySize) )	
			return false;
		
		return true;
	}
	
	
	
}
