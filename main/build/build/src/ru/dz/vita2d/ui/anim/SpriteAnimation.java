package ru.dz.vita2d.ui.anim;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.Event;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition 
{

	private ImageView iv = null;
	//private Duration duration;
	private int count;

	private Image[]	images;
	
	public SpriteAnimation( Duration duration, int count, String iconUrlBase) {
		super();
		//this.iv = iv;
		//this.duration = duration;
		this.count = count;
		
		setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
		setCycleCount(Animation.INDEFINITE);

        images = new Image[count];
        
        for(int i = 0; i < count; i++)
        {
    		//images[count] = new Image(String.format("%s%05d", iconUrlBase, i ));
    		String fn = String.format(iconUrlBase, i );
			images[i] = new Image(fn);
			//System.out.println("fn index="+i+" is "+fn);
        }
               
	}
	
	public void connect(ImageView iv)
	{
		this.iv = iv;
        iv.setCache(false);
        iv.setImage(images[0]);		
	}
	
	private int lastIndex = -1;
	
	@Override
	protected void interpolate(double k) 
	{
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        
        if (index != lastIndex) 
        {
        	if( iv != null )
        		iv.setImage(images[index]);
            lastIndex = index;
            //System.out.println("ani index="+index);
            //iv.setTranslateX(1);            
            //iv.setTranslateX(0);            
            //iv.setViewport(new Rectangle2D(0, 0, images[index].getWidth(), images[index].getHeight()));
            
            //iv.getTranslateX()
        }
    }
	
}
