package com.edlore.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.edlore.services.AccountOpeningRefNo;
import com.edlore.services.AccountOpeningSearchService;
import com.edlore.services.BoxWebHook;
import com.edlore.services.DownloadService;
import com.edlore.services.GetManualUrl;
import com.edlore.services.PingService;
import com.edlore.services.UploadService;
import com.edlore.services.WatsonExpSearchService;



/**
 * @author Sowjanya B
 *
 * This configuration class will used by runtime rest servlet as long as project is
 * deployed the runtime rest servlet will read this class make available all resources
 * which are configured in this class otherwise rest servlet doesn't know about the 
 * resource classes and will not make it accessble
 * 
 */
public class EdloreApplicationConfig extends Application{

	 /** The default life cycle for resource class instances is per-request. 
	   *  The default life cycle for providers is singleton.
	   * */
	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	private Set<Object> singletons;
	
	
	// Default constructor 
	public EdloreApplicationConfig() {
		singletons = new HashSet<Object>();
		singletons.add(new UploadService());
		singletons.add(new PingService());
		singletons.add(new DownloadService());
		singletons.add(new GetManualUrl());
		singletons.add(new BoxWebHook());
		singletons.add(new AccountOpeningRefNo());
		singletons.add(new AccountOpeningSearchService());
		singletons.add(new WatsonExpSearchService());
	}


	/** Fields and properties of returned instances are injected with their declared
	  * dependencies (see Context) by the runtime prior to use.
	  * */ 
	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getSingletons()
	 */
	
	/*This make sure the the object of resources are one object for application*/
	@Override
	public Set<Object> getSingletons() {
		
		return singletons;
	}
	
}