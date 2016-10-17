package application;

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

import org.json.JSONObject;
import org.json.JSONTokener;

// "http://sv-web-15.vtsft.ru/orvd-test/rest/login"

public class RestCaller {

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
			throw new ProtocolException("HTTP responce code: " + conn.getResponseCode());
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


	
	public JSONObject getMeansRecord( int meanId ) throws IOException
	{

		JSONObject data = getJSON( String.format( "rest/means/view/%d/", meanId ) );
		return data;
	}


	// http://sv-web-15.vtsft.ru/orvd-release/resources/models/means-form.js
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

			JSONObject mdm = rc.getMeansDataModel();
			System.out.println("Mean Data Model = "+mdm.toString());
			
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



	
	static void dumpJson( JSONObject jo )
	{
	    for( String key : jo.keySet() )
	    {
	    	System.out.println(key);
	    }
	}


}
