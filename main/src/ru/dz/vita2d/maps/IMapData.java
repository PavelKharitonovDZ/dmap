/**
 * 
 */
package ru.dz.vita2d.maps;

import javafx.scene.image.Image;
import ru.dz.vita2d.maps.def.MapTileDefinition;
import ru.dz.vita2d.maps.over.IMapAddendum;
import ru.dz.vita2d.maps.over.MapPath;


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
	
	void addOverlay(MapTileDefinition mtd, IMapData hyperlink );
	void addPath( MapPath mp );
	
	public Image putOverlays( Image in );

	IMapAddendum getOverlayByRectangle(double x, double y);
	
}
