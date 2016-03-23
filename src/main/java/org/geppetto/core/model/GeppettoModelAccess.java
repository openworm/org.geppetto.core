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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.Tag;
import org.geppetto.model.types.Type;
import org.geppetto.model.util.GeppettoModelException;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.util.PointerUtility;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoModelAccess
{

	private GeppettoModel geppettoModel;

	private GeppettoLibrary commonlibrary;

	private EditingDomain editingDomain;

	public GeppettoModelAccess(GeppettoModel geppettoModel) throws GeppettoVisitingException
	{
		super();
		this.geppettoModel = geppettoModel;
		editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(geppettoModel);
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

	/**
	 * @param variable
	 */
	public void addVariable(final Variable variable)
	{
		Command command = AddCommand.create(editingDomain, geppettoModel, GeppettoPackage.Literals.GEPPETTO_MODEL__VARIABLES, variable);
		editingDomain.getCommandStack().execute(command);
	}

	/**
	 * @param tag
	 */
	public void addTag(final Tag tag)
	{
		Command command = AddCommand.create(editingDomain, geppettoModel, GeppettoPackage.Literals.GEPPETTO_MODEL__TAGS, tag);
		editingDomain.getCommandStack().execute(command);
	}

	/**
	 * @param tag
	 */
	public void addTypeToLibrary(final Type type, final GeppettoLibrary targetLibrary)
	{
		Command command = AddCommand.create(editingDomain, targetLibrary, GeppettoPackage.Literals.GEPPETTO_LIBRARY__TYPES, type);
		Command setCommand = SetCommand.create(editingDomain, targetLibrary, GeppettoPackage.Literals.ISYNCHABLE__SYNCHED, false);
		editingDomain.getCommandStack().execute(command);
		editingDomain.getCommandStack().execute(setCommand);
	}
}
