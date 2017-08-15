
package org.geppetto.core.services;

import java.io.File;

/**
 * Interface use for uploading and downloading files to an online service, e.g. Dropbox or could be another service
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public interface IExternalUploadService
{

	void upload(File file) throws Exception;

	String link(String code) throws Exception;

	void unlink(String code);
}
