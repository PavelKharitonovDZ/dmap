package ru.dz.vita2d.maps.over;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.def.MapTileDefinition;
import ru.dz.vita2d.ui.anim.SpriteAnimation;

/**
 * Overlay (icon, on top image) to be put on map. Displayed and clickable.
 * @author dz
 *
 */
public class MapOverlay extends AbstractMapAddendum  
{

	private String iconUrl;
	private Image image;
	private double xSize;
	private double ySize;
	private MapTileDefinition mtd;

	
	public MapOverlay(String iconUrl, int x, int y, IMapData hyperlink ) {
		this.iconUrl = iconUrl;
		this.xPos = x;
		this.yPos = y;
		this.hyperlink = hyperlink;
		
		load();
		
	}

	public MapOverlay(MapTileDefinition mtd, IMapData hyperlink) {
		this.mtd = mtd;		

		this.iconUrl = mtd.getFile();
		this.xPos = mtd.getX();
		this.yPos = mtd.getY();
		this.hyperlink = hyperlink;
		
		load();
	}

	private void load() {
		image = new Image(iconUrl);
		iv = new ImageView( image );	
		
		iv.setX(xPos);
		iv.setY(yPos);
		
		xSize = image.getWidth();
		ySize = image.getHeight();
	}

	@Override
	public Node getContentNode() {
		return iv;
	}
	
	public void setAnimation(SpriteAnimation sa)
	{
		sa.connect(iv);
        sa.play();
	}
	
	public String getIconUrl() { 		return iconUrl;	}
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.maps.IMapAddendum#isInside(double, double)
	 */
	@Override
	public boolean isInside(double x, double y) 
	{
		
		if( (x < xPos) || (y < yPos) )	
			return false;
		
		if( (x > xPos+xSize) || (y > yPos+ySize) )	
			return false;
		
		return true;
	}

	public void setTileDefinition( MapTileDefinition mtd )
	{
		this.mtd = mtd;		
	}
	
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.maps.IMapAddendum#getTitle()
	 */
	@Override
	public String getTitle() {		
		return mtd == null ? "?" : mtd.getName();
	}

	public Image getImage() {
		return image;		
	}

	/**
	 * If tile refers to some data entity, return reference.
	 * @return
	 */
	@Override
	public IRef getReference()
	{
		return mtd == null ? null : mtd.getReference();
	}
	
	
}
