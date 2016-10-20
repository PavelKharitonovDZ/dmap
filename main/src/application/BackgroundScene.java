package application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BackgroundScene {
	public BackgroundScene(Stage primaryStage) {

        Image bg = new Image("background.png");
        ImageView bgView = new ImageView(bg);        

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(bgView);

		Scene me = new Scene( borderPane );
		
		primaryStage.setScene(me);
        primaryStage.setTitle( "ОРВД: Вход в систему"  );
        primaryStage.show();

	}

}
