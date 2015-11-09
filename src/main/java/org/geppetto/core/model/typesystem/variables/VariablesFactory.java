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
package org.geppetto.core.model.typesystem.variables;

import org.geppetto.core.model.typesystem.types.IType;
import org.geppetto.core.model.typesystem.types.TypesFactory;


/**
 * @author matteocantarelli
 *
 */
public class VariablesFactory
{
	
	public static IVariable getVariable(String name, IType type)
	{
		Variable variable=new Variable(name, type);
		return variable;
	}

	public static ArrayVariable getArrayVariable(String name, IType type)
	{
		ArrayVariable variable=new ArrayVariable(name, type);
		return variable;
	}

	public static IVariable getAnyPrimitiveVariable(String name)
	{
		return getVariable(name, TypesFactory.getPrimitiveType());
	}

	public static IVariable getIntVariable(String name)
	{
		return getVariable(name, TypesFactory.getIntType());
	}
	
	public static IVariable getStringVariable(String name)
	{
		return getVariable(name, TypesFactory.getStringType());
	}
	
	


}
