package ru.dz.vita2d.data;

public class DataConvertor {

	public static String readableValue( String type, String value )
	{
		boolean isBool = false;
		
		if( type.equalsIgnoreCase("bool")) 
			isBool = true;
		
		if(isBool)
			value = booleanReadableValue(value);

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
