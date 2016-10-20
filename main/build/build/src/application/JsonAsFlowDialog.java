package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import application.JsonAsFlowDialog.ViewItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import ru.dz.vita2d.data.DataConvertor;
import ru.dz.vita2d.data.PerTypeCache;
import ru.dz.vita2d.data.ServerCache;
import ru.dz.vita2d.data.ServerUnitType;

@Deprecated
public class JsonAsFlowDialog 
{
	private JSONObject jo;			// Data
	//private Map<String, String> dm = null; 	// Data model (field names)
	private Dialog<ButtonType> dialog;
	//private ServerCache sc;
	private ServerUnitType type;
	private PerTypeCache tc;

	public JsonAsFlowDialog(ServerUnitType type, JSONObject jo) 
	{
		this.jo = jo;
		this.type = type;		
	}
/*	
	void setDataModel(Map<String, String> fieldNamesMap )
	{
		this.dm = fieldNamesMap;		
	}
*/
	public void setCache(PerTypeCache tc) {
		this.tc = tc;
	}
/*
	public void setServerCache(ServerCache sc) {
		this.sc = sc;
	}
	
	private String getFieldName( String id )
	{
		//String fn = dm.get(id);
		//if( fn == null )			fn = id;
		//return fn;
		return sc.getFieldName(type,id);
	}

	private String getFieldType( String id )
	{
		//String fn = dm.get(id);
		//if( fn == null )			fn = id;
		//return fn;
		return sc.getFieldType(type,id);
	}
*/	
	
	public void show()
	{
		//Dialog<ButtonType> 
		dialog = new Dialog<>();
	    dialog.setTitle("Средство"); // TODO hardcode

	    final DialogPane dialogPane = dialog.getDialogPane();
	    dialogPane.setContentText("Means:"); // TODO hardcode
	    dialogPane.getButtonTypes().addAll(ButtonType.OK);
		
	    //FlowPane fp = addFlowPane();
	    ListView<ViewItem> lv = listView();
	    //dialogPane.setExpandableContent(fp);
	    
	    dialogPane.setContent(lv);
	    dialogPane.setMaxWidth(800);
	    dialogPane.setMaxWidth(500);
	    dialog.setResizable(true);
	    
	    dialog.showAndWait();
	}
	
	private ListView<ViewItem> listView()
	{
		ListView<ViewItem> lv = new ListView<>();
		lv.setItems(getListData());
		
		lv.setMinWidth(500);
		lv.setMaxWidth(800);
		
		return lv;
	}
	
	private ObservableList<ViewItem> getListData() {
		List<ViewItem> list = new ArrayList<ViewItem>();

		for( String key : jo.keySet() )
	    {
	    	Object object = jo.get(key);
	    	
	    	// Skip complex ones yet
	    	if (object instanceof JSONObject) {
				//JSONObject new_name = (JSONObject) object;
				continue;
			}
	    	
	    	//Label name = new Label(getFieldName(key));
			//Label value = new Label(object.toString());

	    	if( "shortName".equalsIgnoreCase(key))
	    		dialog.setTitle("Средство '"+object+"'");
	    	
			String fieldName = tc.getFieldName(key);
			
			if(fieldName == null)
				continue;
			
			ViewItem vi = new ViewItem(key, fieldName,object.toString());
			
			list.add(vi);
	    }	    
		
		return FXCollections.observableList(list);
	}
/*
	public FlowPane addFlowPane() {
	    FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(5, 0, 5, 0));
	    flow.setVgap(4);
	    flow.setHgap(4);
	    flow.setPrefWrapLength(500); // preferred width allows for two columns
	    //flow.setStyle("-fx-background-color: DAE6F3;");
	    flow.setMaxWidth(800);

	    for( String key : jo.keySet() )
	    {
	    	Object object = jo.get(key);
	    	
	    	// Skip complex ones yet
	    	if (object instanceof JSONObject) {
				//JSONObject new_name = (JSONObject) object;
				continue;
			}
	    	
	    	Label name = new Label(getFieldName(key));
			Label value = new Label(object.toString());
	    	
	    	HBox pair = new HBox(10, name, value );
	    	pair.setStyle("-fx-background-color: DAE6F3;");
	    	
	        flow.getChildren().add(pair);
	    }	    
	    
	    return flow;
	}
*/	
	
	class ViewItem
	{

		String name;
		String value;
		String type;

		public ViewItem(String id, String name, String _value) {
			this.name = name;
			this.value = _value;
			
			type = tc.getFieldType(id);
			
			if( type == null )
				type = "string";

			value = DataConvertor.readableValue(type, value);
		}
		
		@Override
		public String toString() {

			return name+" = "+value;
		}
		
	}
	
}
