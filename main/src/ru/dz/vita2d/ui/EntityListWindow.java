package ru.dz.vita2d.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dz.vita2d.data.HttpCaller;
import ru.dz.vita2d.data.IRestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;

public class EntityListWindow extends AbstractFormWindow {

	private EntityListView view; // TODO make interface for views? Generalize?

	public EntityListWindow(ServerUnitType type, IRestCaller rc, ServerCache sc) 
	{		
		view = new EntityListView(type, sc);
		scene = new Scene(view.create());
		makeStage(view.getTitle());
	}
	
	
}
