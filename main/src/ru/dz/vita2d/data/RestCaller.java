package ru.dz.vita2d.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

// "http://sv-web-15.vtsft.ru/orvd-test/rest/login"

/**
 * Here we access all the server data.
 * @author dz
 *
 */
public class RestCaller extends HttpCaller implements IRestCaller
{

	private String loggedInUser;


	public RestCaller(String baseUrl) {
		super(baseUrl);

	}


	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login( String login, String password ) throws IOException 
	{
		JSONObject jo = new JSONObject();
		jo.put("login", login );
		jo.put("password", password );

		//String data = post("rest/login", "{\"login\":\"show\",\"password\":\"show\"}");
		JSONObject data = post("rest/login", jo.toString() );

		loggedInUser = login;

		//System.out.println("Logged in\n");

	}


	private void getIcon(String id) throws IOException
	{
		JSONObject jo = new JSONObject();
		jo.put("id",id);
		//http://sv-web-15.vtsft.ru/orvd-test/rest/files/icons/#id=248
		JSONObject data = post("rest/files/icons", jo.toString() ); // "{\"id\":\"248\"}");
		System.out.println("Icon = "+data.toString());
	}









	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#loadUnitList(ru.dz.vita2d.data.ServerUnitType)
	 */

	@Override
	public JSONObject loadUnitList(ServerUnitType type) throws IOException
	{
		String path = String.format(UNIT_LIST_REST_PATH, type);
		/*
		if(type == ServerUnitType.DOCUMENTS)
			path = String.format(UNIT_LIST_REST_PATH, "linkedFiles"); // My god...
		*/
		//path += "/?size=20&page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";
		path += "/?page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";

		JSONObject jo = new JSONObject();
		JSONObject out = post(path, jo.toString());

		return out;
	}

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#loadOtherList(java.lang.String)
	 */
	@Override
	public JSONObject loadOtherList(String entityType) throws IOException
	{
		String path = String.format(OTHER_LIST_REST_PATH, entityType);

		//path += "/?size=20&page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";
		path += "/?page=1&sort=name&order=asc&parentId=&scrollToId=-1";

		JSONObject jo = new JSONObject();
		JSONObject out = post(path, jo.toString());

		return out;
	}

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#loadDictList(java.lang.String)
	 */
	@Override
	public JSONObject loadDictList(String entityType) throws IOException
	{
		String path = String.format(OTHER_DICT_REST_PATH, entityType);

		//path += "/?size=20&page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";
		path += "/?page=1&sort=name&order=asc&parentId=&scrollToId=-1";

		JSONObject jo = new JSONObject();
		JSONObject out = post(path, jo.toString());

		return out;
	}

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getDataRecord(ru.dz.vita2d.data.IRef)
	 */

	@Override
	public JSONObject getDataRecord( IRef ref ) throws IOException
	{
		JSONObject data = getJSON( String.format( "rest/%s/view/%d/", ref.getEntityName(), ref.getId() ) );
		return data;
	}
	

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getDataRecord(ru.dz.vita2d.data.ServerUnitType, int)
	 */

	@Override
	public JSONObject getDataRecord( ServerUnitType type, int id ) throws IOException
	{
		JSONObject data = getJSON( String.format( "rest/%s/view/%d/", type, id ) );
		return data;
	}


	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getDataRecord(ru.dz.vita2d.data.UnitRef)
	 */
	
	@Override
	public JSONObject getDataRecord( UnitRef ref ) throws IOException
	{
		return getDataRecord( ref.getType(), ref.getId() );
	}
	
	
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getDataModel(ru.dz.vita2d.data.ServerUnitType)
	 */
	@Override
	public JSONObject getDataModel(ServerUnitType unitType) throws IOException
	{
		return getDataModel(unitType.toString());
	}
	
	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getDataModel(java.lang.String)
	 */
	@Override
	public JSONObject getDataModel(String entityName) throws IOException {
		String data = getString( String.format( "resources/models/%s-form.js", entityName ) );
		if( data == null)
			return null;
		//jsEval(data);

		// This page gives out not a clean JSON but JavaScript assignment 
		//data = data.replaceAll("^\\$v\\.models\\[\\'means-form\\'\\]=", "" );

		int eqpos = data.indexOf("=");
		if( eqpos < 0)
		{
			System.out.println("no '=' sign in means data model "+data); // TODO logging
			return null;
		}

		data = data.substring(eqpos+1); // skip all up to and incl '=' sign
		JSONObject out = null;
		try {
		out = new JSONObject(data);
		} catch(JSONException e)
		{
			System.out.println("wrong data model "+data); // TODO logging
			throw e;
		}
		return out;
	}

	/*
	private ScriptEngineManager engineManager = new ScriptEngineManager();

	private String jsEval(String jsCode)
	{
		ScriptEngine engine = engineManager.getEngineByName("nashorn");

		try {
			System.out.println(jsCode);

			String ret = "";

			engine.put("ret", ret);


			engine.eval("var $v; $v = new Object(); $v.models = new Object();");
			engine.eval(jsCode+";");
			engine.eval("ret = \"\" + $v.models['means-form'] ;");
			//String ret = (String) engine.getContext().getAttribute("$v.models['means-form']");

			ret = (String) engine.get("ret");

			System.out.println(ret);
			return ret;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	 */	





	private final Object serverMutex = new Object();
	private String serverVersion = null;

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getServerVersion()
	 */
	@Override
	public String getServerVersion() {
		synchronized (serverMutex) {
			if( serverVersion == null )
				loadServerVersion();
			if( serverVersion == null )
				serverVersion = "?.?";
			return serverVersion;
		}
	}


	private void loadServerVersion() {
		try {
			String indexPage = getString("index/");

			int pos = indexPage.indexOf("Версия:");
			if(pos < 0) return;
			
			pos += 7; // skip word
			
			String rest = indexPage.substring(pos);
			
			int endPos = rest.indexOf("<");
			if(endPos <= 0) return;
			if(endPos > 10) endPos = 10;
			
			serverVersion = rest.substring(0, endPos).trim();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getLoggedInUser()
	 */
	@Override
	public String getLoggedInUser() {
		return loggedInUser;
	}




	static public void dumpJson( JSONObject jo )
	{
		for( String key : jo.keySet() )
		{
			System.out.println(key);
		}
	}


	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IRestCaller#getServerURL()
	 */
	@Override
	public String getServerURL() {
		return baseUrl;
	}

	
	
	public static void saveToFile( String name, String data )
	{
		try {
			FileOutputStream os = new FileOutputStream("c:/tmp/"+name);
			os.write(data.getBytes());
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) 
	{	
		//RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-test");
		IRestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");

		System.out.println("Start\n");

		try {

			rc.login("show","show");
			//rc.getIcon("248");

			//rc.getServerVersion();
			
			extractOther(rc,"employees");
			extractOther(rc,"meanKinds");
			extractOther(rc,"objKinds");
			extractOther(rc,"contragents");
			extractOther(rc,"locations");
			extractOther(rc,"divisions");
			extractOther(rc,"users");

			extractDict(rc,"airRoutes");

			
			JSONObject mdm = rc.getDataModel(ServerUnitType.MEANS);//rc.getMeansDataModel();
			//System.out.println("Mean Data Model = "+mdm.toString());
			saveToFile( "means_model", mdm.toString() );

			JSONObject odm = rc.getDataModel(ServerUnitType.OBJECTS);//rc.getMeansDataModel();
			saveToFile( "objs_model", odm.toString() );

			
			JSONObject meanList = rc.loadUnitList(ServerUnitType.MEANS);
			//dumpJson(objList);
			//System.out.println("List = "+objList.toString());
			saveToFile( "means_list", meanList.toString() );

			JSONObject objList = rc.loadUnitList(ServerUnitType.OBJECTS);
			//dumpJson(objList);
			//System.out.println("List = "+objList.toString());
			saveToFile( "objs_list", objList.toString() );

			
			
			JSONObject obj = rc.getDataRecord(ServerUnitType.OBJECTS, 740316);
			//dumpJson(obj);
			//System.out.println("Obj = "+obj.toString());
			saveToFile( "obj_one", obj.toString() );
			
			JSONObject mean = rc.getDataRecord(ServerUnitType.MEANS, 740316);
			saveToFile( "mean_one", mean.toString() );
			
			

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}		

		System.out.println("Done\n");

	}


	private static void extractDict(IRestCaller rc, String name) throws IOException {
		JSONObject mkl =  rc.loadDictList(name);
		saveToFile( name+"_dict", mkl.toString() );
	}


	private static void extractOther(IRestCaller rc, String name) throws IOException {
		JSONObject mkl =  rc.loadOtherList(name);
		saveToFile( name+"_list", mkl.toString() );
	}




}
