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
import org.geppetto.model.types.Type;
import org.geppetto.model.util.GeppettoModelException;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoCommonLibraryAccess
{

	GeppettoLibrary commonlibrary;

	public GeppettoCommonLibraryAccess(GeppettoModel geppettoModel) throws GeppettoModelException
	{
		super();
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
			throw new GeppettoModelException("Common library not found");
		}
	}

	/**
	 * Usage commonLibraryAccess.getType(TypesPackage.Literals.PARAMETER_TYPE);
	 * 
	 * @return
	 * @throws GeppettoModelException
	 */
	public Type getType(EClass eclass) throws GeppettoModelException
	{
		for(Type type : commonlibrary.getTypes())
		{
			if(type.eClass().equals(eclass))
			{
				return type;
			}
		}
		throw new GeppettoModelException("Type for eClass " + eclass + " not found in common library.");
	}
}
