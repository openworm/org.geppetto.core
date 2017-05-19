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
			String[] versions = new String[] { "master", "development" };
			for(String version : versions)
			{
				GeppettoPackage.Registry.INSTANCE.put(GeppettoPackage.eNS_URI_TEMPLATE.replace("$VERSION$", version), GeppettoPackage.eINSTANCE);
			}
			
			ResourceSet resSet = new ResourceSetImpl();
			Resource resource = resSet.createResource(URI.createURI("/GeppettoCommonLibrary.xmi"));
			try
			{
				resource.load(SharedLibraryManager.class.getResourceAsStream("/GeppettoCommonLibrary.xmi"),null);
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
