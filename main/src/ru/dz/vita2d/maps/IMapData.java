/**
 * 
 */
package ru.dz.vita2d.maps;

import javafx.scene.image.Image;


/**
 * @author dz
 *
 * Data for map screen. Interface.
 *
 */
public interface IMapData 
{
	/**
	 * Get main view image.
	 * @return Image to display in window.
	 */
	Image	getImage();
	
	/**
	 * Window title.
	 * @return Title string.
	 */
	String getTitle();
	
	void addOverlay(String iconUrl, int x, int y, IMapData hyperlink );
	public Image putOverlays( Image in );

	MapOverlay getOverlayByRectangle(double x, double y);
	
}
