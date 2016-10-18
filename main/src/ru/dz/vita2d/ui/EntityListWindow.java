package ru.dz.vita2d.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerUnitType;

public class EntityListWindow {

	private Stage stage = new Stage();
	private Scene scene;
	private EntityListView view;

	public EntityListWindow(ServerUnitType type, RestCaller rc) {
		
		view = new EntityListView(type, rc);
		
		scene = new Scene(view.create());
		
		stage.setWidth(800);
		stage.setHeight(500);

        stage.setTitle("My New Stage Title");
        stage.setScene(scene);
        stage.show();

	}
	
	
}
