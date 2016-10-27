package com.mitsind.az.util;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMap {

	public static Map<String, String> values = new HashMap<String, String>();
	
	static {	
		
		values.put("jpg", "image/jpg");
		values.put("pdf", "application/pdf");
		values.put("png", "image/png");
		
	}
	
	public static String getMimeType(String extension)
	{		
		return values.get(extension);
	}
}