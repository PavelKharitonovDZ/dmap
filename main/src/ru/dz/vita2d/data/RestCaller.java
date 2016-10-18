package ru.dz.vita2d.data;

import java.io.BufferedReader;
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
			String errText = loadString(conn.getErrorStream());
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
		conn.setRequestMethod("GET");

		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(postData.getBytes());
		os.flush();
		os.close();

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
		
		System.out.println("Logged in\n");

	}


	private void getIcon(String id) throws IOException
	{
		JSONObject jo = new JSONObject();
		jo.put("id",id);
		//http://sv-web-15.vtsft.ru/orvd-test/rest/files/icons/#id=248
		JSONObject data = post("rest/files/icons", jo.toString() ); // "{\"id\":\"248\"}");
		System.out.println("Icon = "+data.toString());
	}


	@Deprecated
	public JSONObject getMeansRecord( int meanId ) throws IOException
	{

		JSONObject data = getJSON( String.format( "rest/means/view/%d/", meanId ) );
		return data;
	}

	// http://sv-web-15.vtsft.ru/orvd-release/resources/models/means-form.js
	@Deprecated
	public JSONObject getMeansDataModel() throws IOException
	{

		String data = getString( "resources/models/means-form.js" );
		
		// This page gives out not a clean JSON but JavaScript assignment 
		//data = data.replaceAll("^\\$v\\.models\\[\\'means-form\\'\\]=", "" );

		int eqpos = data.indexOf("=");
		if( eqpos < 0)
		{
			System.out.println("no '=' sign in means data model "+data);
			return null;
		}
		
		data = data.substring(eqpos+1); // skip all up to and incl '=' sign
		
		
		//System.out.println("model "+data);
		
		JSONObject out = new JSONObject(data);
		
		return out;
	}


	
	
	
	
	
	
	
	
	static final String LIST_REST_PATH = "rest/%s/list/";
	
	/**
	 * Get list of objects of given type. 
	 * @param type See ServerUnitType constants for types.
	 * @return JSON with data
	 * @throws IOException
	 */
	
	JSONObject loadList(ServerUnitType type) throws IOException
	{
		String path = String.format(LIST_REST_PATH, type);
		
		JSONObject jo = new JSONObject();
		jo.put("sort", "id" );
		jo.put("order", "asc" );
		jo.put("size", 1000 );
		jo.put("page", 1 );
		jo.put("_", "000" );
		
		System.out.println(jo.toString());
		
		JSONObject out = post(path, jo.toString());

		//JSONObject out = getJSON(path);
		
		return out;
	}
	

	/**
	 * Get object of given type. 
	 * @param type See ServerUnitType constants for types.
	 * @param id object id
	 * @return JSON with data
	 * @throws IOException
	 */
	
	public JSONObject getDataRecord( ServerUnitType type, int id ) throws IOException
	{
		JSONObject data = post( String.format( "rest/%s/view/%d/", type, id ), "" );
		return data;
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
			System.out.println("no '=' sign in means data model "+data);
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
	
	
	
	
	public static void main(String[] args) 
	{	
		//RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-test");
		RestCaller rc = new RestCaller("http://sv-web-15.vtsft.ru/orvd-release");

		try {
			System.out.println("Start\n");

			rc.login("show","show");
			//rc.getIcon("248");
			
			
			JSONObject mr = rc.getMeansRecord( 2441372 );
			System.out.println("Mean = "+mr.toString());

			JSONObject mdm = rc.getDataModel(ServerUnitType.MEANS);//rc.getMeansDataModel();
			System.out.println("Mean Data Model = "+mdm.toString());
			
			
			/*
			JSONObject objList = rc.loadList(ServerUnitType.OBJECTS);
			dumpJson(objList);
			
			
			JSONObject obj = rc.getDataRecord(ServerUnitType.OBJECTS, 740316);
			dumpJson(obj);
			*/
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}		

	}


	public String getServerVersion() {
		// TODO Auto-generated method stub
		return "?.?";
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

	

}
