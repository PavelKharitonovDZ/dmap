package ru.dz.vita2d.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;

public class EntityListWindow extends AbstractFormWindow {

	//private Stage stage = new Stage();
	//private Scene scene;
	private EntityListView view; // TODO make interface for views? Generalize?

	public EntityListWindow(ServerUnitType type, RestCaller rc, ServerCache sc) {
		
		view = new EntityListView(type, rc, sc);
		scene = new Scene(view.create());
		makeStage(view.getTitle());
		/*
		
		stage.setWidth(800);
		stage.setHeight(500);

        stage.setTitle("My New Stage Title");
        stage.setScene(scene);
        stage.show();
		*/
	}
	
	
}
