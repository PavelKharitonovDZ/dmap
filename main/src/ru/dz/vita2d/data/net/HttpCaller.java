package ru.dz.vita2d.data.net;

import java.io.BufferedReader;
import java.io.File;
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

import org.json.JSONObject;
import org.json.JSONTokener;

import ru.dz.vita2d.data.DataConvertor;

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


	public void downloadFile(String filePath, String fileUrl, long modified) throws IOException 
	{
		HttpURLConnection conn = null;
		FileOutputStream os = null;
		
		try {
		
		URL url = new URL(baseUrl + "/" + fileUrl );
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Accept", "application/json");
		
		conn.setRequestMethod("GET");
		
		checkResponceCode(conn);
	
		InputStream is = conn.getInputStream();
	
		
		File fn = new File(filePath); 
		File dir = fn.getParentFile();
		dir.mkdirs();

		os = new FileOutputStream(fn);

		byte[] buf = new byte[1024];
		int len;
		while((len = is.read(buf)) > 0) {
			os.write(buf, 0, len);
		}

		os.close();
		os = null;
		// TODO something is wrong with time, it comes in negaive
		//System.out.println("modif time="+DataConvertor.msecToString(modified));
		//fn.setLastModified(modified);
		
		} catch (IOException e) {
			// TODO log me
			//e.printStackTrace();
			throw e;
		} finally {
			if( os != null ) os.close();
			if( conn != null ) conn.disconnect();
		}
	}
	
}
