package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.dz.vita2d.data.DataConvertor;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerUnitType;

public class TableViewSample extends Application {

	//public static final String Column1MapKey = "A";
	//public static final String Column2MapKey = "B";

	Set<String> fieldNames = new HashSet<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Table View Sample");
		stage.setWidth(300);
		stage.setHeight(500);

		final Label label = new Label(type.getDisplayName());
		label.setFont(new Font("Arial", 20));


		TableView<Map> table_view = new TableView<>(generateDataInMap());

		table_view.setEditable(true);
		table_view.getSelectionModel().setCellSelectionEnabled(true);
		table_view.setMinWidth(600);


		fieldNames.forEach(fName -> {
			String readableName = tp.getFieldName(fName);
			if( readableName == null )
				return;

			TableColumn<Map, String> col = new TableColumn<>(readableName); // TODO Must be human readable name
			col.setCellValueFactory(new MapValueFactory(fName));

			table_view.getColumns().add(col);
		});

		final VBox vbox = new VBox();

		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, table_view);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	//ServerUnitType type = ServerUnitType.OBJECTS;
	ServerUnitType type = ServerUnitType.OBJECTS;
	RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");
	PerTypeCache tp = new PerTypeCache(type, rc);

	private ObservableList<Map> generateDataInMap() {
		int max = 10;
		ObservableList<Map> allData = FXCollections.observableArrayList();

		try {
			rc.login("show","show");

			JSONObject objList = rc.loadList(type);
			//JSONObject objList = rc.loadList(ServerUnitType.JOBS);

			//System.out.println("List = "+objList);

			JSONArray a = objList.getJSONArray("list");
			//RestCaller.dumpJson(objList);
			//System.out.println("List = "+a);

			a.forEach( li -> { 
				//System.out.println("li = "+li); 
				JSONObject lio = (JSONObject) li;

				//JSONObject odata = lio.getJSONObject(type.getObjectTypeName());
				JSONObject odata = lio.getJSONObject("obj");

				//System.out.println("data = "+odata);

				Map<String, String> dataRow = new HashMap<>();

				odata.keySet().forEach(fName -> 
				{ 
					Object data = odata.get(fName);
					if(
							(data instanceof String)
							|| (data instanceof Integer) 
							) 
					{
						dataRow.put(fName, data.toString()); 
						fieldNames.add(fName); 
					}
					else if(data instanceof Boolean)
					{
						dataRow.put(fName, DataConvertor.booleanReadableValue(data.toString())); 
						fieldNames.add(fName); 
					}
					else if(data instanceof JSONObject) 
					{
						JSONObject sub = (JSONObject) data;
						if( sub.has("name") )
						{

							dataRow.put(fName, sub.getString("name")); 
							fieldNames.add(fName); 
						}
					}
					else
					{
						System.out.println("class = "+ data.getClass()+" "+fName+"="+data );
					}

				} );

				allData.add(dataRow);

			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
        for (int i = 1; i < max; i++) {
            Map<String, String> dataRow = new HashMap<>();

            String value1 = "A" + i;
            String value2 = "B" + i;

            dataRow.put(Column1MapKey, value1);
            dataRow.put(Column2MapKey, value2);

            allData.add(dataRow);
        }
		 */
		return allData;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
	}
}