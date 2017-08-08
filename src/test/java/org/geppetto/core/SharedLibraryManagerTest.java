
package org.geppetto.core;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.manager.SharedLibraryManager;
import org.geppetto.model.GeppettoLibrary;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class SharedLibraryManagerTest
{

	@Test
	public void test() throws GeppettoInitializationException
	{
		GeppettoLibrary library=SharedLibraryManager.getSharedCommonLibrary();
		Assert.assertNotNull(library);
	}

}
