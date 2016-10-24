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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

	private ObservableList<Map> allData = FXCollections.observableArrayList();
	private TableView<Map> table;
	
	private JSONObject objList;
	
	public EntityListView(ServerUnitType type, RestCaller rc, ServerCache sc) 
	{
		super();

		this.type = type;
		this.rc = rc;
		title = "Список: "+type.getDisplayName();
		tc = sc.getTypeCache(type); //new PerTypeCache(type, rc);
		
		try {
			objList = rc.loadUnitList(type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public String getTitle() {
		return title;
	}

	
	
	public Pane create()	
	{
		//Group root = new Group();

		//final Label label = new Label(type.getDisplayName());
		//label.setFont(new Font("Arial", 20));


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
		vbox.getChildren().addAll(getMenu(), /*label,*/ table);

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

	//private ObservableList<Map> generateDataInMap(ObservableList<Map> allData) 
	private void generateDataInMap(ObservableList<Map> allData) 
	{

		//ObservableList<Map> allData = FXCollections.observableArrayList();

		//try 
		{
			//rc.login("show","show");

			//JSONObject objList = rc.loadUnitList(type);
			if(objList == null)
				return;

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
					
					if( ok.ok && ok.search ) //filter(dataRow ) )					
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
						if( ok.ok && ok.search ) //filter(dataRow ) )					
							allData.add(dataRow);
					} );
				}

			});

		}
		/*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//return allData;
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
		
		if( fs.checkSearchFilter(readableValue) ) 
			ok.search = true;

	}


	private class BoolPtr 
	{ 
		public boolean search = false; 
		boolean ok = true; 
	} 

	private Node getMenu() {
		/*
		MenuBar mb = new MenuBar();

		Menu filterMenu = new Menu("Фильтр");

		MenuItem d1 = new MenuItem("Фильтровать");
		d1.setOnAction(actionEvent -> showFilter() );		
		filterMenu.getItems().add(d1);

		//filterMenu.setOnAction(actionEvent -> showFilter() );

		mb.getMenus().addAll(filterMenu );
		*/
		Button clearFlterButton = new Button("X");
		clearFlterButton.setVisible(false);
		clearFlterButton.setOnAction(a -> clearFilters(clearFlterButton) );
		clearFlterButton.setTooltip(new Tooltip("Очистить фильтры"));
		
		Button filterButton = new Button("Фильтр");
		filterButton.setOnAction( a -> showFilter(clearFlterButton) );

		//ImageView clearIv = new ImageView(new Image("icons/color/close.png"));
		//Button clearButton = new Button("",clearIv);
		Button clearButton = new Button("X");
		clearButton.setVisible(false); // search field is empty bu default
		clearButton.setTooltip(new Tooltip("Очистить поисковый запрос"));
		
		
		TextField searchField = new TextField();
		//searchField.setOnAction(action -> searchModified(searchField.getText()));
		Tooltip tip = new Tooltip("Поиск по любому полю");
		searchField.setTooltip(tip);
		searchField.setPromptText("Поиск");
		searchField.setFocusTraversable(false); // or else it gets focus and does not show prompt text
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
		    searchModified(newValue,clearButton);
		});
		
		clearButton.setOnAction(a -> { searchField.setText(""); } );		
		
		/*
		//searchField.setMinHeight(clearButton.getHeight());
		//clearButton.setMaxHeight(searchField.getHeight());
		clearIv.setPreserveRatio(true);
		//clearIv.setScaleY(value);
		clearIv.setFitWidth(searchField.getHeight());
		clearIv.setFitHeight(searchField.getHeight());
		clearButton.setGraphic(clearIv);
		*/
		
		// Auto-sizing spacer
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);
		
		//HBox mhbox = new HBox( mb, spacer, new Label("Поиск: "), searchField );
		//HBox mhbox = new HBox( mb, spacer, searchField, clearButton );
		HBox mhbox = new HBox( filterButton, clearFlterButton, spacer, searchField, clearButton );
		//mhbox.setPadding(new Insets(10));
		mhbox.setPadding(new Insets(0,10,0,0)); // 10 px on right side
		
		
		return mhbox; //mb;
	}





	private void searchModified(String text, Button clearButton) {
		//System.out.println("search: "+text);
		fs.setSearchFilter(text);
		clearButton.setVisible(fs.isSearchActive());
		updateListData();
	}





	private void showFilter(Button clearFlterButton) {
		
		//fieldNames.forEach(fn -> {	fieldFilter(fn); } );
		//fs.clear();
		FilterDialog fd = new FilterDialog(tc);
		fd.show( fs );
		
		clearFlterButton.setVisible(fs.isFilterActive());
		
		//table.refresh();
		//table = new TableView<>(generateDataInMap());
		//table.data
		updateListData();
	}

	private void clearFilters(Button clearFlterButton) {
		fs.clear(); 
		clearFlterButton.setVisible(false); 
		updateListData();
	}




	private void updateListData() {
		allData.clear();
		//table.getda
		generateDataInMap(allData);
		table.refresh();
	}


}
