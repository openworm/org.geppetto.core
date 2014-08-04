/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
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

package org.geppetto.core.model.services;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.geppetto.core.model.IModel;
import org.geppetto.core.model.IModelInterpreter;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.simulation.Aspect;
import org.geppetto.core.model.state.StateTreeRoot;
import org.geppetto.core.visualisation.model.CAspect;
import org.geppetto.core.visualisation.model.CEntity;
import org.geppetto.core.visualisation.model.Obj;
import org.geppetto.core.visualisation.model.VisualModel;
import org.springframework.stereotype.Service;

/**
 * @author matteocantarelli
 * 
 */
@Service
public class OBJModelInterpreterService implements IModelInterpreter
{

	private static final String OBJ = "OBJ";
	private CEntity _visualEntity;
	private int _modelHash;

	@Override
	public IModel readModel(URL url, List<URL> recordings, String instancePath) throws ModelInterpreterException
	{
		ModelWrapper obj = new ModelWrapper(instancePath);
		try
		{
			Scanner scanner = new Scanner(url.openStream(), "UTF-8");
			String objContent = scanner.useDelimiter("\\A").next();
			scanner.close();
			obj.wrapModel(OBJ, objContent);
		}
		catch(IOException e)
		{
			throw new ModelInterpreterException(e);
		}

		return obj;
	}

	@Override
	public CEntity getVisualEntity(IModel model, Aspect aspect, StateTreeRoot stateTree) throws ModelInterpreterException
	{
		if(_visualEntity == null || _modelHash != model.hashCode())
		{
			_visualEntity = new CEntity();
			CAspect rootAspect = new CAspect();
			rootAspect.setId(aspect.getId());
			_visualEntity.getAspects().add(rootAspect);
			_modelHash = model.hashCode();
			Obj obj = new Obj();
			obj.setModel((String) ((ModelWrapper) model).getModel(OBJ));
			
			VisualModel colladaModel = new VisualModel();
			colladaModel.getObjects().add(obj);
			rootAspect.getVisualModel().add(colladaModel);
			return _visualEntity;
		}
		else
		{
			//if we already sent once the update every other time it's going to be empty unless it changes
			//as the geometry won't change
			CEntity empty = new CEntity();
			CAspect visualAspect = new CAspect();
			visualAspect.setId(aspect.getId());
			empty.getAspects().add(visualAspect);
			return empty;
		}
		
	}

}
