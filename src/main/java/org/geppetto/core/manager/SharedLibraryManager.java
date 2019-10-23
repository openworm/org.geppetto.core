
package org.geppetto.core.manager;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.LibraryManager;

/**
 * @author matteocantarelli
 *
 */
public class SharedLibraryManager
{

	private static LibraryManager manager;

	private static GeppettoLibrary commonLibrary;

	private static LibraryManager getLibraryManager()
	{
		if(manager == null)
		{
			manager = GeppettoFactory.eINSTANCE.createLibraryManager();
		}
		return manager;
	}

	public synchronized static GeppettoLibrary getSharedCommonLibrary() throws GeppettoInitializationException
	{
		if(commonLibrary == null)
		{
			GeppettoPackage.eINSTANCE.eClass();
			Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
			Map<String, Object> m = reg.getExtensionToFactoryMap();
			m.put("xmi", new XMIResourceFactoryImpl()); // sets the factory for the XMI type
			GeppettoPackage.Registry.INSTANCE.put(GeppettoPackage.eNS_URI, GeppettoPackage.eINSTANCE);

			// We add all supported versions of the schema
			String[] versions = new String[] { "v1.1.0" };
			for(String version : versions)
			{
				GeppettoPackage.Registry.INSTANCE.put(GeppettoPackage.eNS_URI_TEMPLATE.replace("$VERSION$", version), GeppettoPackage.eINSTANCE);
			}

			ResourceSet resSet = new ResourceSetImpl();
			Resource resource = resSet.createResource(URI.createURI("/GeppettoCommonLibrary.xmi"));
			try
			{
				resource.load(SharedLibraryManager.class.getResourceAsStream("/GeppettoCommonLibrary.xmi"), null);
			}
			catch(IOException e)
			{
				throw new GeppettoInitializationException(e);
			}
			commonLibrary = (GeppettoLibrary) resource.getContents().get(0);
			getLibraryManager().getLibraries().add(commonLibrary);

		}
		return commonLibrary;
	}

}
