package org.geppetto.core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZipDirectory
{

	private static Log logger = LogFactory.getLog(ZipDirectory.class);

	public static Path getZipFromDirectory(File sourceFolder)
	{
		return getZipFromDirectory(sourceFolder, sourceFolder.getName() + ".zip");
	}

	public static Path getZipFromDirectory(File sourceFolder, String outputFileName)
	{
		List<File> fileList = new ArrayList<File>();
		getAllFiles(sourceFolder, fileList);
		writeZipFile(sourceFolder, outputFileName, fileList);
		return Paths.get(sourceFolder.getAbsolutePath() + System.getProperty("file.separator") + outputFileName);
	}

	public static void getAllFiles(File dir, List<File> fileList)
	{
		File[] files = dir.listFiles();
		for(File file : files)
		{
			fileList.add(file);
			if(file.isDirectory())
			{
				getAllFiles(file, fileList);
			}
		}
	}

	public static void writeZipFile(File directoryToZip, String outputFileName, List<File> fileList)
	{

		try
		{
			logger.info("Creating zip file " + outputFileName);
			FileOutputStream fos = new FileOutputStream(directoryToZip + "/" + outputFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for(File file : fileList)
			{
				if(!file.isDirectory())
				{ // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException, IOException
	{

		FileInputStream fis = new FileInputStream(file);

		String zipFilePath = file.getName();
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

	public static Path getZipFromFile(File file) {
		List<File> fileList = new ArrayList<File>();
		fileList.add(file);
		File sourceFolder = new File(System.getProperty("user.dir")+"/geppettoTmp");
		String outputName = file.getName()+".zip";
		writeZipFile(sourceFolder, outputName, fileList);
		String path = sourceFolder.getAbsolutePath() + File.separator + outputName;
		return Paths.get(path);
	}

}