
package org.geppetto.core;

import java.io.File;

import org.geppetto.core.services.DropboxUploadService;
import org.junit.Test;

public class TestDropboxUploadService
{
	@Test
	public void test() 
	{
 
		try {
			DropboxUploadService dropboxService = new DropboxUploadService();
			
			String authorizeUrl = dropboxService.getAuthorizationURL();
	        System.out.println("1. Go to: " + authorizeUrl);
	        System.out.println("2. Click \"Allow\" (you might have to log in first)");
	        System.out.println("3. Copy the authorization code.");
	        String code = "";
	        //System.out.println("Code "+ code);
	        dropboxService.link(code);
	        dropboxService.upload(new File("./src/test/resources/H5DatasetCreate.h5"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
