package ru.dz.vita2d.ui;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;

public class EntityFormWindow extends AbstractFormWindow {
	private EntityFormView view;

	public EntityFormWindow(ServerUnitType type, RestCaller rc, PerTypeCache tc, int id) throws IOException {
		
		view = new EntityFormView(type, rc, tc, id);
		
		scene = new Scene(view.create());

		makeStage(view.getTitle());

	}


}
