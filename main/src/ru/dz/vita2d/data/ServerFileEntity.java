package ru.dz.vita2d.data;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONObject;

import javafx.application.Platform;
import ru.dz.vita2d.data.model.JsonParser;
import ru.dz.vita2d.data.net.IRestCaller;
import ru.dz.vita2d.data.net.RestCaller;

/**
 * Represent data (document) file stored on server and in a local copy.
 * @author dz
 *
 */
public class ServerFileEntity extends JsonParser {

	private String entity;
	private int id;
	private String description;
	/**  File name extension. */
	private String extension;
	private String typeName;
	private String uri;
	private long createdMsec;

	public ServerFileEntity(JSONObject fileInfo) {
		entity = fileInfo.getString("linkEntity");
		id = fileInfo.getInt("id");
		uri = fileInfo.getString("uri");
		
		description = tryLoadString(fileInfo, "description");
		extension = tryLoadString(fileInfo, "extension");
		
		JSONObject stamp = fileInfo.getJSONObject("stamp");
		createdMsec = 1000L * stamp.getInt("created");
		
		if( fileInfo.has("type"))
		{
			JSONObject type = fileInfo.getJSONObject("type");
			typeName = tryLoadString(fileInfo, "name");
		}
	}

	/** On server */
	private String fileUrl() {
		return String.format("/rest/%s/files/%d/%s", entity, id, URLEncoder.encode(uri) );
	}

	/** In local FileSystem */
	private String filePath() {
		return String.format("/server-files/%s/%d/%s", entity, id, uri );
	}
	
	public void download(IRestCaller rc, String fileBasePath ) throws IOException
	{
		// TODO check that it is all right with modif time/date
		System.out.println("modif time="+DataConvertor.msecToString(createdMsec));
		rc.downloadFile( fileBasePath+"/"+filePath(), fileUrl(), createdMsec );
	}
	
	public void systemRunFileProgram(String fileBasePath) throws IOException
	{
		String cmd = "cmd /c start "+fileBasePath+"/"+filePath();
		System.out.println("Run '"+cmd+"'");
		
		/*
		Runtime rt = Runtime.getRuntime();
		
		//rt.exec(cmd);

		String[] cmdarray = new String[4];
		
		cmdarray[0] = "cmd";
		cmdarray[1] = "/c";
		cmdarray[2] = "start";
		cmdarray[3] = fileBasePath+"/"+filePath();
		
		rt.exec(cmdarray);
		*/
		
		Desktop desktop = Desktop.getDesktop();
		desktop.open(new File(fileBasePath, filePath()));
	}
	
	@Override
	public String toString() {
		return filePath();
	}
}
