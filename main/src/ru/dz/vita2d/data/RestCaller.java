package ru.dz.vita2d.data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.json.JSONObject;
import org.json.JSONTokener;

// "http://sv-web-15.vtsft.ru/orvd-test/rest/login"

/**
 * Here we access all the server data.
 * @author dz
 *
 */
public class RestCaller 
{

	private final String baseUrl;
	private String loggedInUser;


	public RestCaller(String baseUrl) {
		this.baseUrl = baseUrl;

		// Let cookie authorization work
		CookieManager cm = new CookieManager();
		CookieHandler.setDefault(cm);

	}


	static String loadString(InputStream is) throws IOException
	{
		StringBuilder sb = new StringBuilder();		

		InputStreamReader in = new InputStreamReader(is,"UTF-8");

		BufferedReader br = new BufferedReader(in);

		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}

		return sb.toString();
	}

	static JSONObject loadJSON(InputStream is) throws IOException
	{		
		JSONTokener jt = new JSONTokener(loadString(is));
		return new JSONObject(jt);		
	}

	private void checkResponceCode(HttpURLConnection conn) throws IOException {
		if (conn.getResponseCode() != 200) {
			String errText = "?";
			
			try {
			errText = loadString(conn.getErrorStream());
			} catch( Throwable e )
			{
				System.out.println(e);
			}
			URL errUrl = conn.getURL();
			throw new ProtocolException("Url: "+errUrl+" HTTP responce code: " + conn.getResponseCode() + " text = '"+errText+"'");
		}
	}


	private HttpURLConnection mkConn(String urlTail) throws MalformedURLException, IOException {
		URL url = new URL(baseUrl + "/" + urlTail );
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		return conn;
	}



	private String getString(String urlTail) throws IOException
	{
		HttpURLConnection conn = mkConn(urlTail);
		conn.setRequestMethod("GET");

		checkResponceCode(conn);

		InputStream is = conn.getInputStream();

		String out = loadString(is);
		conn.disconnect();
		return out;
	}


	private JSONObject getJSON(String urlTail) throws IOException
	{
		HttpURLConnection conn = mkConn(urlTail);
		conn.setRequestMethod("GET");

		checkResponceCode(conn);

		InputStream is = conn.getInputStream();

		JSONObject out = loadJSON(is);
		conn.disconnect();
		return out;
	}

	private JSONObject post(String urlTail, String postData) throws IOException
	{
		HttpURLConnection conn = mkConn(urlTail);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");

		if(postData != null)
		{
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(postData.getBytes());
			os.flush();
			os.close();
		}
		checkResponceCode(conn);

		InputStream is = conn.getInputStream();

		JSONObject out = loadJSON(is);
		conn.disconnect();
		return out;
	}











	/**
	 * Logs us in. Must be called first.
	 * @param login
	 * @param password
	 * @throws IOException
	 */
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









	static final String UNIT_LIST_REST_PATH = "rest/units/%s/list/";
	private static final String OTHER_LIST_REST_PATH = "rest/%s/list/";

	/**
	 * Get list of objects of given type. 
	 * @param type See ServerUnitType constants for types.
	 * @return JSON with data
	 * @throws IOException
	 */

	public JSONObject loadUnitList(ServerUnitType type) throws IOException
	{
		String path = String.format(UNIT_LIST_REST_PATH, type);

		//path += "/?size=20&page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";
		path += "/?page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";

		JSONObject jo = new JSONObject();
		JSONObject out = post(path, jo.toString());

		return out;
	}

	/** God knows what differs units from other entities */
	public JSONObject loadOtherList(String entityType) throws IOException
	{
		String path = String.format(OTHER_LIST_REST_PATH, entityType);

		//path += "/?size=20&page=1&sort=obj.division.filial.name&order=asc&parentId=&scrollToId=-1";
		path += "/?page=1&sort=name&order=asc&parentId=&scrollToId=-1";

		JSONObject jo = new JSONObject();
		JSONObject out = post(path, jo.toString());

		return out;
	}


	/**
	 * <p>Get object of given type.<p> 
	 * @param type See ServerUnitType constants for types.
	 * @param id object id
	 * @return JSON with data
	 * @throws IOException
	 */

	public JSONObject getDataRecord( ServerUnitType type, int id ) throws IOException
	{
		JSONObject data = getJSON( String.format( "rest/%s/view/%d/", type, id ) );
		return data;
	}


	/**
	 * <p>Get object of given type.<p> 
	 * @param ref object reference (type+id)
	 * @return JSON with data
	 * @throws IOException
	 */
	
	public JSONObject getDataRecord( UnitRef ref ) throws IOException
	{
		return getDataRecord( ref.getType(), ref.getId() );
	}
	
	
	/**
	 * Get data model (field names, types, etc) for given type.
	 * @param unitType See ServerUnitType constants for types.
	 * @return JSON with model
	 * @throws IOException
	 */
	public JSONObject getDataModel(ServerUnitType unitType) throws IOException
	{
		String data = getString( String.format( "resources/models/%s-form.js", unitType ) );

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

		JSONObject out = new JSONObject(data);		
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

	/**
	 * <p>Get server version by parsing web site index page.</p>
	 * <p>TODO: make REST API call for that!</p>
	 * @return Server version string.
	 */
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
		RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");

		try {
			System.out.println("Start\n");

			rc.login("show","show");
			//rc.getIcon("248");

			//rc.getServerVersion();
			
			JSONObject empl =  rc.loadOtherList("employees");
			saveToFile( "employees_list", empl.toString() );

			JSONObject mkl =  rc.loadOtherList("meanKinds");
			saveToFile( "meanKinds_list", mkl.toString() );
			/*			
			JSONObject mr = rc.getMeansRecord( 2441372 );
			System.out.println("Mean = "+mr.toString());
			 */			

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

	}
	


}
