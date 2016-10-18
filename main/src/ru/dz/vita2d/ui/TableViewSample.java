package ru.dz.vita2d.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerUnitType;

public class TableViewSample extends Application {

	//ServerUnitType type = ServerUnitType.OBJECTS;
	ServerUnitType type = ServerUnitType.OBJECTS;
	RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");
	//PerTypeCache tp = new PerTypeCache(type, rc);

	private EntityListView view;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) 
	{
		view = new EntityListView(type, rc);
		
		Scene scene = new Scene(view.create());
		
		stage.setTitle("Table View Sample");
		stage.setWidth(300);
		stage.setHeight(500);
		
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}



	@Override
	public void init() throws Exception {
		super.init();
	}
}