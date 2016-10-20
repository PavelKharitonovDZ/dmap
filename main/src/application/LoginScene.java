package application;

import java.io.IOException;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.dz.vita2d.data.RestCaller;

@Deprecated
public class LoginScene {
	private TextField loginField;
	private TextField passwdField;

	//private static VBox root;
	private RestCaller rc;
	private Scene me;
	private Main main;
	private Label message;

	public LoginScene(RestCaller rc, Stage primaryStage, Main main) {
        this.rc = rc;
		this.main = main;
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

		me = new Scene( root );

	    //me.setAccelerator(KeyCombination.keyCombination("Alt+F4"));

		me.setOnKeyPressed(new EventHandler<KeyEvent>() {

	        public void handle(KeyEvent ke) {
	            if (ke.getCode() == KeyCode.ESCAPE) {
	                //System.out.println("Key Pressed: " + ke.getCode());
	                doDemoLogin();
	            }
	        }
	    });
		
		primaryStage.setScene(me);
        primaryStage.setTitle( "ОРВД: Вход в систему"  );
        primaryStage.show();

	}

	private HBox createButtons() {
        Button m1 = new Button("Войти");
        m1.setOnAction(e -> doLogin(loginField.getText(),passwdField.getText()) );
        
        Button m2 = new Button("Демо [ESC]");
        m2.setOnAction(e -> doDemoLogin() );
        
        
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
			//System.out.println("login successful, user is "+login);

			main.afterLogin();
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
