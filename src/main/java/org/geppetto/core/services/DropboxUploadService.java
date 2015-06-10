package org.geppetto.core.services;


import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;

import com.dropbox.core.*;

/**
 * Class for uploading files to a drop box account. 
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class DropboxUploadService implements IExternalUploadService{

	final String APP_KEY = "kbved8e6wnglk4h";
    final String APP_SECRET = "3vfszva2y4ax7j5";
    private String authorizeURL = null;
    private DbxWebAuthNoRedirect webAuth = null;
    private boolean authorized = false;
    private DbxRequestConfig config = null;
    private DbxClient client = null;
    
	private static Log logger = LogFactory.getLog(DropboxUploadService.class);
    
	public DropboxUploadService(){
		authorizeService();
	}
	
	protected void authorizeService(){
		if(!authorized){
			DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

			config = new DbxRequestConfig(
					"JavaTutorial/1.0", Locale.getDefault().toString());
			webAuth = new DbxWebAuthNoRedirect(config, appInfo);

			authorizeURL = webAuth.start();
		}
	}
	
	public String getAuthorizationURL(){
		if(this.authorizeURL==null){
			this.authorizeService();
		}
		
		return this.authorizeURL;
	}
	
	
	@Override
	public void upload(File inputFile) throws Exception {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(inputFile);

			DbxEntry.File uploadedFile = client.uploadFile("/"+inputFile.getName(),
					DbxWriteMode.add(), inputFile.length(), inputStream);
			logger.info("Uploaded: " + uploadedFile.toString());
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			inputStream.close();
		}
	}

	@Override
	public void link(String code) throws Exception {
		try {
			DbxAuthFinish authFinish = webAuth.finish(code);

			String accessToken = authFinish.accessToken;

			client = new DbxClient(config, accessToken);

			logger.info("Linked account: " + client.getAccountInfo().displayName);
		} catch (DbxException e) {
			throw new Exception(e.getMessage());
		}   
	}

	@Override
	public void unlink(String code) {
		
	}

}
