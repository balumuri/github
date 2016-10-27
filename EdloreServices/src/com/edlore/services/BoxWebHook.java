package com.edlore.services;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxFolder.Info;
import com.box.sdk.Metadata;
import com.edlore.box.util.CDSFilenetCEConnection;
import com.edlore.box.util.CaseManagerObjectStore;
import com.edlore.box.util.EdloreBoxAPIConnection;
import com.edlore.box.util.GetFolderInfo;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.mitsind.az.util.ContentTypeMap;

@Path(value="/webhook")
public class BoxWebHook {

	final Logger logger = Logger.getLogger(BoxWebHook.class.getName());
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("com/edlore/resourses/applicationProperties");

	
	@GET	
	@Path("/box")
	public Response boxHook(@QueryParam("hook")String item_id, @QueryParam("type")String event_type, @QueryParam("parent")String item_parent_folder_id, @QueryParam("item")String item_type)
	{
		
		logger.info(" item id --"+ item_id +" event type ---- "+event_type +" parent folder id ------ "+ item_parent_folder_id+" item_type "+item_type);
		if(item_id == null || "".equals(item_id) && event_type == null || "".equals(event_type) && item_parent_folder_id == null || "".equals(item_parent_folder_id) && item_type == null || "".equals(item_type))
		{
			return Response.status(400).build();			
		}
		
		if(event_type.equals("created") && item_type.equals("folder"))
		{
			logger.info("enter into if event type "+ event_type +" item type "+ item_type);
			
			 BoxDeveloperEditionAPIConnection api = null;
			 BoxFolder folder = null;
			 		 
			 
			 api = EdloreBoxAPIConnection.getAppUserConnection();
			 
			 folder  = new BoxFolder(api, item_id);
			
			 Info info =    folder.getInfo();
			 logger.info("folder name  "+ folder.getInfo().getName()+" parent folder name ----- "+info.getParent().getID());	
			if(!"11523321485".equalsIgnoreCase(info.getParent().getID()))
			{
				System.out.println(" With in return ----------"+info.getParent().getID());
				 return Response.status(200).build(); 
			}
			 
				createFolderInFilenet(folder.getInfo().getName());	 		 
			 
			
		}else
			if(event_type.equals("uploaded") && item_type.equals("file"))
			{				
				logger.info("enter into else event type "+ event_type +" item type "+ item_type);
				 BoxDeveloperEditionAPIConnection api = null;
				 BoxFile file = null;
				 BoxFile.Info info=null;
				api = EdloreBoxAPIConnection.getAppUserConnection();	
				file = new BoxFile(api, item_id);
				
				  info = file.getInfo();	
				  String parentFolderName =  info.getParent().getName();
				  logger.info("parent folder is  --- "+parentFolderName);
				  BoxFolder parentFolder = new BoxFolder(api, info.getParent().getID());
				  Info parentFolderInfo = parentFolder.getInfo();	
				  
				  logger.info("parent folder is  --- "+parentFolderInfo.getParent().getID());
				  
				  if(!"11523321485".equals(parentFolderInfo.getParent().getID()))
				  {
					  logger.info("with in not parent folder ");
					  return Response.status(200).build(); 
					  
				  }
			
				  logger.info("with in yes parent folder ");
				  uploadBoxFileToFilenetAsDoc(api, item_id);
				 
				  Metadata metadata = null;
				  try{
					  metadata = file.getMetadata();
				  } catch (Exception e) {
					
					  logger.info("no custom metadata -------- ");
					  uploadBoxFileToFilenetAsDoc(api, item_id);
					  
					  return Response.status(200).build(); 
				 }
					logger.info("mata data ID is =========="+ metadata);
					
					logger.info("mata data ID is =========="+ metadata.toString());
					logger.info("mata data ID is =========="+ info.getName());
					 
				
				 uploadBoxFileToFilenetAsMeatadata(metadata.toString(), parentFolderName);	
					  
			}
					
		return Response.status(200).build();
	}
	
	
	@GET	
	@Path("/filenet")
	public Response filenetHook(@QueryParam("id")String id)
	{
		
		logger.info("filent hook called successfully ---------- Doc Id "+id);
		
		ObjectStore cmObjectStore = null;
		Document document = null;
		FolderSet folderSet = null;
		String referenceNumber = null;
		BoxDeveloperEditionAPIConnection api = null;	
		
		BoxFolder accountOpenFolder = null;
		BoxFolder referenceNoFolder = null;
		
		api = EdloreBoxAPIConnection.getAppUserConnection();
		cmObjectStore = CaseManagerObjectStore.getCMObjectStore();		
		document = Factory.Document.fetchInstance(cmObjectStore, id, null);		
		folderSet = document.get_FoldersFiledIn();
		
		String docTitle = document.getProperties().getStringValue("DocumentTitle");
		
		logger.info("docTitle -------------- "+ docTitle);
		Iterator<Folder> folders = folderSet.iterator();
		while (folders.hasNext()) {

		Folder folder =	folders.next();
		
			Folder docFolder = Factory.Folder.fetchInstance(cmObjectStore, folder.get_Id(), null);
			referenceNumber=docFolder.getProperties().getStringValue("AMT_ReferenceNumber");
		
			logger.info("Reference number is --------------- "+referenceNumber);
		}
		
		accountOpenFolder = new BoxFolder(api, "11523321485");
		
		referenceNoFolder = GetFolderInfo.getFolderInfo(api, accountOpenFolder, referenceNumber);
		
		System.out.println("referenceNoFolder "+ referenceNoFolder);
	ContentElementList contentElementList = document.get_ContentElements();
		InputStream inputStream = null;
				 	
		
		if(!contentElementList.isEmpty())
		{
			logger.info("Content is not empty ----- ");					
			Iterator<ContentTransfer> iterator = contentElementList.iterator();
			
			while (iterator.hasNext()) {
				ContentTransfer transfer =  iterator.next();				
				logger.info("content type "+transfer.get_ContentType());				
				inputStream = transfer.accessContentStream();
				
				logger.info("content retrival name ---- "+transfer.get_RetrievalName());
				referenceNoFolder.uploadFile(inputStream, transfer.get_RetrievalName());
		 }}
	
				 logger.info("success -----------------------------");
			
			
		
		
		return Response.status(200).build();
		
	}
	
	
	private void uploadBoxFileToFilenetAsMeatadata(String jsonString,  String parentFolderName)
	{
		ObjectStore objectStore = null;
		Document document = null;
		boolean flag = false;
		objectStore = CDSFilenetCEConnection.getObjectStore();
		
		Folder folder = Factory.Folder.fetchInstance(objectStore,"/"+resourceBundle.getString("FolderName")+"/"+parentFolderName,null);		  
		document = Factory.Document.createInstance(objectStore,resourceBundle.getString("MetadataClassId"),null);
		 DocumentSet documentSet = folder.get_ContainedDocuments();
		 
			Iterator<Document> iterator = documentSet.iterator();
			Document document2 = null;
			ContentElementList contentElementList2 = null;
			while (iterator.hasNext()) {
				document2 = iterator.next();
				System.out.println("Document name ---------------------------- "+document2.get_Name());
				if("metadata".equalsIgnoreCase(document2.get_Name()))
				{
					
					document2.checkout(ReservationType.EXCLUSIVE, null, null, null);
					document2.save(RefreshMode.REFRESH);
					document = (Document) document2.get_Reservation();	
					flag = true;
					break;
				}
				
			}
		
		
		  document.getProperties().putObjectValue("DocumentTitle","metadata");
		  ContentElementList contentElementList = Factory.ContentElement.createList();
		  ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
		  
		  //set the content, content type and add to contentElementList
		  try {		  
			
			contentTransfer.setCaptureSource(new ByteArrayInputStream(jsonString.getBytes()));
			
		
		  contentTransfer.set_ContentType("application/json");
		  contentElementList.add(contentTransfer);
		  
		  /* add contentElementList to the document,
		   * checkIn the document 
		   * set the MimeType of document and save the document*/
		  document.set_ContentElements(contentElementList);
		  document.checkin(AutoClassify.AUTO_CLASSIFY,CheckinType.MAJOR_VERSION);
		  document.set_MimeType("application/json");
		  document.save(RefreshMode.REFRESH);
		  System.out.println("After saving Document");    
		 
		 
		  
		  if(!flag)
			{
          //filing a document into folder by using ReferentialContainmentRelationship(rcr) and save the rcr
		  ReferentialContainmentRelationship rcr = folder.file(document, AutoUniqueName.AUTO_UNIQUE, "metadata", DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
	      rcr.save(RefreshMode.REFRESH);
	      System.out.println("After filing document in folder");
			}
		  } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	void uploadBoxFileToFilenetAsDoc(BoxDeveloperEditionAPIConnection api, String fileId)
	{
		logger.info("with in upload to filenet as doc -------------" );
		downloadFromBox(api, fileId);
	}
	
	private void downloadFromBox(BoxDeveloperEditionAPIConnection api, String fileId)
	{
		
		BoxFile file = null;
		BoxFile.Info info=null;
		OutputStream outputStream = null;
		FileInputStream fis = null;
		
		try {
			
			logger.info("Connection created succesfully");
		  
		    file= new BoxFile(api, fileId);
			info = file.getInfo();
			
			logger.info("file name is:"+info.getName());

			 String filePath=info.getName();
			 
				logger.info("file path is:"+filePath);

			
			 outputStream = new FileOutputStream(filePath);
			
			 fis = new FileInputStream(filePath);
			  logger.info("before downloading");
		      
			  file.download(outputStream);
			  
			  uploadToFilenetAscontent(api, fis, info);
			  
			  logger.info("download succesfully");
			
			}catch (Exception e) {
				e.printStackTrace();
			}		
		
	}
	
	
	private void uploadToFilenetAscontent(BoxDeveloperEditionAPIConnection api, FileInputStream fis, BoxFile.Info fileInfo)
	{
		ObjectStore objectStore = null;
		String docName = null;
		boolean flag = false;
		objectStore = CDSFilenetCEConnection.getObjectStore();
		docName = getFileNameWithoutExtesion(fileInfo.getName());
		logger.info("file name "+ fileInfo.getName()+" parent folder name "+ fileInfo.getParent().getName());
		
		Folder fileNetFolder = Factory.Folder.fetchInstance(objectStore, "/BoxtoFilenetSync/"+fileInfo.getParent().getName(), null);
		logger.info("folder info in filenet ------- "+fileNetFolder.get_FolderName());
		
		Document document = Factory.Document.createInstance(objectStore,resourceBundle.getString("AccountOpeningClassId"),null);
		
		 DocumentSet documentSet = fileNetFolder.get_ContainedDocuments();
		 
			Iterator<Document> iterator = documentSet.iterator();
			Document document2 = null;
		//	ContentElementList contentElementList2 = null;
			while (iterator.hasNext()) {
				document2 = iterator.next();
				logger.info("Document name ---------------------------- "+document2.get_Name());
				if(docName.equals(document2.get_Name()))
				{
					logger.info("Document alredy exists --------------- "+document2.get_Name());
					return;
					/*document2.checkout(ReservationType.EXCLUSIVE, null, null, null);
					document2.save(RefreshMode.REFRESH);
					document2.getProperties().getStringValue("");
					
					document = (Document) document2.get_Reservation();	
					flag = true;
					break;*/
				}
				
			}	
		
		
		document.getProperties().putObjectValue("DocumentTitle",docName);
		  ContentElementList contentElementList = Factory.ContentElement.createList();
		  ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
		  
		  //set the content, content type and add to contentElementList
		  try {		  
			
			contentTransfer.setCaptureSource(fis);
			
		 logger.info("box file extension ------- "+fileInfo.getExtension());
		 logger.info("box file to string ------- "+fileInfo.getResource().toString());
		  contentTransfer.set_ContentType(getFileMime(fileInfo.getName()));
		  
		  contentElementList.add(contentTransfer);
		  
		  /* add contentElementList to the document,
		   * checkIn the document 
		   * set the MimeType of document and save the document*/
		  document.set_ContentElements(contentElementList);
		  document.checkin(AutoClassify.AUTO_CLASSIFY,CheckinType.MAJOR_VERSION);
		  
		  document.set_MimeType(getFileMime(fileInfo.getName()));
		  document.save(RefreshMode.REFRESH);
		  System.out.println("After saving Document"); 		  
		  
		  if(!flag)
			{
			  //filing a document into folder by using ReferentialContainmentRelationship(rcr) and save the rcr
			  ReferentialContainmentRelationship rcr = fileNetFolder.file(document, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
			  rcr.save(RefreshMode.REFRESH);
			}
	      System.out.println("After filing document in folder");
	      
		  } catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	private void createFolderInFilenet(String folderName)
	{
		ObjectStore objectStore = null;
		
		objectStore = CDSFilenetCEConnection.getObjectStore();
		logger.info("object store is -------------"+ objectStore);
		//create MyTest folder instance
		  Folder folder = Factory.Folder.fetchInstance(objectStore,resourceBundle.getString("FolderId"),null);
		  
		  logger.info("folder name is "+folder.get_FolderName());
		  
		  Folder subfolder =  folder.createSubFolder(folderName);
		  
		  subfolder.save(RefreshMode.REFRESH);
		  System.out.println("success full----------");
	}
	
	private  String getFileNameWithoutExtesion(String fileName)
	{		
		String name = null;		
		name = fileName.substring(0, fileName.length()-4);
		
		return name;
	}	
	
	private static String getFileMime(String fileName)
	{
		String extension = null;
		extension = fileName.substring(fileName.length()-3, fileName.length());	
		extension = ContentTypeMap.getMimeType(extension);			
		return extension;		
	}	
	
	
	
}