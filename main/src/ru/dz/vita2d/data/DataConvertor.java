package ru.dz.vita2d.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author dz
 *
 */
public class DataConvertor {

	/**
	 * 
	 * <p>Generate user-readable representation for a field.</p>
	 * 
	 * @param type field type (model calls it domain)
	 * @param value value as read from JSON
	 * @return converted value
	 */
	public static String readableValue( String type, String value )
	{
		if( type == null ) return value;

		switch(type)
		{
		case "bool":
			value = booleanReadableValue(value);
			break;

		case "datetime":
		{
			long unixMilliSeconds = Long.parseLong(value);

			value = msecToString(unixMilliSeconds);
			//System.out.println("date time "+value);
			break;
		}

		case "icon": // TODO process me?
		case "email":
		case "mean":
		case "event":
		case "birthDate":
		case "obj":
		case "phone":
		case "sysname":
		case "name":
		case "lastName":
		case "firstName":
		case "job":
		case "file":
		case "reflist":
		case "unit":
		case "currency":
		case "multi":
		case "integer":
		case "positiveInteger":
		case "positiveShort":
		case "text":
		case "string":
		case "date":
		case "shortName":
		case "fullName":
		case "unitName":
		case "reference":
		case "sysreference":
		case "division": // todo - modify parser below to depend on type?
			// ignore
			break;

		default:
			System.out.println("readableValue: unknown type "+type);
			break;
		}

		return value;
	}

	public static String msecToString(long unixMilliSeconds) 
	{
		Date date = new Date(unixMilliSeconds);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String value = sdf.format(date);
		return value;
	}

	public static String booleanReadableValue(String value) {
		if( value.equalsIgnoreCase("true") )
			value = "Да";
		else
			value = "Нет";
		return value;
	}


	/**
	 * Disassemble complex value represented with JSON structure.
	 * 
	 * @param fieldName field name as read from JSON
	 * @param type field type (model calls it domain)
	 * @param sub data as from JSON 
	 * @param out consumer to eat parsed name/value pairs.
	 */
	public static void parseComplexVal( String fieldName, JSONObject sub, BiConsumer<String,String> out )
	{
		if( sub.has("name") )
		{
			Object data = sub.get("name");
			if (data instanceof String) 
			{
				String asString = (String) data;
				out.accept(fieldName, asString);
			}
			else
			{
				System.out.println("parseComplexVal unknown type "+data.getClass());
			}
		}


		switch(fieldName)
		{
		case "division":
			l2field("filial", sub, out);
			l2field("center", sub, out);
			break;

		case "location":
			if( sub.has("place") )
			{
				JSONObject l2data = sub.getJSONObject("place");
				out.accept("lat", Double.toString(l2data.getDouble("lat")) );
				out.accept("lng", Double.toString(l2data.getDouble("lng")) );
			}
			break;			
		}


	}

	private static void l2field(String l2fn, JSONObject sub, BiConsumer<String, String> out) {
		if( sub.has(l2fn) )
			parseComplexVal(l2fn, sub.getJSONObject(l2fn), out);
	}


	public static void parseArrayVal( String fieldName, JSONArray arr, BiConsumer<String,String> out )	
	{
		arr.forEach( jo -> {
			if (jo instanceof JSONObject) {
				JSONObject joo = (JSONObject) jo;
				parseComplexVal(fieldName, joo, out);
			}
			else
			{
				System.out.println("parseArrayVal unknown type "+jo.getClass());
			}
		});
	}


	public static void parseAnything( String fieldName, Object data, BiConsumer<String,String> out )
	{
		if(
				(data instanceof String)
				|| (data instanceof Integer)
				|| (data instanceof Long)
				|| (data instanceof Double)
				|| (data instanceof Boolean)
				) 
		{
			out.accept(fieldName, data.toString() );
		}
		else if(data instanceof JSONObject) 
		{
			JSONObject sub = (JSONObject) data;
			parseComplexVal(fieldName, sub, out);
		}
		else if(data instanceof JSONArray) 
		{
			JSONArray sub = (JSONArray) data;
			parseArrayVal(fieldName, sub, out);
		}
		else
		{
			System.out.println("parseAnything unknown class = "+ data.getClass()+" "+fieldName+"="+data );
		}

	}

	public static boolean isNumericDomain(String domain) {
		switch(domain)
		{
		case "currency":
		case "integer":
		case "positiveInteger":
		case "positiveShort":
			return true;
		default:
			return false;
		}
	}

	public static boolean isDateOrTime(String domain) {
		switch(domain)
		{
		case "date":
		case "datetime":
			return true;
		default:
			return false;
		}
	}

	/** 
	 * TODO unused and wrong 
	 * @param domain
	 * @return True if fields of this domain can't be used as data filters (directly).
	 */
	public static boolean isNonFilterable(String domain) {
		switch(domain)
		{
		case "reference":
		case "sysreference":
		case "reflist":
			return true;
		default:
			return false;
		}
	}

	public static boolean isReference(String domain) {
		switch(domain)
		{
		case "reference":
		case "sysreference":
		//case "reflist": // TODO how do we process?
			return true;
		default:
			return false;
		}
	}

	
}
