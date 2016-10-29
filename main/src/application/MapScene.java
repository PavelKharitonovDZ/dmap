package application;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.over.IMapAddendum;
import ru.dz.vita2d.maps.over.MapOverlay;

public class MapScene extends AbstractMapScene {

	private static final int MIN_PIXELS = 10;




	private IMapData mData; // = bigMapData;

	/** Main map image. */
	private Image image; 
	private ImageView imageView;
	private double width, height;



	public MapScene( Stage primaryStage, Main main ) {
		super(primaryStage,main);		
	}









	@Override
	public
	void setOverviewScale() 
	{
		reset(imageView, width, height);
	}

	/** reset to the top left. */
	private void reset(ImageView imageView, double width, double height) {
		imageView.setViewport(new Rectangle2D(0, 0, width, height));
	}









	public void setMapData(IMapData mapData) 
	{
		mData = mapData;

		image = mData.getImage(); //new Image(IMAGE_URL);

		double width = mData.getImage().getWidth();
		double height = mData.getImage().getHeight();

		//imageView = new ImageView( mData.putOverlays( image ) );
		imageView = new ImageView();
		reOverlay();
		
		imageView.setPreserveRatio(true);
		//reset(imageView, width / 2, height / 2);
		reset(imageView, width, height);

		if(Defs.animationEnabled())
		{
			AnimationTimer at = new AnimationTimer() {			
				@Override
				public void handle(long now) {
					reOverlay();				
				}
			};
			at.start();
		}

		currentOverlay = null;


		width = mData.getImage().getWidth();
		height = mData.getImage().getHeight();

		connectActions();


		Pane container = new Pane(imageView);
		if(!Defs.FULL_SCREEN)
		{
			container.setPrefSize(800, 600);
			//container.setPrefSize(1400, 800);
			//container.setMinSize(900, 800);
		}

		info = new Pane();
		if(!Defs.FULL_SCREEN)
			info.setPrefSize(400, 600);
		info.setPadding(new Insets(20)); // TODO wrong
		fillInfo();

		HBox mapAndInfo = new HBox(10, container, info);

		imageView.fitWidthProperty().bind(container.widthProperty());
		imageView.fitHeightProperty().bind(container.heightProperty());

		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		fillMenu(menuBar);


		VBox root = new VBox(menuBar, mapAndInfo);
		root.setFillWidth(true);
		VBox.setVgrow(container, Priority.ALWAYS);

		scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle( "ОРВД: " + mData.getTitle() ); //"ОРВД - Планшет Инженера");
		//primaryStage.show();
	}









	private void connectActions() {
		ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

		imageView.setOnMousePressed(e -> {            
			Point2D mousePress = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
			mouseDown.set(mousePress);
			//System.out.println("press @ x=" + mousePress.getX()+" y=" + mousePress.getY());
		});

		imageView.setOnMouseDragged(e -> {
			Point2D dragPoint = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
			shift(imageView, dragPoint.subtract(mouseDown.get()));
			mouseDown.set(imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
		});

		imageView.setOnScroll(e -> {
			double delta = e.getDeltaY();
			Rectangle2D viewport = imageView.getViewport();

			double scale = clamp(Math.pow(1.01, delta),

					// don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
					Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

					// don't scale so that we're bigger than image dimensions:
					Math.max(width / viewport.getWidth(), height / viewport.getHeight())

					);

			Point2D mouse = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

			double newWidth = viewport.getWidth() * scale;
			double newHeight = viewport.getHeight() * scale;

			// To keep the visual point under the mouse from moving, we need
			// (x - newViewportMinX) / (x - currentViewportMinX) = scale
			// where x is the mouse X coordinate in the image

			// solving this for newViewportMinX gives

			// newViewportMinX = x - (x - currentViewportMinX) * scale 

			// we then clamp this value so the image never scrolls out
			// of the imageview:

			double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 
					0, width - newWidth);
			double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 
					0, height - newHeight);

			imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
		});

		imageView.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) 
			{
				reset(imageView, width, height);            
			}

			Point2D mouseClick = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
			System.out.println("click @ x=" + mouseClick.getX()+" y=" + mouseClick.getY());

			IMapAddendum overlay = mData.getOverlayByRectangle( mouseClick.getX(), mouseClick.getY() );
			IMapData link =  (overlay != null) ? overlay.getHyperlink() : null;
			if( (overlay != null) && (link != null) )
				setMapData(link);
		});

		imageView.setOnMouseMoved(e -> 
		{
			Point2D mouseClick = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

			IMapAddendum overlay = mData.getOverlayByRectangle( mouseClick.getX(), mouseClick.getY() );
			if( overlay != null )
			{
				currentOverlay = overlay;
				fillInfo();
			}
		});
	}


























	/** Recombine image by puting overlays on it. */
	@Override
	protected void reOverlay() {
		imageView.setImage( mData.putOverlays( image ) );
	}









	// shift the viewport of the imageView by the specified delta, clamping so
	// the viewport does not move off the actual image:
	private static void shift(ImageView imageView, Point2D delta) {
		Rectangle2D viewport = imageView.getViewport();

		double width = imageView.getImage().getWidth() ;
		double height = imageView.getImage().getHeight() ;

		double maxX = width - viewport.getWidth();
		double maxY = height - viewport.getHeight();

		double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
		double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

		imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
	}


	// convert mouse coordinates in the imageView to coordinates in the actual image:
	private static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
		double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
		double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

		Rectangle2D viewport = imageView.getViewport();
		return new Point2D(
				viewport.getMinX() + xProportion * viewport.getWidth(), 
				viewport.getMinY() + yProportion * viewport.getHeight());
	}


}
