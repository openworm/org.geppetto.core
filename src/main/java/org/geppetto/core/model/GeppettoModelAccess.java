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

import org.eclipse.emf.ecore.EClass;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.Tag;
import org.geppetto.model.types.Type;
import org.geppetto.model.util.GeppettoModelException;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.util.PointerUtility;
import org.geppetto.model.values.Pointer;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoModelAccess
{

	private GeppettoModel geppettoModel;
	
	private GeppettoLibrary commonlibrary;

	public GeppettoModelAccess(GeppettoModel geppettoModel) throws GeppettoVisitingException
	{
		super();
		this.geppettoModel=geppettoModel;
		for(GeppettoLibrary library : geppettoModel.getLibraries())
		{
			if(library.getId().equals("common"))
			{
				commonlibrary = library;
				break;
			}
		}
		if(commonlibrary == null)
		{
			throw new GeppettoVisitingException("Common library not found");
		}
	}
	
	/**
	 * @param tag
	 */
	public void addTag(Tag tag)
	{
		geppettoModel.getTags().add(tag);
	}

	/**
	 * Usage commonLibraryAccess.getType(TypesPackage.Literals.PARAMETER_TYPE);
	 * 
	 * @return
	 * @throws GeppettoVisitingException
	 */
	public Type getType(EClass eclass) throws GeppettoVisitingException
	{
		for(Type type : commonlibrary.getTypes())
		{
			if(type.eClass().equals(eclass))
			{
				return type;
			}
		}
		throw new GeppettoVisitingException("Type for eClass " + eclass + " not found in common library.");
	}
	
	/**
	 * @param instancePath
	 * @return
	 * @throws GeppettoModelException
	 */
	public Pointer getPointer(String instancePath) throws GeppettoModelException
	{
		return PointerUtility.getPointer(geppettoModel, instancePath);
	}
}
