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

import org.json.JSONObject;
import org.json.JSONTokener;

public class HttpCaller {

	protected final String baseUrl;

	public HttpCaller(String baseUrl) {
		this.baseUrl = baseUrl;

		// Let cookie authorization work
		CookieManager cm = new CookieManager();
		CookieHandler.setDefault(cm);
	}

	static String loadString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();		
	
		InputStreamReader in = new InputStreamReader(is,"UTF-8");
	
		BufferedReader br = new BufferedReader(in);
	
		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
	
		return sb.toString();
	}

	static JSONObject loadJSON(InputStream is) throws IOException {		
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

	protected String getString(String urlTail) throws IOException {
		HttpURLConnection conn = mkConn(urlTail);
		conn.setRequestMethod("GET");
	
		checkResponceCode(conn);
	
		InputStream is = conn.getInputStream();
	
		String out = loadString(is);
		conn.disconnect();
		return out;
	}

	protected JSONObject getJSON(String urlTail) throws IOException {
		HttpURLConnection conn = mkConn(urlTail);
		conn.setRequestMethod("GET");
	
		checkResponceCode(conn);
	
		InputStream is = conn.getInputStream();
	
		JSONObject out = loadJSON(is);
		conn.disconnect();
		return out;
	}

	protected JSONObject post(String urlTail, String postData) throws IOException {
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

	protected static final String UNIT_LIST_REST_PATH = "rest/units/%s/list";
	protected static final String OTHER_LIST_REST_PATH = "rest/%s/list";
	protected static final String OTHER_DICT_REST_PATH = "rest/%s/dict";

}
