package application;

// http://sv-web-15.vtsft.ru/orvd-release/index/#
// http://sv-web-15.vtsft.ru/orvd-test/index/#

import javafx.application.Application;
import javafx.stage.Stage;
import media.Player;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.maps.MapList;


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