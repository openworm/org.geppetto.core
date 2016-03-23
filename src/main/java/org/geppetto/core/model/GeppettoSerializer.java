/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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
import org.emfjson.EMFJs;
import org.emfjson.jackson.JacksonOptions;
import org.emfjson.jackson.databind.ser.TypeSerializer;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.ISynchable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoSerializer
{

	private static Log logger = LogFactory.getLog(GeppettoSerializer.class);

	private static GeppettoCustomTypeSerializer customTypeSerializer = new GeppettoCustomTypeSerializer();

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

		Map<String, Object> options = new HashMap<>();
		options.put(EMFJs.OPTION_SERIALIZE_DEFAULT_VALUE, true);

		JacksonOptions jacksonOptions = new JacksonOptions.Builder().withTypeSerializer(customTypeSerializer).build(options);
		EMFModule module = new EMFModule(resourceSet, jacksonOptions);
		if(onlySerialiseDelta)
		{
			// this module, by using the ISynchable interface, serialises only those objects
			// for which synched==false and as soon an object is serialised synched is set to true
			module.addSerializer(ISynchable.class, new SynchableSerializer(jacksonOptions));
		}
		module.addSerializer(Map.Entry.class, new EMapGeppettoSerializer(jacksonOptions));
		mapper.registerModule(module);

		Resource resource = resourceSet.createResource(URI.createURI("geppettoModel"));
		resource.getContents().add(toSerialize);

		mapper.writeValue(outputStream, resource);
		// resource.save(outputStream, null);
		outputStream.flush();
		sw.close();
		String serializedModel = sw.toString();
		logger.info("Model serialized to JSON " + (System.currentTimeMillis() - start) + "ms");

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
	private static class GeppettoCustomTypeSerializer implements TypeSerializer
	{

		@Override
		public void serialize(EClass eClass, JsonGenerator jg, SerializerProvider provider) throws IOException
		{

			jg.writeStringField("eClass", eClass.getName());
		}

	}

}
