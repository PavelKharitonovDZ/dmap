package ru.dz.vita2d.ui;

import java.io.IOException;

import javafx.scene.Scene;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerUnitType;

public class EntityFormWindow extends AbstractFormWindow {
	private EntityFormView view;

	public EntityFormWindow(ServerUnitType type, PerTypeCache tc, int id) throws IOException {
		
		view = new EntityFormView(type, tc, id);
		
		scene = new Scene(view.create());

		makeStage(view.getTitle());

	}


}
