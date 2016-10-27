package com.edlore.box.util;

import org.apache.log4j.Logger;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;

/**
 * @author Sowjanya B
 *
 */
public class GetFolderInfo {

	final static Logger logger = Logger.getLogger(GetFolderInfo.class);
	
	public static BoxFolder getFolderInfo(BoxDeveloperEditionAPIConnection api,BoxFolder boxFolder, String folderName)
	{
		 logger.info("inside get folder info ---"+ boxFolder.getInfo().getName());
		 
		 BoxFolder folder = null;
		 
		 // iterating the items avalilable inside the boxfolder
	       for (BoxItem.Info itemInfo : boxFolder) {
	    	   logger.info("itemInfo.getName():::"+itemInfo.getName()+"::folder Name::"+folderName);
	    	   
	     // This condition is used to check whether the given assetid is valid or not. If not valid throws an exception
	       	if(folderName.equalsIgnoreCase(itemInfo.getName())){
	       		// Checking whether the item is of instance BoxFolder.Info
	       		
	       		logger.info("Enter into if equals method.........");
	       		
	       		if (itemInfo instanceof BoxFolder.Info) {
	       			folder = (BoxFolder) itemInfo.getResource();
	       			logger.info("Child root folder info is :: "+ folder.getInfo().getName());
	       			
	       		    return folder;
					}
	       		}
	       }
		return folder;
	}
}
