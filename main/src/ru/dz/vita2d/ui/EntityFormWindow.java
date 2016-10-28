package ru.dz.vita2d.ui;

import java.io.IOException;

import javafx.scene.Scene;
import ru.dz.vita2d.data.ITypeCache;
import ru.dz.vita2d.data.net.RestCaller;
import ru.dz.vita2d.data.net.ServerCache;
import ru.dz.vita2d.data.ref.IRef;
import ru.dz.vita2d.data.type.ServerUnitType;

public class EntityFormWindow extends AbstractFormWindow {
	private EntityFormView view;

	public EntityFormWindow(ServerUnitType type, ITypeCache tc, int id) throws IOException 
	{	
		view = new EntityFormView(type, tc, id);		
		scene = new Scene(view.create());
		makeStage(view.getTitle());
	}

	public EntityFormWindow(IRef ref, ServerCache sc) throws IOException 
	{	
		view = new EntityFormView(ref, sc);		
		scene = new Scene(view.create());
		makeStage(view.getTitle());
	}


}
