package org.geppetto.core.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;


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
        private DbxClientV2 client = null;

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
				FileMetadata uploadedFile = client.files().uploadBuilder("/" + inputFile.getName())
				    .withMode(WriteMode.ADD)
				    .uploadAndFinish(inputStream);
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

			String accessToken = authFinish.getAccessToken();

			client = new DbxClientV2(config, accessToken);

			logger.info("Linked account: " + client.users().getCurrentAccount().getName());

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
			client = new DbxClientV2(config, dropboxToken);
		}
	}

}
