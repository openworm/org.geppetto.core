

package org.geppetto.core.s3;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3ManagerTest
{

	private static final String PATH = "test";

	public S3ManagerTest()
	{
	}

	@Test
	public void testS3Operations() throws IOException
	{
		List<S3ObjectSummary> paths = S3Manager.getInstance().retrievePathsFromS3(PATH);
		int count = paths.size();
		S3Manager.getInstance().saveTextToS3("S3ManagerTest test", PATH + "/" + PATH + new Date().getTime() + ".txt");
		List<S3ObjectSummary> objects = S3Manager.getInstance().retrievePathsFromS3(PATH);
		Assert.assertEquals(count + 1, objects.size());
		S3Manager.getInstance().deleteFromS3(objects.get(objects.size() - 1).getKey());
		objects = S3Manager.getInstance().retrievePathsFromS3(PATH);
		Assert.assertEquals(count, objects.size());
	}

}
