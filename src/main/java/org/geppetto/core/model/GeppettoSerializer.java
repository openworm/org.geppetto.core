
package org.geppetto.core.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.ISynchable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoSerializer
{

	private static Log logger = LogFactory.getLog(GeppettoSerializer.class);

	private static GeppettoCustomTypeSerializer customTypeSerializer = new GeppettoCustomTypeSerializer(null);

	/**
	 * @param geppettoModel
	 * @param onlySerialiseDelta
	 *            if true it only serialised the delta between what was serialised previously and what changed
	 * @return
	 */
	public static String serializeToJSON(EObject toSerialize, boolean onlySerialiseDelta) throws IOException
	{
		long start = System.currentTimeMillis();

		StringWriter sw = new StringWriter();
		WriteableOutputStream outputStream = new WriteableOutputStream(sw, "UTF-8");

		ObjectMapper mapper = new ObjectMapper();

		ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(GeppettoPackage.eNS_URI, GeppettoPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new JsonResourceFactory());		

		EMFModule module = new EMFModule();
		module.configure(EMFModule.Feature.OPTION_SERIALIZE_TYPE, true);
		module.addSerializer(EClass.class, customTypeSerializer);
		if(onlySerialiseDelta)
		{
			// this module, by using the ISynchable interface, serialises only those objects
			// for which synched==false and as soon an object is serialised synched is set to true
		    module.addSerializer(ISynchable.class, new SynchableSerializer(null));
		}
		module.addSerializer(Map.Entry.class, new EMapGeppettoSerializer(module.getUriHandler()));

		//module.addSerializer(M ap.Entry.class, new EMapGeppettoSerializer(jacksonOptions));
		mapper.registerModule(module);

		Resource resource = resourceSet.createResource(URI.createURI("geppettoModel"));
		resource.getContents().add(toSerialize);

		mapper.writeValue(outputStream, resource);
		// resource.save(outputStream, null);
		outputStream.flush();
		sw.close();
		String serializedModel = sw.toString();
		//logger.info("Model serialized to JSON " + (System.currentTimeMillis() - start) + "ms");

		return serializedModel;
	}

	/**
	 * @param toSerialize
	 * @return
	 * @throws IOException
	 */
	public static String serializeToJSON(EObject toSerialize) throws IOException
	{
		return serializeToJSON(toSerialize, false);
	}
	
	/**
	 * @author matteocantarelli
	 *
	 */
	private static class GeppettoCustomTypeSerializer extends StdSerializer<EClass>
	{

		protected GeppettoCustomTypeSerializer(Class<EClass> t) {
			super(t);
		}

		@Override
		public void serialize(EClass eClass, JsonGenerator jg, SerializerProvider provider) throws IOException
		{

			jg.writeStringField("eClass", eClass.getName());
		}

	}
}
