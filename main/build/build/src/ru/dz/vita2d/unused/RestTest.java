package ru.dz.vita2d.unused;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RestTest {

	static String loadOut(InputStream is) throws IOException
	{
		StringBuilder sb = new StringBuilder();		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
	
		return sb.toString();
	}
	
	static void get() throws IOException
	{
		URL url = new URL("http://sv-web-15.vtsft.ru/orvd-test/rest/login");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		InputStream is = conn.getInputStream();

		String out = loadOut(is);
		System.out.println("Output from Server: "+out);

		conn.disconnect();
		
	}
	
	static void post(String postData) throws IOException
	{
		URL url = new URL("http://sv-web-15.vtsft.ru/orvd-test/rest/login");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json");

		// For POST only - START
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(postData.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		InputStream is = conn.getInputStream();

		String out = loadOut(is);
		System.out.println("Output from Server: "+out);

		
		dumpHeaders(conn);
		
		conn.disconnect();
		
	}

	private static void dumpHeaders(HttpURLConnection con) {
		Map<String, List<String>> h = con.getHeaderFields();
		for( String key : h.keySet() )
		{
			List<String> vals = h.get(key);
			
			for( String val : vals )
			{
				System.out.println(key + " = "+val);
			}
		}
	}
	
	public static void main(String[] args) 
	{

		try {
			System.out.println("Start\n");

			post("{\"login\":\"show\",\"password\":\"show\"}");
			
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}		

	}

}
