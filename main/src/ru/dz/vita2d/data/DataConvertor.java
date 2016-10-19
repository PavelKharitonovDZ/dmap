package ru.dz.vita2d.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Generate user-readable representation for a field
 * @author dz
 *
 */
public class DataConvertor {

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
			long unixSeconds = Long.parseLong(value);

			//Date date = new Date(unixSeconds*1000L);
			Date date = new Date(unixSeconds);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			value = sdf.format(date);
			//System.out.println("date time "+value);
			break;
		}

		case "integer":
		case "positiveInteger":
		case "positiveShort":
		case "text":
		case "string":
		case "date":
		case "shortName":
		case "fullName":
		case "unitName":
			// ignore
			break;

		default:
			System.out.println("unknown type "+type);
			break;
		}

		return value;
	}

	public static String booleanReadableValue(String value) {
		if( value.equalsIgnoreCase("true") )
			value = "Да";
		else
			value = "Нет";
		return value;
	}

}
