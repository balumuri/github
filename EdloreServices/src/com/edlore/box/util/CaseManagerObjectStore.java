package com.edlore.box.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.ibm.casemgmt.api.context.P8ConnectionCache;
import com.ibm.casemgmt.api.context.SimpleP8ConnectionCache;

public class CaseManagerObjectStore {

	final Logger logger = Logger.getLogger(CaseManagerObjectStore.class.getName());
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("com/edlore/resourses/applicationProperties");
	
	public static ObjectStore getCMObjectStore(){
	
		System.out.println("CaseCreation.getCases()");
		
		P8ConnectionCache connectionCache = new SimpleP8ConnectionCache();
		Connection connection =  connectionCache.getP8Connection(resourceBundle.getString("URI"));
		Subject subject  = UserContext.createSubject(connection,resourceBundle.getString("USERNAME"),resourceBundle.getString("PASSWORD"),null);
		UserContext uc = UserContext.get();
	    uc.pushSubject(subject);
	    Locale origLocale = uc.getLocale();
	    uc.setLocale(origLocale);
		System.out.println("-------------------");
	  //  CaseMgmtContext context = CaseMgmtContext.set(new CaseMgmtContext(new SimpleVWSessionCache(), connectionCache));
	    Domain _fnDomain = Factory.Domain.fetchInstance(connection, null, null);
	    System.out.println("Domain  :::"+_fnDomain.get_Name());
		ObjectStore objectStore1 = Factory.ObjectStore.fetchInstance(_fnDomain, resourceBundle.getString("OS"), null);
		
		return objectStore1;
		
	}
}