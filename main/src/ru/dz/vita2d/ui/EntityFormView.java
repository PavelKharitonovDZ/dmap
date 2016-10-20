package ru.dz.vita2d.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import ru.dz.vita2d.data.DataConvertor;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.RestCaller;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;

/**
 * </p>Display list entity fields.</p>
 * 
 * @author dz
 *
 */

public class EntityFormView {

	private ServerUnitType type;
	private RestCaller rc;
	private PerTypeCache tc;

	private JSONObject jo;			// Data
	
	private Label shortNameLabel = new Label("");
	private int entityId;
	
	private String title = "";
	
	/**
	 * Display entity data.
	 * 
	 * @param type
	 * @param rc
	 * @param sc
	 * @param entityId - id of object to display (will request from server)
	 * @throws IOException 
	 */
	
	public EntityFormView(ServerUnitType type, RestCaller rc, PerTypeCache tc, int entityId) throws IOException 
	{
		super();

		this.type = type;
		this.rc = rc;
		this.tc = tc;
		this.entityId = entityId;
		
		JSONObject record;

		//record = rc.getDataRecord(type, entityId);
		record = tc.getServerCache().getDataRecord(type, entityId);
		
		//System.out.println(record);
		JSONObject entity = record.getJSONObject("entity");

		jo = entity;
	}

	
	public Pane create()	
	{
		shortNameLabel.setFont(new Font("Arial", 20));
		shortNameLabel.setTooltip(new Tooltip("id: "+entityId) );

		TableView<Map> table = new TableView<>(generateDataInMap());

		table.setEditable(true);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.setMinWidth(470);

		TableColumn<Map, String> col1 = new TableColumn<>("Поле");
		col1.setCellValueFactory(new MapValueFactory("fn"));
		table.getColumns().add(col1);


		TableColumn<Map, String> col2 = new TableColumn<>("Значение");
		col2.setCellValueFactory(new MapValueFactory("fv"));
		table.getColumns().add(col2);

		/*
		table.setRowFactory( tv -> {
			TableRow<Map> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					Map<String,String> rowData = row.getItem();
					//System.out.println(rowData);
					String sid = rowData.get("id");
				}
			});
			return row ;
		});
		*/

		final VBox vbox = new VBox();

		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(shortNameLabel, table);

		return vbox;
	}

	
	private ObservableList<Map> generateDataInMap() {
		//int max = 10;
		ObservableList<Map> allData = FXCollections.observableArrayList();

		for( String key : jo.keySet() )
	    {
	    	Object object = jo.get(key);
	    	
	    	// Skip complex ones yet
	    	if (object instanceof JSONObject) {
				//JSONObject new_name = (JSONObject) object;
				continue;
			}

	    	
	    	if( "shortName".equalsIgnoreCase(key))
	    	{
	    		//dialog.setTitle("Средство '"+object+"'");
	    		title = type.getDisplayName()+": "+object.toString();
	    		shortNameLabel.setText(title);
	    	}
	    	
			String fieldName = tc.getFieldName(key);
			String fieldType = tc.getFieldType(key);
			
			if(fieldName == null)
				continue;
			
			String val = DataConvertor.readableValue( fieldType, object.toString() );
			
			Map<String, String> dataRow = new HashMap<>();

			dataRow.put("fn", fieldName );
			dataRow.put("fv", val );
			
			allData.add(dataRow);
			
	    }	    
		
		

		return allData;
	}


	public String getTitle() {
		return title;
	}
	

}
