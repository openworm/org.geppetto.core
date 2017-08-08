

package org.geppetto.core.model;

import java.net.URL;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.GeppettoPackage;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoModelReader
{

	static
	{
		GeppettoPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl()); // sets the factory for the XMI type
		m.put("xml", new XMIResourceFactoryImpl()); // sets the factory for the XMI type
		m.put("json", new JsonResourceFactory()); // sets the factory for the JSON type

		GeppettoPackage.Registry.INSTANCE.put(GeppettoPackage.eNS_URI, GeppettoPackage.eINSTANCE);

		// We add all supported versions of the schema
		String[] versions = new String[] { "master", "development" };
		for(String version : versions)
		{
			GeppettoPackage.Registry.INSTANCE.put(GeppettoPackage.eNS_URI_TEMPLATE.replace("$VERSION$", version), GeppettoPackage.eINSTANCE);
		}

	}

	/**
	 * @param url
	 * @return
	 * @throws GeppettoInitializationException
	 */
	public static GeppettoModel readGeppettoModel(URL url) throws GeppettoInitializationException
	{

		GeppettoModel geppettoModel = null;
		try
		{
			AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack());
			Resource resource = domain.loadResource(url.toURI().toString());

			geppettoModel = (GeppettoModel) resource.getContents().get(0);
		}
		catch(Exception e)
		{
			throw new GeppettoInitializationException("Unable to unmarshall simulation with url: " + url.toString(), e);
		}

		return geppettoModel;
	}

}
