package ru.dz.vita2d.ui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AbstractFormWindow {
	protected Stage stage = new Stage();
	protected Scene scene;
	
	protected void makeStage(String title) {
		makeStage(title, Modality.NONE);
	}

	protected void makeStage(String title, Modality mod) 
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
		{
	        public void handle(KeyEvent ke) 
	        {
	            if (ke.getCode() == KeyCode.ESCAPE) {
	                stage.close();
	            }
	        }
	    });
		
		stage.setWidth(800);
		stage.setHeight(500);
		stage.initModality(mod);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
	}

	
}
