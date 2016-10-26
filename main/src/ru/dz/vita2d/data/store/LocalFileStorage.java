package ru.dz.vita2d.data.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.function.Supplier;

import org.json.JSONObject;

public class LocalFileStorage {

	static final private String baseDir = System.getProperty("user.home")+"/.orvd2d";
	
	private static String makeFn(String name) {
		new File(baseDir).mkdirs();
		return baseDir+"/"+name;
	}

	public static String getBasedir() {		return baseDir;	}
	
	/** Save text */
	public static void saveToFile( String fileName, String data ) throws IOException
	{
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(makeFn(fileName));
			//os.write(data.getBytes());
			saveString(os, data);
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if( os != null )
				os.close();
		}
	}

	static private void saveString(OutputStream os, String data) throws IOException
	{
		OutputStreamWriter out = new OutputStreamWriter(os,"UTF-8");
		BufferedWriter bw = new BufferedWriter(out);
		bw.write(data);
		bw.close();
	}

	
	
	
	/** Load text */
	public static String loadFromFile( String fileName ) throws IOException
	{
		FileInputStream is = new FileInputStream(makeFn(fileName));

		String data = loadString(is);

		is.close();
		
		return data;
	}

	static private String loadString(InputStream is) throws IOException
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

	public static JSONObject JSONCachedLoader( RestSupplier<JSONObject> s, String fileName )
	{
		RestSupplier<String> ss = () -> {return s.get().toString();};
		String out = cachedLoader( ss, fileName );
		return new JSONObject(out);
	}
	
	public static String cachedLoader( RestSupplier<String> s, String fileName )
	{
		try {
			String data = s.get();
			saveToFile(fileName, data); // Save local copy
			return data;
		} catch (IOException e) {
			// Failed to get data, attempt to use cached.
			// TODO mark we're offline
			try {
				String data = loadFromFile(fileName);
				return data;
			} catch (IOException e1) {
				System.out.println("no access to server and no local copy for "+fileName);
				return null;
			}
		}
	}
	
}
