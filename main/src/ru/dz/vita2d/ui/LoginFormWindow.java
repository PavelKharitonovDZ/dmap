package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import ru.dz.vita2d.data.IRestCaller;

public class LoginFormWindow extends AbstractFormWindow 
{
	private LoginFormView view;

	public LoginFormWindow(IRestCaller rc, Consumer<String> loginDone)  {
		
		view = new LoginFormView(rc, loginDone);
		
		scene = new Scene(view.create());		

		//makeStage("Вход в систему", Modality.WINDOW_MODAL);
		//stage.setWidth(800);
		//stage.setHeight(500);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Вход в систему");
        stage.setScene(scene);
        stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();
		/*
		stage.showAndWait();
        stage.hide();
        stage.close();
		*/
	}
/*
	public void setLoginAction( Consumer<String> loginDone )
	{
		view.setLoginAction( loginDone );		
	}
*/
	public boolean isLoggedIn() { return view.isLoggedIn(); }

	public void close() {
		stage.close();		
	}	


}
