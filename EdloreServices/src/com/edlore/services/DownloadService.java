package com.edlore.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFile;
import com.edlore.box.util.EdloreBoxAPIConnection;

/**
 * @author Sowjanya B
 *
 */
@Path(value="/download")
public class DownloadService{
	/**
	 * @param manualId
	 * @return response 
	 * 
	 *  By taking input as manualId it connects to box and then returns 
	 *  the manual based on manual Id
	 */
	final Logger logger = Logger.getLogger(DownloadService.class);
	@GET
	@Path("{manualId}")
	@Produces({"application/pdf", "image/png", "image/tif", "image/jpeg", "video/mp4"})
	public Response getFile(@PathParam("manualId") String manualId) {

		logger.info("Enter get file method manualId is ::"+ manualId);
		
		// As now this url is hardcoded, from local file syatem it returns the pdf
		if(manualId == null || "".equals(manualId))
		{
			return Response.status(400).entity("requested manual id unavilable").build();
		}
		//String ZIP_FILE_PATH = "C:\\edlorefolder\\"+ manualId;
		//  File f = new File(ZIP_FILE_PATH);
			BoxFile file = null;
			BoxDeveloperEditionAPIConnection api = null;
			BoxFile.Info info=null;
			OutputStream outputStream = null;
			 FileInputStream fis = null;
			 
			 try {
				 
			api = EdloreBoxAPIConnection.getAppUserConnection();
			if(api == null)
			{
				return Response.status(400).entity("Unable to connect to box ").build();
			}
			logger.info("Connection created succesfully");
		  
		    file= new BoxFile(api, manualId);
			info = file.getInfo();
			
			logger.info("file name is:"+info.getName());

			 String filePath=info.getName();
			 
				logger.info("file path is:"+filePath);

			
			 outputStream = new FileOutputStream(filePath);
			
			 fis = new FileInputStream(filePath);
			  logger.info("before downloading");
		      
			  file.download(outputStream);
			
			  logger.info("download succesfully");

			 
			  } catch(Exception e) {
				  if(e instanceof BoxAPIException)
					{
					  	logger.info("In download service box api exception -------"+ e );

						BoxAPIException exception = (BoxAPIException) e;	

						logger.info("Box stack trace is---------"+ exception);
						logger.info("with in exception block of get box connection response----------"+exception.getResponse()); 
		    			logger.info("with in exception block of get box connection locationzation message ----------"+exception.getLocalizedMessage());
		    			logger.info("with in exception block of get box connection message ----------"+exception.getMessage());
		    		    logger.info("with in exception block of get box connection response code ----------"+exception.getResponseCode());
		    			logger.info("with in exception block of get box connection ----------"+exception.toString());
		    			logger.info("with in exception block of get box connection----------"+exception.getCause());
						
						return Response.status(exception.getResponseCode()).entity(exception.getMessage()).build();
					}
				  logger.info("In download service exception is -------"+ e);

				  e.printStackTrace();
				  
			}finally {
				
			}
			

	     /** Return the reponse with status 200 and set the 
	      *  reponse header as content-disposition as inline so that browser client can able to disply 
	      *  the document or loads the document on browser 
	      *  
	      *  If content-disposition is attachment then it forces the browser to save document
	      *  in local system or it gets dowloaded
	      *  */
	    
	    return Response.ok(fis)
	            .header("Content-Disposition",
	                   "inline; filename="+info.getName()).build();
	}
}