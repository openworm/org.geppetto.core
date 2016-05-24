package org.geppetto.core.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author adrianq
 * @author matteocantarelli
 *
 */
public class Zipper
{

	private static Log logger = LogFactory.getLog(Zipper.class);
	private FileOutputStream fos;
	private ZipOutputStream zos;
	private String fullPathToZipToCreate;
	private String innerFolderName = "";

	public Zipper(String fullPathToZipToCreate) throws FileNotFoundException
	{
		super();
		fos = new FileOutputStream(fullPathToZipToCreate);
		zos = new ZipOutputStream(fos);
		this.fullPathToZipToCreate = fullPathToZipToCreate;
	}
	
	public Zipper(String fullPathToZipToCreate, String innerFolderName) throws IOException
	{
		this(fullPathToZipToCreate);
		this.innerFolderName = innerFolderName + "/";
		zos.putNextEntry(new ZipEntry(this.innerFolderName));
	}

	/**
	 * @param aspectPath
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Path getZipFromFile(URL file) throws IOException
	{
		addToZip(file);
		return processAddedFilesAndZip();
	}

	/**
	 * @param sourceFolder
	 * @return
	 * @throws IOException
	 */
	public Path getZipFromDirectory(File sourceFolder) throws IOException
	{
		return getZipFromDirectory(sourceFolder, sourceFolder.getName() + ".zip");
	}

	/**
	 * @param sourceFolder
	 * @param outputFileName
	 * @return
	 * @throws IOException
	 */
	private Path getZipFromDirectory(File sourceFolder, String outputFileName) throws IOException
	{
		List<URL> files = new ArrayList<URL>();
		getAllFiles(sourceFolder, files);
		for(URL file : files)
		{
			addToZip(file);
		}
		return processAddedFilesAndZip();
	}

	/**
	 * @param dir
	 * @param fileList
	 * @throws MalformedURLException
	 */
	private void getAllFiles(File dir, List<URL> fileList) throws MalformedURLException
	{
		File[] files = dir.listFiles();
		for(File file : files)
		{
			fileList.add(file.toURI().toURL());
			if(file.isDirectory())
			{
				getAllFiles(file, fileList);
			}
		}
	}

	/**
	 * @param directoryToZip
	 * @param outputFileName
	 * @param fileList
	 * @throws IOException
	 */
	public Path processAddedFilesAndZip() throws IOException
	{
		logger.info("Creating zip file " + fullPathToZipToCreate);
		zos.close();
		fos.close();
		return Paths.get(fullPathToZipToCreate);
	}

	/**
	 * @param file
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void addToZip(URL file) throws FileNotFoundException, IOException
	{
		String zipFilePath = "";
		File fileObject = new File(file.getFile());
		if (fileObject.isDirectory()){
			zipFilePath = fileObject.getName() + "/";
		}
		else{
			zipFilePath = URLReader.getFileName(file);
		}
		
		logger.info("Writing '" + zipFilePath + "' to zip file");
		zos.putNextEntry(new ZipEntry(this.innerFolderName + zipFilePath));

		InputStream  fis = file.openStream();
		byte[] bytes = new byte[1024];
		int length;
		while((length = fis.read(bytes)) >= 0)
		{
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

}