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
	
	

	
	/**
	 * @param aspectPath
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Path getZipFromFile(String destinationfilePath, URL file) throws IOException
	{
		List<URL> fileList = new ArrayList<URL>();
		fileList.add(file);
		File sourceFolder = new File(System.getProperty("user.dir") + "/geppettoTmp");
		writeZipFile(sourceFolder, destinationfilePath, fileList);
		String path = sourceFolder.getAbsolutePath() + File.separator + destinationfilePath;
		return Paths.get(path);
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
		List<URL> fileList = new ArrayList<URL>();
		getAllFiles(sourceFolder, fileList);
		writeZipFile(sourceFolder, outputFileName, fileList);
		return Paths.get(sourceFolder.getAbsolutePath() + System.getProperty("file.separator") + outputFileName);
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
	private void writeZipFile(File directoryToZip, String outputFileName, List<URL> fileList) throws IOException
	{
		logger.info("Creating zip file " + outputFileName);
		FileOutputStream fos = new FileOutputStream(directoryToZip + "/" + outputFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);

		for(URL file : fileList)
		{
			// if(!file.isDirectory())
			{ // we only zip files, not directories
				addToZip(directoryToZip, file, zos);
			}
		}

		zos.close();
		fos.close();
	}

	/**
	 * @param directoryToZip
	 * @param file
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addToZip(File directoryToZip, URL file, ZipOutputStream zos) throws FileNotFoundException, IOException
	{

		InputStream fis = file.openStream();

		String zipFilePath = URLReader.getFileName(file);
		logger.info("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

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