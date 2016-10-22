package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import ru.dz.vita2d.data.DataConvertor;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;
import ru.dz.vita2d.unused.JsonAsFlowDialog;
/**
 * </p>Display list of entities.</p>
 * 
 * @author dz
 *
 */
public class EntityListView {

	private ServerUnitType type;
	private RestCaller rc;
	private PerTypeCache tc;

	private Set<String> fieldNames = new HashSet<>();
	private String title = "";


	public EntityListView(ServerUnitType type, RestCaller rc, ServerCache sc) {
		super();

		this.type = type;
		this.rc = rc;
		title = "Список: "+type.getDisplayName();
		tc = sc.getTypeCache(type); //new PerTypeCache(type, rc);
	}



	public Pane create()	
	{
		//Group root = new Group();

		final Label label = new Label(type.getDisplayName());
		label.setFont(new Font("Arial", 20));


		TableView<Map> table = new TableView<>(generateDataInMap());

		table.setEditable(true);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.setMinWidth(600);

		placeFirst(table, "shortName");
		//placeFirst(table, "unitName"); // no shortName in 

		fieldNames.forEach(fName -> {
			addColumn(table, fName);
		});

		table.setRowFactory( tv -> {
			TableRow<Map> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					Map<String,String> rowData = row.getItem();
					//System.out.println(rowData);
					String sid = rowData.get("id");
					if( sid == null )
					{
						// TODO log error
						return;
					}
					int id = Integer.parseInt(sid); 
					//System.out.println(id);

					try {
						EntityFormWindow fw = new EntityFormWindow(type, rc, tc, id);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			return row ;
		});


		final VBox vbox = new VBox();

		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, table);

		return vbox;
	}



	private void placeFirst(TableView<Map> table, String exception) {
		if(fieldNames.contains(exception))
		{
			fieldNames.remove(exception);
			addColumn(table, exception);
		}
	}



	private void addColumn(TableView<Map> table, String fName) {
		String readableName = tc.getFieldName(fName);
		if( readableName == null )
			return;

		TableColumn<Map, String> col = new TableColumn<>(readableName);
		col.setCellValueFactory(new MapValueFactory(fName));

		table.getColumns().add(col);
	}

	private ObservableList<Map> generateDataInMap() 
	{

		ObservableList<Map> allData = FXCollections.observableArrayList();

		try {
			//rc.login("show","show");

			JSONObject objList = rc.loadList(type);

			JSONArray a = objList.getJSONArray("list");

			a.forEach( li -> { 
				//System.out.println("li = "+li); 
				JSONObject lio = (JSONObject) li;

				if(type == ServerUnitType.OBJECTS)
				{
					JSONObject odata = lio.getJSONObject("obj");

					//System.out.println("data = "+odata);

					Map<String, String> dataRow = new HashMap<>();

					loadEntity(odata, dataRow);
					allData.add(dataRow);

				}
				else
				{
					JSONArray ja = lio.getJSONArray(type.getPluralTypeName());

					ja.forEach(jae -> {

						JSONObject odata = (JSONObject) jae; //lio.getJSONObject(type.getPluralTypeName());
						//JSONObject odata = lio.getJSONObject("obj");

						//System.out.println("data = "+odata);

						Map<String, String> dataRow = new HashMap<>();

						loadEntity(odata, dataRow);
						allData.add(dataRow);
					} );
				}

			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allData;
	}



	private void loadEntity(JSONObject odata, Map<String, String> dataRow) {
		odata.keySet().forEach(fName -> 
		{ 
			Object data = odata.get(fName);
			//String type = tc.getFieldType(fName);
			
			DataConvertor.parseAnything(fName, data, (fieldName,fieldVal) -> {
				String fieldType = tc.getFieldType(fName);
				dataRow.put(fieldName, DataConvertor.readableValue( fieldType, fieldVal )); 
				fieldNames.add(fieldName); 
				
			});
			
			/*
			if(
					(data instanceof String)
					|| (data instanceof Integer) 
					) 
			{
				dataRow.put(fName, DataConvertor.readableValue( type, data.toString() )); 
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
				//System.out.println("class = "+ data.getClass()+" "+fName+"="+data );
			}
			*/
		} );
	}



	public String getTitle() {
		return title;
	}

}
