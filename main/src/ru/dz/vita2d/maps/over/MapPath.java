package ru.dz.vita2d.maps.over;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.ref.UnitRef;

/**
 * Used to draw communication lines on the map.
 * @author dz
 *
 */
public class MapPath extends AbstractMapAddendum {
	private final static int LINE_WIDTH = 4;
	
	//private List<Point2D> points = new ArrayList<>();
	private int xSize;
	private int ySize;
	private String title = ""; 

	double[] xPoints;
	double[] yPoints;
	
	public MapPath(Point2D... points) {
		xPoints = new double[points.length];
		yPoints = new double[points.length];
		
		/*
		for(Point2D p : points )
		{
			//this.points.add(p);
		}*/
		
		for( int i = 0; i < points.length; i++ )
		{
			xPoints[i] = points[i].getX();
			yPoints[i] = points[i].getY();
		}
		
		title = "оптическая линия связи";
		
	}

	/*
	public ImageView getImageView() {
		if( iv == null )
		{
			Image im = paintMe();
			iv = new ImageView(im);
		}
		return iv;	
	}
	*/
	
	public Node getContentNode()
	{
		calcSize(); // TODO don't need each time?

		Canvas c = new Canvas(xPos+xSize+LINE_WIDTH*2,yPos+ySize+LINE_WIDTH*2);
		
		// We're off? Don't paint us.
		if(!ml.isEnabled()) 
			return c;
		
		GraphicsContext gc = c.getGraphicsContext2D();
		
		gc.setLineWidth(LINE_WIDTH);
		gc.setLineJoin(StrokeLineJoin.ROUND);
		gc.setLineCap(StrokeLineCap.ROUND);
		
		//gc.setStroke(Color.BURLYWOOD);
		//gc.setFill(Color.RED);

		gc.setStroke( (ml == null) ? Color.RED : ml.getColor() );
		
		gc.strokePolyline(xPoints, yPoints, xPoints.length);
		
		/*
		Point2D lastPoint = null;

		for( Point2D p : points )
		{
			if( lastPoint != null )
			{
				//gc.lineTo(p.getX(), p.getY());
				gc.strokeLine(lastPoint.getX(), lastPoint.getY(), p.getX(), p.getY());
			}
			//else				gc.moveTo(p.getX(), p.getY());

			lastPoint = p;
		}
		*/
		//gc.applyEffect(new DropShadow(3, 5, 5, Color.WHITE));
		
		return c;
	}

	@Override
	public boolean isInside(double x, double y) 
	{
		if(!ml.isEnabled()) 
			return false;
		
		if( (x < xPos) || (y < yPos))			
			return false;

		if( (x > xPos+xSize) || (y > yPos+ySize))			
			return false;

		

		for( int i = 1; i < xPoints.length; i++ )
		{
			java.awt.geom.Line2D l = new java.awt.geom.Line2D.Double(
					xPoints[i-1], yPoints[i-1], 
					xPoints[i], yPoints[i]
					);

			double dist = l.ptSegDist(x,y);
			//System.out.println("dist="+dist);
			if( dist < 16 )
				return true;
		}		
		
		//return true;
		return false;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public IRef getReference() {
		return null;
	}

	/*
	private Image paintMe()
	{
		calcSize(); // TODO don't need each time?
		//ImageView bottom = new ImageView( in );
		
		//Group blend = new Group(  );

		Canvas c = new Canvas(xPos+xSize,yPos+ySize);
		//c.setOpacity(30);
		
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.setLineWidth(5);
		
		gc.setStroke(Color.BLUE);
		gc.setFill(Color.RED);
		
		//gc.clearRect(xPos, yPos, xSize, ySize);
		
		//blend.getChildren().add(c);

		Point2D lastPoint = null;

		for( Point2D p : points )
		{
			if( lastPoint != null )
			{
				gc.lineTo(p.getX(), p.getY());
			}
			else
				gc.moveTo(p.getX(), p.getY());

			lastPoint = p;
		}



		WritableImage wi = new WritableImage(xPos+xSize, yPos+ySize);
		//WritableImage snapshot = blend.snapshot(new SnapshotParameters(), wi);
		SnapshotParameters sp = new SnapshotParameters();
		//sp.setFill(Color.TRANSPARENT);
		WritableImage snapshot = c.snapshot(sp, wi);

		return snapshot;

	}
	*/
	private void calcSize() {

		double minx, miny, maxx, maxy;
		/*
		minx = maxx = points.get(0).getX();
		miny = maxy = points.get(0).getY();

		for( Point2D p : points )
		{
			minx = Math.min(minx, p.getX() );
			miny = Math.min(miny, p.getY() );

			maxx = Math.max(maxx, p.getX() );
			maxy = Math.max(maxy, p.getY() );
		}
		*/
		
		minx = maxx = xPoints[0];
		miny = maxy = yPoints[0];

		for( int i = 1; i < xPoints.length; i++ )
		{
			minx = Math.min(minx, xPoints[i] );
			miny = Math.min(miny, yPoints[i] );

			maxx = Math.max(maxx, xPoints[i] );
			maxy = Math.max(maxy, yPoints[i] );
		}
		

		xPos = (int) minx;
		yPos = (int) miny;

		xSize = (int) (maxx - minx);
		ySize = (int) (maxy - miny);

	}



	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}


}
