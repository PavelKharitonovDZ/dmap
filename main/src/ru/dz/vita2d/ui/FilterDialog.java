package ru.dz.vita2d.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.dz.vita2d.data.FilterSet;
import ru.dz.vita2d.data.ModelFieldDefinition;
import ru.dz.vita2d.data.PerTypeCache;

/**
 * <p>Dialog used to set up table (list)filtering.</p>
 * <p>Shows possible values for all fields.<p>
 * @author dz
 *
 */
public class FilterDialog {

	private Set<String> fieldIds;
	private PerTypeCache tc;

	public FilterDialog(PerTypeCache tc) 
	{
		this.tc = tc;
		fieldIds = tc.getFieldIds();

		makeDialog();
		boxes.clear();
		fieldIds.forEach(fn ->
		{
			fieldFilter(fn);
		} );

	}

	private Dialog<Boolean> dialog = new Dialog<>();
	private GridPane grid = new GridPane();
	private int lastGridRow = 0;

	private void makeDialog() 
	{

		// Create the custom dialog.
		dialog.setTitle("Настройка фильтрации");
		dialog.setHeaderText("Выберите фильтры:");

		// TODO Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Фильтровать", ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType("Показать всё", ButtonData.CANCEL_CLOSE);
		//dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);


		lastGridRow = 0;
		//GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		/*
		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);
		 */

		// Enable/Disable login button depending on whether a username was entered.
		//Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		//loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		//username.textProperty().addListener((observable, oldValue, newValue) -> {
		//    loginButton.setDisable(newValue.trim().isEmpty());
		//});


		ScrollPane scroll = new ScrollPane();
		scroll.setPrefSize(300, 400);
		scroll.setContent(grid);

		dialog.getDialogPane().setContent(scroll);

		// Request focus on the username field by default.
		//Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return true;
			}
			return false;
		});

	}

	public void show(FilterSet fs) {
		Optional<Boolean> result = dialog.showAndWait();

		fs.clear();
		result.ifPresent(usernamePassword -> {
		    if(result.get())
		    	fillFilterSet(fs);
		});		
		 
	}

	// field {name,value} -> checkbox
	private Map<Pair<String,String>,CheckBox>boxes = new HashMap<>();
	
	private void fieldFilter(String fn) 
	{

		if( fn == null ) return;

		ModelFieldDefinition fm = tc.getFieldModel(fn);

		if( fm == null ) return;

		if( fm.isNumeric() ) return; // TODO make range filtering

		if( fm.isDateOrTime() ) return; // TODO make date range filtering

		// It's ok, we have related table's value here available
		//if( fm.isNonFilterable() ) return; // TODO filter by related table row

		// In any case don't show filter if no choice exist
		if( fm.getValues().isEmpty() ) return;

		// Only one case - can't filter too
		if( fm.getValues().size() == 1 ) return;

		//System.out.println(fn+":");
		//fm.getValues().forEach(fv -> System.out.println("\t"+fv));


		
		Label fieldNameLabel = new Label(tc.getFieldName(fn)+":");
		fieldNameLabel.setFont(new Font("Arial", 20));
		grid.add(fieldNameLabel, 0, lastGridRow++);
		

		fm.getValues().forEach(fv -> {
			//grid.add(new Label(fv), 1, myGridRow);
			int myGridRow = lastGridRow++;

			CheckBox cb = new CheckBox(fv);
			grid.add( cb, 0, myGridRow );
			
			boxes.put(new Pair<String, String>(fn, fv), cb);
		});

	}


	/**
	 * if checkbox is checked, add it's field name/val to filter set
	 * @param fs
	 */
	private void fillFilterSet(FilterSet fs) 
	{
		boxes.keySet().forEach(pair -> {
			String fieldId = pair.getKey();
			String fieldValue = pair.getValue();
			
			CheckBox cb = boxes.get(pair);
			if( cb.isSelected() )			
				fs.add(fieldId, fieldValue);
		});
		
	}


}


