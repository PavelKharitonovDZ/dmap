package application;

import javafx.animation.AnimationTimer;

// http://sv-web-15.vtsft.ru/orvd-release/index/#
// http://sv-web-15.vtsft.ru/orvd-test/index/#

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.maps.MapList;
import ru.dz.vita2d.media.Player;
import ru.dz.vita2d.ui.LoginFormWindow;


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
		rc = new RestCaller(Defs.HOST_NAME);
		sc = new ServerCache(rc);
	
		// Load data from local file with list of maps
		ml = new MapList();

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		if( Defs.FULL_SCREEN)
		{
			primaryStage.setMaximized(true);		
			primaryStage.setResizable(false);
		}
		
		/*
		AnimationTimer at = new AnimationTimer() {			
			@Override
			public void handle(long now) {
				System.out.println("at ");
				
			}
		};
		at.start();
		*/
		
		//primaryStage.initStyle(StageStyle.DECORATED);
		logout();
	}    

	LoginFormWindow lw;
	
	public void logout() {
		//LoginScene ls = 
		//new LoginScene(rc, primaryStage, this );
		new BackgroundScene( primaryStage );
		lw = new LoginFormWindow(rc, login -> afterLogin() );

	}

	public void afterLogin() {
		// Resets Scene
		lw.close();
		MapScene ms = new MapScene( primaryStage, this );
		ms.setMapData( ml.getRootMap() );
		
		if( !Defs.FULL_SCREEN )
			primaryStage.centerOnScreen();

		//primaryStage.setMaximized(true);
		//primaryStage.setMaximized(true);
		Player.bell();
	}




	public static void main(String[] args) {
		launch(args);
	}


}