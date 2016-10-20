package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.function.Consumer;

import application.Main;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.dz.vita2d.data.RestCaller;

public class LoginFormView 
{
	private TextField loginField;
	private TextField passwdField;

	private RestCaller rc;
	private Label message;
	private Consumer<String> loginDone = null;

	public LoginFormView(RestCaller rc, Consumer<String> loginDone) {
		this.rc = rc;
		this.loginDone = loginDone;		
	}

	
	public Pane create()	
	{
		HBox buttons = createButtons();

		loginField = new TextField();
		passwdField = new TextField();		
		message = new Label();
		message.setMinWidth(300);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		//grid.setPadding(new Insets(10));

		grid.add(loginField, 1, 0);
		grid.add(passwdField, 1, 1);
		//grid.add(message, 0, 2);

		grid.add(new Label("Имя"), 0, 0);
		grid.add(new Label("Пароль"), 0, 1);

		VBox container = new VBox( grid, message );
		container.setPadding(new Insets(10));
		container.setSpacing(10);


		Image logo = new Image("logo.png");
		ImageView logoView = new ImageView(logo);

		HBox andLogo = new HBox( container, logoView );

		VBox root = new VBox( andLogo, buttons );
		root.setFillWidth(true);
		VBox.setVgrow(andLogo, Priority.ALWAYS);

		return root;
	}

	private HBox createButtons() {
		Button m1 = new Button("Войти");
		m1.setOnAction(e -> doLogin(loginField.getText(),passwdField.getText()) );
		m1.setDefaultButton(true);

		Button m2 = new Button("Демо [ESC]");
		m2.setOnAction(e -> doDemoLogin() );
		m2.setCancelButton(true);


		HBox buttons = new HBox(10, m1, m2 );
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(10));
		return buttons;
	}

	private void doDemoLogin() {
		doLogin("show","show");
	}

	private void doLogin(String login, String pw) {
		try {
			rc.login(login,pw);
			// TODO log
			System.out.println("login successful, user is "+login);

			//main.afterLogin();
			
			if(loginDone != null)
				loginDone.accept(login);
			
		} catch (java.net.ProtocolException e) {
			// Login failed
			message.setText("Неверный логин или пароль");
		} catch (IOException e) {
			message.setText("Нет связи");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}


