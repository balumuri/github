package com.edlore.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

/**
 * @author Sowjanya B
 *
 * Used to test the either provider is working or not
 * a simple get method
 */
@Path(value="/ping")
public class PingService {

	final Logger logger = Logger.getLogger(UploadService.class.getName());

	
	@GET
	public Response ping()
	{
		System.out.println("ping success");
		
		logger.info("ping success");
		
		return Response.status(200).entity("Ping Successful....").build();
	}
	
}
