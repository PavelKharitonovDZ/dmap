package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.function.Consumer;

import application.Main;
import javafx.application.Platform;
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
import ru.dz.vita2d.data.IRestCaller;

public class LoginFormView 
{
	private TextField loginField;
	private TextField passwdField;

	private IRestCaller rc;
	private Label message;
	private Consumer<String> loginDone = null;
	private boolean loggedIn = false;

	public LoginFormView(IRestCaller rc, Consumer<String> loginDone) {
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

	private Button loginButton = new Button("Войти");
	private Button demoButton = new Button("Демо [ESC]");

	private HBox createButtons() {
		//Button loginButton = new Button("Войти");
		//loginButton.setOnAction(e -> doLogin(loginField.getText(),passwdField.getText()) );
		loginButton.setOnAction(e -> doThreadedLogin(loginField.getText(),passwdField.getText()) );
		
		loginButton.setDefaultButton(true);

		//Button demoButton = new Button("Демо [ESC]");
		demoButton.setOnAction(e -> doDemoLogin() );
		demoButton.setCancelButton(true);


		HBox buttons = new HBox(10, loginButton, demoButton );
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(10));

		loginButton.setDisable(false);
		demoButton.setDisable(false);

		return buttons;
	}

	private void doDemoLogin() {
		//doLogin("show","show");
		doThreadedLogin("show","show");
	}

	private void doLogin(String login, String pw) {
		try {
			rc.login(login,pw);
			// TODO log
			//System.out.println("login successful, user is "+login);

			loggedIn = true; // TODO wrong? check in RestCaller?

			message.setText("Имя и пароль приняты, запуск приложения");
			loginButton.setDisable(true);
			demoButton.setDisable(true);

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


	private void doThreadedLogin(String login, String pw)
	{
		loginButton.setDisable(true);
		demoButton.setDisable(true);
		message.setText("Проверяем имя и пароль");

		Thread r = new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean succ = false;
				boolean ioerr = false;
				try {
					rc.login(login,pw);
					succ = true;
				} catch (java.net.ProtocolException e) {
					// Login failed
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ioerr = true;
				}

				backToWinThread(succ,ioerr);

			}

			private void backToWinThread(boolean succ, boolean ioerr) {
				Platform.runLater(new Runnable() {					
					@Override
					public void run() {
						afterLogin(succ,ioerr,login);						
					}
				});
			}
		});
		
		r.start();
	}

	private void afterLogin(boolean succ, boolean ioerr, String login)	
	{
		if( ioerr )
		{
			loginButton.setDisable(false);
			demoButton.setDisable(false);
			message.setText("Ошибка связи");
		
			return;
		}
		
		if(!succ)
		{
			loginButton.setDisable(false);
			demoButton.setDisable(false);
			message.setText("Неверный логин или пароль");
		}
		else
		{

			loggedIn = true; // TODO wrong? check in RestCaller?			
			message.setText("Имя и пароль приняты, запуск приложения");
		}

		if(succ && (loginDone != null))
			loginDone.accept(login);

	}


	public boolean isLoggedIn() {

		return loggedIn;
	}



}


