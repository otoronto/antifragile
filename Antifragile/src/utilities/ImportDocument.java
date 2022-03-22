package utilities;

package com.mapfre.common;

import java.io.File;
import java.util.UUID;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.DfTime;
import com.mapfre.constract.Content;
import com.mapfre.constract.ListOfImportedDocuments;
import com.mapfre.constract.ListOfImportedDocumentsDetail;
import com.mapfre.constract.UserAuthInfo;
import com.mapfre.singleton.Variable;

public class ImportDocuments {

  private static final String ROOT_CABINET = "/Webservice Documents";
  private static final String HEALTH_CABINET = "/HealthTempAgito";
  private static final String FAX_FOLDER= "/Fax Documents/Mail Messages";
  private static final String ATTACHMENT_FOLDER= "/Fax Documents/Attachments";
  private static final String REIMBURSEMENT_CABINET = "/Reimbursement";

  public ListOfImportedDocuments start(String folderpath, Content content, UserAuthInfo authInfo,String fullPath) throws DfException {
    CreateSession call = new CreateSession();
    // "sourcesystem":"Reimbursement"
    IDfSession session = call.privateGetSession();
    try {
      session.beginTrans();
      File folder = new File(folderpath);
      
      File[] listOfFiles = folder.listFiles();
      UUID uuid = UUID.randomUUID();
      String folderId = createFolder(session, uuid.toString(), authInfo, content);
      ListOfImportedDocuments documents = new ListOfImportedDocuments();
      
      content.setProposalnumber(content.getProposalnumber() == null ? "" : content.getProposalnumber());
      if (content.getProvisiontype()!=null && content.getProvisiontype().equalsIgnoreCase("Provisions")) {	       
          for (File file : listOfFiles) {
            if (file.isFile()) {
              DfLogger.debug(this, "File Name ->" + file.getName(), null, null);
              String attachmentId= createAttachment(session, file.getName(), fullPath);
              String docId=  createProvisionDocument(session, file.getName(), fullPath, content,  attachmentId);
                          
              ListOfImportedDocumentsDetail detail = new ListOfImportedDocumentsDetail();
              detail.setObjectname(file.getName());
              detail.setRobjectid(docId);
              documents.putDocuments(detail);
            }
          }
      }
      else{        
          for (File file : listOfFiles) {
            if (file.isFile()) {
              DfLogger.debug(this, "File Name ->" + file.getName(), null, null);
              String docId=createDocument(session, file.getName(), folderpath + "/" + file.getName(), folderId, content, authInfo);
              ListOfImportedDocumentsDetail detail = new ListOfImportedDocumentsDetail();
              detail.setObjectname(file.getName());
              detail.setRobjectid(docId);
              documents.putDocuments(detail);
            }
          }
      }
    	  


      session.commitTrans();
      
      return documents;
    } catch (DfException ex) {
      DfLogger.error(this, "Import Start Error {0}", new Object[] {folderpath}, ex);
      session.abortTrans();
      throw ex;
    } finally {
      call.releasesession();
    }
  }

  private String getCabinet(UserAuthInfo authInfo, Content content) {
    String cabinetName = "";

    if (isAgitoUser(authInfo))
      cabinetName = HEALTH_CABINET;
    else if (isReimbursementUser(content))
      cabinetName = REIMBURSEMENT_CABINET;
    else
      cabinetName = ROOT_CABINET;

    return cabinetName;
  }

  private String createFolder(IDfSession session, String foldername, UserAuthInfo authInfo, Content content) throws DfException {
    IDfFolder folder = (IDfFolder) session.newObject("dm_folder");

    if (folder != null) {
      folder.setObjectName(foldername);
      folder.link(getCabinet(authInfo, content));
      DfLogger.debug(this, "Folder {0} is being saved...", new Object[] {foldername}, null);
      folder.save();
      String folderId = folder.getObjectId() != null ? folder.getObjectId().getId() : null;
      DfLogger.debug(this, "Folder {0} has been saved! Id : {1}", new Object[] {foldername, folderId}, null);
      return folderId;
    }
    return null;
  }

  private String createDocument(IDfSession session, String documentName, String documentPath, String folderId, Content content, UserAuthInfo authInfo) throws DfException {
    String extension = documentName.substring(documentName.lastIndexOf('.') + 1);
    String dctmExtension = Variable.INSTANCE.getExtensions().get(extension.toLowerCase());
    DfLogger.debug(this, "Extension -> DCTM: [{0}] -> [{1}]", new String[] {extension.toLowerCase(), dctmExtension}, null);
    if (dctmExtension == null || dctmExtension.isEmpty()) {
      DfException exceptionExtensionNotSupported = new DfException();
      String errorMessage = String.format("No such supported Documentum content type '%s' found for file '%s'! Extension '%s' may not be supported.", dctmExtension, documentName, extension);
      exceptionExtensionNotSupported.setMessage(errorMessage);
      DfLogger.error(this, errorMessage, null, exceptionExtensionNotSupported);
      throw exceptionExtensionNotSupported;
    }
    String documentType = "mtr_document";

    if ("lega_user".equals(authInfo.getUserName())) {
      documentType = "mtr_ladocument";
    }

    IDfDocument document = (IDfDocument) session.newObject(documentType);
    String docId="";
    if (document != null) {
      document.setObjectName(documentName);
      document.setContentType(dctmExtension);
      document.setFile(documentPath);
          
      document.link(folderId);
      
      document.setRepeatingString("claim_number", 0, content.getClaimnumber().trim());
      document.setRepeatingInt("file_number", 0, Integer.parseInt(content.getFilenumber()));
      if (content.getDocumentbarcodenumber() != null)
        document.setString("barcode_no", content.getDocumentbarcodenumber().trim());
      if (content.getBoxbarcodenumber() != null)
        document.setString("rm_barcode_number", content.getBoxbarcodenumber().trim());
      document.setString("department_code", content.getDepartmentcode().trim());
      document.setString("source_system", content.getSourcesystem().trim());
      document.setString("document_type", content.getDocumenttype().get(documentName));
      document.setRepeatingString("proposal_number", 0, content.getProposalnumber().trim());
      document.setString("document_status", getDocumentStatus(content, authInfo));
      document.setString("health_center_name", content.getHealthcentername());
      document.setString("provision_source_system", content.getProvisionsource());

      if ("mtr_ladocument".equals(documentType)) {
        document.setString("case_number", content.getCasenumber());
      }

      document.save();
      docId=document.getObjectId().getId();
    }
    
    return docId;
  }

  private String createProvisionDocument(IDfSession session, String documentName, String documentPath,Content content,String attachmentId) throws DfException {
	    String extension = documentName.substring(documentName.lastIndexOf('.') + 1);
	    String dctmExtension = Variable.INSTANCE.getExtensions().get(extension.toLowerCase());
	    DfLogger.debug(this, "Extension -> DCTM: [{0}] -> [{1}]", new String[] {extension.toLowerCase(), dctmExtension}, null);
	    if (dctmExtension == null || dctmExtension.isEmpty()) {
	      DfException exceptionExtensionNotSupported = new DfException();
	      String errorMessage = String.format("No such supported Documentum content type '%s' found for file '%s'! Extension '%s' may not be supported.", dctmExtension, documentName, extension);
	      exceptionExtensionNotSupported.setMessage(errorMessage);
	      DfLogger.error(this, errorMessage, null, exceptionExtensionNotSupported);
	      throw exceptionExtensionNotSupported;
	    }
	    String documentType = "hlth_fax_document";

	    IDfDocument document = (IDfDocument) session.newObject(documentType);
	    String docId="";
	    if (document != null) {
	      document.setObjectName(content.getCustomername());	      
	      document.setContentType(dctmExtension);
	      document.setFile(documentPath);
	    
	      document.link(FAX_FOLDER);

	      document.setTime("receive_date", new DfTime());
	      document.setString("provision_number", content.getClaimnumber());
	      document.appendString("attachmentids", attachmentId);
	      document.setString("identification_number", content.getIdentificationnumber());
	      document.setString("insurer", content.getInsurer().trim());	      
	      document.setString("policy_source_system", content.getProvisionsource());      
	      document.setString("customer_name", content.getCustomername().trim());
	      document.setString("health_center_name", content.getHealthcentername());
	      // FIXME: replace static document status
	      document.setString("document_status", "0");

	      document.save();
	      docId=document.getObjectId().getId();
	    }
	    
	    return docId;
	  }
  
  private String createAttachment(IDfSession session, String documentName, String fullPath) throws DfException {
	    String extension = documentName.substring(documentName.lastIndexOf('.') + 1);
	    String dctmExtension = Variable.INSTANCE.getExtensions().get(extension.toLowerCase());
	    DfLogger.debug(this, "Extension -> DCTM: [{0}] -> [{1}]", new String[] {extension.toLowerCase(), dctmExtension}, null);
	    if (dctmExtension == null || dctmExtension.isEmpty()) {
	      DfException exceptionExtensionNotSupported = new DfException();
	      String errorMessage = String.format("No such supported Documentum content type '%s' found for file '%s'! Extension '%s' may not be supported.", dctmExtension, documentName, extension);
	      exceptionExtensionNotSupported.setMessage(errorMessage);
	      DfLogger.error(this, errorMessage, null, exceptionExtensionNotSupported);
	      throw exceptionExtensionNotSupported;
	    }
	    String documentType = "dm_document";

	    IDfDocument document = (IDfDocument) session.newObject(documentType);
	    String docId="";
	    if (document != null) {
	      document.setObjectName(documentName);
	      document.setContentType(dctmExtension);
	      document.setFile(fullPath);
	    
	      document.link(ATTACHMENT_FOLDER);
	      document.save();
	      docId=document.getObjectId().getId();
	    }
	    
	    return docId;
	  }
  private String getDocumentStatus(Content content, UserAuthInfo authInfo) {
    String documentStatus = "";

    if (isAgitoUser(authInfo) && "0155".equals(content.getDepartmentcode()))
      documentStatus = "Arþivde";
    else if (isReimbursementUser(content))
      documentStatus = "Akýþta";

    return documentStatus;
  }

  private boolean isAgitoUser(UserAuthInfo authInfo) {
    return "agito_user".equals(authInfo.getUserName());
  }

  private boolean isReimbursementUser(Content content) {
    return content.getSourcesystem() != null && "reimbursement".equalsIgnoreCase(content.getSourcesystem().trim());
  }

}