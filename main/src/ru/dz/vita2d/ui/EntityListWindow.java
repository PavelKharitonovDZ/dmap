package ru.dz.vita2d.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dz.vita2d.data.net.HttpCaller;
import ru.dz.vita2d.data.net.IRestCaller;
import ru.dz.vita2d.data.net.ServerCache;
import ru.dz.vita2d.data.type.ServerUnitType;

public class EntityListWindow extends AbstractFormWindow {

	private EntityListView view; // TODO make interface for views? Generalize?

	public EntityListWindow(ServerUnitType type, IRestCaller rc, ServerCache sc) 
	{		
		view = new EntityListView(type, sc);
		scene = new Scene(view.create());
		makeStage(view.getTitle());
	}
	
	
}
