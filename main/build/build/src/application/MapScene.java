package application;

import java.io.IOException;

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.dz.vita2d.data.EntityRef;
import ru.dz.vita2d.data.ServerUnitType;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.MapOverlay;
import ru.dz.vita2d.ui.EntityFormView;

public class MapScene extends AbstractMapScene {
		
	private static final int MIN_PIXELS = 10;


	private Scene scene;
	
	private Pane info; // Right pane - must be filled with info on current object
	
	private IMapData mData; // = bigMapData;
	
	private ImageView imageView;
	private double width, height;

	private MapOverlay currentOverlay; 
	
	
	public MapScene( Stage primaryStage, Main main ) {
		super(primaryStage,main);		
	}
	

	
	
	


	
	
	@Override
	public
	void setOverviewScale() 
	{
		reset(imageView, width, height);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private HBox createButtons(double width, double height, ImageView imageView) 
	{
		Button reset = new Button("Сброс масштаба");
		reset.setOnAction(e -> reset(imageView, width / 2, height / 2));

		Button full = new Button("Обзор");
		full.setOnAction(e -> setOverviewScale());

		Button r1 = new Button("Means test");
		r1.setOnAction(e -> {
			try {
				//RestCaller.dumpJson(fieldNames);

				JSONObject mr = main.rc.getMeansRecord( 2441372 );

				JSONObject entity = mr.getJSONObject("entity");

				JsonAsFlowDialog jd = new JsonAsFlowDialog( ServerUnitType.MEANS, entity );
				//jd.setDataModel(sc.getFieldNamesMap());
				jd.setCache( main.sc.getTypeCache(ServerUnitType.MEANS) );
				jd.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} );

		//HBox buttons = new HBox(10, reset, full, m0, m1, m2, m3, r1, mroot);
		HBox buttons = new HBox(10, reset, full, r1 );
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(10));
		return buttons;
	}
	*/

	// reset to the top left:
	private void reset(ImageView imageView, double width, double height) {
		imageView.setViewport(new Rectangle2D(0, 0, width, height));
	}

	
	
	
	
	
	
	
	
	public void setMapData(IMapData mapData) 
	{
		mData = mapData;

		Image image = mData.getImage(); //new Image(IMAGE_URL);

		double width = mData.getImage().getWidth();
		double height = mData.getImage().getHeight();

		imageView = new ImageView( mData.putOverlays( image ) );
		imageView.setPreserveRatio(true);
		//reset(imageView, width / 2, height / 2);
		reset(imageView, width, height);
		
		currentOverlay = null;
		
		restart();
	}
	
	private void restart() {

		width = mData.getImage().getWidth();
		height = mData.getImage().getHeight();

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

			MapOverlay overlay = mData.getOverlayByRectangle( mouseClick.getX(), mouseClick.getY() );
			if( overlay != null )
			{
				setMapData(overlay.getHyperlink());
			}
		});

		imageView.setOnMouseMoved(e -> 
		{
			Point2D mouseClick = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

			MapOverlay overlay = mData.getOverlayByRectangle( mouseClick.getX(), mouseClick.getY() );
			if( overlay != null )
			{
				currentOverlay = overlay;
				fillInfo();
			}
		});

	
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void fillInfo() {
		info.getChildren().clear();
		
		VBox vb = new VBox(10);
		//vb.setPadding(new Insets(10));
		info.getChildren().add(vb);
		
		//vb.getChildren().clear();
		
		//vb.getChildren().add( new Label("Карта: "+mData.getTitle()) );
		
		if( currentOverlay != null )
		{
			vb.getChildren().add( new Label("Объект: "+currentOverlay.getTitle() ) );
			
			vb.getChildren().add( new ImageView( currentOverlay.getImage() ) );
			
			EntityRef ref = currentOverlay.getReference();
			if( ref != null )
			{
				ServerUnitType type = ref.getType();
				try {
					EntityFormView view = new EntityFormView(type, main.rc, main.sc.getTypeCache(type), ref.getId() );
					Pane node = view.create();
					//node.setMaxWidth(300);
					vb.getChildren().add( node );
					//VBox.setVgrow(node, Priority.ALWAYS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
			
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
	
	
	private static double clamp(double value, double min, double max) {

		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
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
