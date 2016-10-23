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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.dz.vita2d.data.DataConvertor;
import ru.dz.vita2d.data.FilterSet;
import ru.dz.vita2d.data.ModelFieldDefinition;
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
	private FilterSet fs = new FilterSet();


	public EntityListView(ServerUnitType type, RestCaller rc, ServerCache sc) {
		super();

		this.type = type;
		this.rc = rc;
		title = "Список: "+type.getDisplayName();
		tc = sc.getTypeCache(type); //new PerTypeCache(type, rc);
	}



	private ObservableList<Map> allData = FXCollections.observableArrayList();
	private TableView<Map> table;
	
	public Pane create()	
	{
		//Group root = new Group();

		final Label label = new Label(type.getDisplayName());
		label.setFont(new Font("Arial", 20));


		//TableView<Map>
		generateDataInMap(allData);
		table = new TableView<>(allData);

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
		vbox.getChildren().addAll(getMenu(), label, table);

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

	private ObservableList<Map> generateDataInMap(ObservableList<Map> allData) 
	{

		//ObservableList<Map> allData = FXCollections.observableArrayList();

		try {
			//rc.login("show","show");

			JSONObject objList = rc.loadUnitList(type);

			JSONArray a = objList.getJSONArray("list");

			a.forEach( li -> { 
				//System.out.println("li = "+li); 
				JSONObject lio = (JSONObject) li;

				if(type == ServerUnitType.OBJECTS)
				{
					JSONObject odata = lio.getJSONObject("obj");

					//System.out.println("data = "+odata);

					Map<String, String> dataRow = new HashMap<>();

					BoolPtr ok = new BoolPtr();
					loadEntity(odata, dataRow, ok);
					
					if( ok.ok ) //filter(dataRow ) )					
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

						BoolPtr ok = new BoolPtr();
						loadEntity(odata, dataRow, ok);
						if( ok.ok ) //filter(dataRow ) )					
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







	private void loadEntity(JSONObject odata, Map<String, String> dataRow, BoolPtr ok ) {
		odata.keySet().forEach(fName -> 
		{ 
			Object data = odata.get(fName);

			DataConvertor.parseAnything(fName, data, (fieldName,fieldVal) -> {
				String fieldType = tc.getFieldType(fName);
				String readableValue = DataConvertor.readableValue( fieldType, fieldVal );
				
				filter(ok, fName, readableValue);
				
				dataRow.put(fieldName, readableValue); 
				fieldNames.add(fieldName); 
				// update set of possible values in per type cache
				tc.updateFieldValuesStats(fieldName, readableValue);
			});


		} );
	}





	private void filter(BoolPtr ok, String fieldId, String readableValue) 
	{
		
		boolean filter = fs.filter( fieldId, readableValue );
		//System.out.println(fieldId+"="+readableValue+" = "+(filter?"1":"0") );
		if( !filter ) ok.ok = false;
	}


	private class BoolPtr { boolean ok = true; } 

	public String getTitle() {
		return title;
	}

	private Node getMenu() {
		MenuBar mb = new MenuBar();

		Menu filterMenu = new Menu("Фильтр");

		MenuItem d1 = new MenuItem("Фильтровать");
		d1.setOnAction(actionEvent -> showFilter() );		
		filterMenu.getItems().add(d1);

		//filterMenu.setOnAction(actionEvent -> showFilter() );

		mb.getMenus().addAll(filterMenu );

		return mb;
	}



	private void showFilter() {
		
		//fieldNames.forEach(fn -> {	fieldFilter(fn); } );
		
		FilterDialog fd = new FilterDialog(tc);
		fd.show( fs );
		
		//table.refresh();
		//table = new TableView<>(generateDataInMap());
		//table.data
		allData.clear();
		//table.getda
		generateDataInMap(allData);
		table.refresh();
	}



	private void fieldFilter(String fn) {
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
		
		System.out.println(fn+":");
		fm.getValues().forEach(fv -> System.out.println("\t"+fv));
		
		
	}

	/** 
	 * Check if this row is to be shown.
	 * @param dataRow
	 * @return
	 */
	private boolean filter(Map<String, String> dataRow) 
	{
		
		for( Map.Entry<String,String> e : dataRow.entrySet() )
		{
			if( fs.filter(e.getKey(), e.getValue() ) == false )
			{
				System.out.println("off "+e.getKey());
				return false;
			}
		}
		
		return true;
	}

}
