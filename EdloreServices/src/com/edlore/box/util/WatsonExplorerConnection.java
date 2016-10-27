package com.edlore.box.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import velocity.VelocityPort;
import velocity.VelocityService;
import velocity.soap.Authentication;

public class WatsonExplorerConnection {

	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("com/edlore/resourses/applicationProperties");
	private static  VelocityService velocityService = null;
	private static VelocityPort velocityPort = null;
	
	final static Logger logger = Logger.getLogger(WatsonExplorerConnection.class.getName());
	private static VelocityService getWatsonService()
	{
		
		if( velocityService == null)
		{
			URL url = null;
			QName qName = null;		
			
			try {
			url = new URL("http://122.183.97.242:10025/vivisimo/cgi-bin/velocity.exe?v.app=api-soap&use-types=true&wsdl=1&specialize-for=&use-types=true&");
			qName = new QName("urn:/velocity", "VelocityService");			
			velocityService = new VelocityService(url, qName);
			logger.info("With in get velocity service when it is null ");
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return velocityService;
			}		
		}
		logger.info(" Retuning velocity service ");
		return velocityService;		
	}
	
	public static VelocityPort getVelocityPort()
	{
		VelocityService vService = null;
		
		if(velocityPort == null)
		{
			vService = getWatsonService();
			velocityPort = vService.getVelocityPort();
			((javax.xml.ws.BindingProvider) velocityPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
				    "http://122.183.97.242:10025/vivisimo/cgi-bin/velocity.exe?v.app=api-soap&");
			logger.info(" Retuning velocity port when it is null ");
		}
		logger.info(" Retuning velocity port ");
		return velocityPort; 
	}
	
	
	public static Authentication getAuthenticationForWatson()
	{
			Authentication authentication = new Authentication();
	        authentication.setUsername(resourceBundle.getString("UserName"));
	        authentication.setPassword(resourceBundle.getString("Password"));
	        
	        System.out.println("authentication :"+authentication);
	        
	       return authentication;	
	}
	
}