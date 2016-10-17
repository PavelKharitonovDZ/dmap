package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// http://sv-web-15.vtsft.ru/orvd-release/index/#
// http://sv-web-15.vtsft.ru/orvd-test/index/#

import javafx.application.Application;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * 
 * mediaPlayer = new MediaPlayer(hit);


            mediaPlayer.setOnPlaying(() -> {

                FillTransition ft = new FillTransition(Duration.millis(seconds * 1000), rect, Color.TRANSPARENT, Color.GREEN);
                ft.setCycleCount(2);
                ft.setAutoReverse(true);

                ft.play();
            });

            mediaPlayer.play();

 * 
 */

public class Main extends Application {

	//private static final String IMAGE_CREDIT_URL = "http://www.nasa.gov/image-feature/global-mosaic-of-pluto-in-true-color";
	private static final String HOME_URL = "http://dz.ru";
	//private static final String IMAGE_URL = "orl_a_map.png";
	private static final int MIN_PIXELS = 10;

	Scene scene;
	ImageView imageView;
	double width, height; 

	OutoorMapData bigMapData = new OutoorMapData("map.png", "Карта объекта");
	OutoorMapData mainMapData = new OutoorMapData("orl_a_map.png", "Карта ОРЛ-А");
	IndoorMapData mainPlanData = new IndoorMapData("plan.png", "План здания");
	OutoorMapData deviceData = new OutoorMapData("device_00.png", "Щиток"); // TODO NonMapData

	{
		bigMapData.addOverlay( "orl-a-icon.png", 926, 1205, mainMapData );
	}

	IMapData mData = bigMapData;
	private Stage primaryStage;

	private RestCaller rc;
	private ServerCache sc;

	@Override
	public void init() throws Exception {
		super.init();

		//rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-test");
		rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");
		sc = new ServerCache(rc);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		logout();
	}    

	private void logout() {
		//LoginScene ls = 
		new LoginScene(rc, primaryStage, this );    	
	}

	public void afterLogin() {
		// Resets Scene too
		setMapData(bigMapData);
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


		HBox buttons = createButtons(width, height, imageView);
		Tooltip tooltip = new Tooltip("Scroll to zoom, drag to pan");
		Tooltip.install(buttons, tooltip);

		Pane container = new Pane(imageView);
		container.setPrefSize(800, 600);

		imageView.fitWidthProperty().bind(container.widthProperty());
		imageView.fitHeightProperty().bind(container.heightProperty());

		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		fillMenu(menuBar);

		//HBox links = createLinks();

		//VBox root = new VBox(menuBar, links, container, buttons);
		VBox root = new VBox(menuBar, container, buttons);
		root.setFillWidth(true);
		VBox.setVgrow(container, Priority.ALWAYS);

		scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle( "ОРВД: " + mData.getTitle() ); //"ОРВД - Планшет Инженера");
		primaryStage.show();
	}

	private void fillMenu(MenuBar menuBar) {
		// File menu - new, save, exit
		Menu fileMenu = new Menu("Файл");

		MenuItem loginMenuItem = new MenuItem("Сменить пользователя");
		loginMenuItem.setOnAction(actionEvent -> logout());

		MenuItem exitMenuItem = new MenuItem("Выход (выключить планшет)");
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());
		exitMenuItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));


		fileMenu.getItems().addAll( loginMenuItem,
				new SeparatorMenuItem(), exitMenuItem);



		Menu navMenu = new Menu("Навигация");

		MenuItem navHomeMap = new MenuItem("На общую карту");
		navHomeMap.setOnAction(actionEvent -> setMapData(bigMapData));

		MenuItem navOverview = new MenuItem("Обзор");
		navOverview.setOnAction(actionEvent -> setOverviewScale());

		navMenu.getItems().addAll( navHomeMap, new SeparatorMenuItem(), navOverview );

		/*
	    Menu webMenu = new Menu("Web");
	    CheckMenuItem htmlMenuItem = new CheckMenuItem("HTML");
	    htmlMenuItem.setSelected(true);
	    webMenu.getItems().add(htmlMenuItem);

	    CheckMenuItem cssMenuItem = new CheckMenuItem("CSS");
	    cssMenuItem.setSelected(true);
	    webMenu.getItems().add(cssMenuItem);

	    Menu sqlMenu = new Menu("SQL");
	    ToggleGroup tGroup = new ToggleGroup();
	    RadioMenuItem mysqlItem = new RadioMenuItem("MySQL");
	    mysqlItem.setToggleGroup(tGroup);

	    RadioMenuItem oracleItem = new RadioMenuItem("Oracle");
	    oracleItem.setToggleGroup(tGroup);
	    oracleItem.setSelected(true);

	    sqlMenu.getItems().addAll(mysqlItem, oracleItem,
	        new SeparatorMenuItem());

	    Menu tutorialManeu = new Menu("Tutorial");
	    tutorialManeu.getItems().addAll(
	        new CheckMenuItem("Java"),
	        new CheckMenuItem("JavaFX"),
	        new CheckMenuItem("Swing"));

	    sqlMenu.getItems().add(tutorialManeu);
		 */

		Menu aboutMenu = new Menu("О системе");

		MenuItem version = new MenuItem("Версия");
		version.setOnAction(actionEvent -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Версия системы");
			alert.setHeaderText("ОРВД Планшет Инженера");

			String s =
					"Версия фронтального приложения: 0.1\n"+
							"Версия сервера: "+rc.getServerVersion()+"\n"+
							"Точка запуска: "+getHostServices().getCodeBase()+"\n"+
							"Базовая ссылка: "+getHostServices().getDocumentBase()+"\n" +
							"Пользователь: "+rc.getLoggedInUser()+"\n"
							;

			alert.setContentText(s);
			alert.show();
		});

		MenuItem aboutDz = new MenuItem("Digital Zone");
		aboutDz.setOnAction(actionEvent -> getHostServices().showDocument(HOME_URL));

		MenuItem aboutVita = new MenuItem("VitaSoft");
		aboutVita.setOnAction(actionEvent -> getHostServices().showDocument("vtsft.ru"));

		aboutMenu.getItems().addAll( version, new SeparatorMenuItem(), aboutDz, aboutVita );

		// --------------- Menu bar

		//menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);
		menuBar.getMenus().addAll(fileMenu, navMenu, aboutMenu );
	}


	private void setOverviewScale() 
	{
		reset(imageView, width, height);
	}

	/*
	private HBox createLinks() 
	{
		Hyperlink link = new Hyperlink("Digital Zone");
        link.setOnAction(e -> getHostServices().showDocument(HOME_URL));

        link.setPadding(new Insets(10));
        link.setTooltip(new Tooltip(HOME_URL));

        Hyperlink link2 = new Hyperlink("VitaSoft");
        link2.setOnAction(e -> getHostServices().showDocument("vtsft.ru"));
        link2.setPadding(new Insets(10));
        //link2.setTooltip(new Tooltip(HOME_URL));

        Button logout = new Button("Сменить пользователя");
        logout.setOnAction(e -> logout());

        HBox links = new HBox(link, link2, logout );
		return links;
	}
	 */
	private HBox createButtons(double width, double height, ImageView imageView) 
	{
		Button reset = new Button("Сброс масштаба");
		reset.setOnAction(e -> reset(imageView, width / 2, height / 2));

		Button full = new Button("Обзор");
		full.setOnAction(e -> setOverviewScale());

		Button m0 = new Button("Общая карта");
		m0.setOnAction(e -> setMapData(bigMapData));        

		Button m1 = new Button("Карта объекта");
		m1.setOnAction(e -> setMapData(mainMapData));

		Button m2 = new Button("Строение");
		m2.setOnAction(e -> setMapData(mainPlanData) );

		Button m3 = new Button("Щиток");
		m3.setOnAction(e -> setMapData(deviceData) );

		//Button s1 = new Button("Beep");
		//s1.setOnAction(e -> { new AudioClip(getResource("click.wav").toString()).play(); } );

		Button r1 = new Button("Means test");
		r1.setOnAction(e -> {
			try {
				//RestCaller.dumpJson(fieldNames);

				JSONObject mr = rc.getMeansRecord( 2441372 );

				JSONObject entity = mr.getJSONObject("entity");

				JsonAsFlowDialog jd = new JsonAsFlowDialog( entity );
				//jd.setDataModel(sc.getFieldNamesMap());
				jd.setServerCache( sc );
				jd.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} );





		HBox buttons = new HBox(10, reset, full, m0, m1, m2, m3, r1);
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(10));
		return buttons;
	}

	private void setMapData(IMapData mapData) 
	{
		mData = mapData;

		Image image = mData.getImage(); //new Image(IMAGE_URL);

		double width = mData.getImage().getWidth();
		double height = mData.getImage().getHeight();

		imageView = new ImageView( mData.putOverlays( image ) );
		imageView.setPreserveRatio(true);
		//reset(imageView, width / 2, height / 2);
		reset(imageView, width, height);
		/*
    	if( scene != null )
    		scene.getRoot().requestLayout(); // hope it will repaint us?
		 */
		restart();
	}

	// reset to the top left:
	private void reset(ImageView imageView, double width, double height) {
		imageView.setViewport(new Rectangle2D(0, 0, width, height));
	}

	// shift the viewport of the imageView by the specified delta, clamping so
	// the viewport does not move off the actual image:
	private void shift(ImageView imageView, Point2D delta) {
		Rectangle2D viewport = imageView.getViewport();

		double width = imageView.getImage().getWidth() ;
		double height = imageView.getImage().getHeight() ;

		double maxX = width - viewport.getWidth();
		double maxY = height - viewport.getHeight();

		double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
		double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

		imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
	}

	private double clamp(double value, double min, double max) {

		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	// convert mouse coordinates in the imageView to coordinates in the actual image:
	private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
		double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
		double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

		Rectangle2D viewport = imageView.getViewport();
		return new Point2D(
				viewport.getMinX() + xProportion * viewport.getWidth(), 
				viewport.getMinY() + yProportion * viewport.getHeight());
	}

	public static void main(String[] args) {
		launch(args);
	}

}