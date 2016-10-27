package com.edlore.amazon.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WatsonExplorerClient {

	
	public static void main(String[] args) {
		

		URL url;
		
		try {
		url = new URL("http://watsonex:10025/vivisimo/cgi-bin/query-meta.exe?v%3asources=CdsWexCollection&query=Cause&Problem%20DOCUMENT_KEY%3a%22%7bF344BFD0-9BE1-49F5-A59B-7F2404E27F1B%7d%22&v%3aframe=cache&search-vfile=viv_XEH6O5&search-state=%28root%29%7croot&");
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("Accept", "text/html");

		System.out.println("Before if ----- ");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		
		
		Object object =conn.getContent();
		OutputStream outputStream = null;
		
		System.out.println(object.getClass());
		
		InputStream inputStream = conn.getInputStream();
		
		outputStream = new FileOutputStream(new File("D://cds//response.html"));
		
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		System.out.println("Done!");
		
		System.out.println("successfully called----------- ");
		
		
	}catch (Exception e) {
		e.printStackTrace();
	}
}

}
