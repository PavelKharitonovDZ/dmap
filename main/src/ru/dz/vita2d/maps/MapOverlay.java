package ru.dz.vita2d.maps;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.dz.vita2d.data.EntityRef;
import ru.dz.vita2d.ui.anim.SpriteAnimation;

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

	private Image image;
	private ImageView iv;
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

	public void setAnimation(SpriteAnimation sa)
	{
		sa.connect(iv);
        sa.play();
	}
	
	public String getIconUrl() { 		return iconUrl;	}
	public int getX() {		return xPos;	}
	public int getY() {		return yPos;	}
	public IMapData getHyperlink() {		return hyperlink;	}

	/**
	 * <p>Returns cached instance of imageview for us - must be used once, just in 
	 * main map draw.</p>
	 * <p>If you need more instances, use getImage()</p>
	 * 
	 * @return Cached imageview.
	 * 
	 * @see MapOverlay.getImage()
	 */
	public ImageView getImageView() {		return iv;	}

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
	public EntityRef getReference()
	{
		return mtd == null ? null : mtd.getReference();
	}
	
	
}
