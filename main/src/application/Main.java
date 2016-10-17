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
import media.Player;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.maps.DeviceMapData;
import ru.dz.vita2d.maps.IMapData;
import ru.dz.vita2d.maps.IndoorMapData;
import ru.dz.vita2d.maps.MapList;
import ru.dz.vita2d.maps.MapOverlay;
import ru.dz.vita2d.maps.OutoorMapData;


public class Main extends Application 
{

	private Stage primaryStage;

	public RestCaller rc;
	public ServerCache sc;
	public MapList ml;
	
	@Override
	public void init() throws Exception {
		super.init();

		//rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-test");
		rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");
		sc = new ServerCache(rc);
	
		// Load data from local file with list of map
		ml = new MapList();

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		logout();
	}    

	public void logout() {
		//LoginScene ls = 
		new LoginScene(rc, primaryStage, this );    	
	}

	public void afterLogin() {
		// Resets Scene 
		MapScene ms = new MapScene( primaryStage, this );
		ms.setMapData( ml.getRootMap() );//bigMapData);
		Player.bell();
	}




	public static void main(String[] args) {
		launch(args);
	}

}