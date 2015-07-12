package org.geppetto.core.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

/**
 * Class for uploading files to a drop box account.
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class DropboxUploadService implements IExternalUploadService
{

	final String APP_KEY = "kbved8e6wnglk4h";
	final String APP_SECRET = "3vfszva2y4ax7j5";
	private String authorizeURL = null;
	private DbxWebAuthNoRedirect webAuth = null;
	private DbxRequestConfig config = null;
	private DbxClient client = null;

	private static Log logger = LogFactory.getLog(DropboxUploadService.class);

	public DropboxUploadService()
	{
		authorizeService();
	}

	protected void authorizeService()
	{

		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		config = new DbxRequestConfig("Geppetto", Locale.getDefault().toString());
		webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		authorizeURL = webAuth.start();

	}

	public String getAuthorizationURL()
	{
		if(this.authorizeURL == null)
		{
			this.authorizeService();
		}

		return this.authorizeURL;
	}

	@Override
	public void upload(File inputFile) throws Exception
	{
		if(client != null)
		{
			FileInputStream inputStream = null;
			try
			{
				inputStream = new FileInputStream(inputFile);

				DbxEntry.File uploadedFile = client.uploadFile("/" + inputFile.getName(), DbxWriteMode.add(), inputFile.length(), inputStream);
				logger.info("Uploaded: " + uploadedFile.toString());
			}
			catch(Exception e)
			{
				throw new Exception(e);
			}
			finally
			{
				inputStream.close();
			}
		}
		else
		{
			throw new GeppettoExecutionException("Cannot upload a file to dropbox, the account is not linked.");
		}
	}

	@Override
	public String link(String code) throws Exception
	{
		try
		{
			DbxAuthFinish authFinish = webAuth.finish(code);

			String accessToken = authFinish.accessToken;

			client = new DbxClient(config, accessToken);

			logger.info("Linked account: " + client.getAccountInfo().displayName);

			return accessToken;
		}
		catch(DbxException e)
		{
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public void unlink(String code)
	{
		client = null;
	}

	public void init(String dropboxToken)
	{
		if(client == null)
		{
			client = new DbxClient(config, dropboxToken);
		}
	}

}
