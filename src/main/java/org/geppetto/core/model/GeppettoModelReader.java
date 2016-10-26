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
