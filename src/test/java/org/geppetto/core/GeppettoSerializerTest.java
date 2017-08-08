
package org.geppetto.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.geppetto.core.model.GeppettoSerializer;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.GeppettoPackage;
import org.junit.Test;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoSerializerTest
{

	@Test
	public void testSerializer() throws IOException, URISyntaxException
	{
		// Initialize the factory and the resource set
		GeppettoPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl()); // sets the factory for the XMI type
		m.put("json", new JsonResourceFactory()); // sets the factory for the JSON type
		ResourceSet resSet = new ResourceSetImpl();

		Resource resource = resSet.createResource(URI.createURI("/GeppettoModelTest.xmi"));
		resource.load(GeppettoSerializerTest.class.getResourceAsStream("/GeppettoModelTest.xmi"), null);

		GeppettoModel geppettoModel = (GeppettoModel) resource.getContents().get(0);
		URL url = this.getClass().getResource("/test.json");
		String jsonResource = new String(Files.readAllBytes(Paths.get(url.toURI())));
		assertEquals(jsonResource, GeppettoSerializer.serializeToJSON(geppettoModel));
		assertEquals(jsonResource, GeppettoSerializer.serializeToJSON(geppettoModel, true));
		assertEquals(jsonResource, GeppettoSerializer.serializeToJSON(geppettoModel));
		assertTrue(geppettoModel.getVariables().get(0).isSynched());
		assertEquals("{\"eClass\":\"GeppettoModel\",\"id\":\"\",\"name\":\"\",\"variables\":[{\"synched\":true}],\"libraries\":[{\"synched\":true}]}", GeppettoSerializer.serializeToJSON(geppettoModel, true));
	}

}
