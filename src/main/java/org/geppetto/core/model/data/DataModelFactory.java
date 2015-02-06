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
package org.geppetto.core.model.data;

import org.geppetto.core.data.model.AType;
import org.geppetto.core.data.model.ArrayVariable;
import org.geppetto.core.data.model.SimpleType;
import org.geppetto.core.data.model.SimpleVariable;
import org.geppetto.core.data.model.StructuredType;
import org.geppetto.core.data.model.SimpleType.Type;

/**
 * Factory class for the generated data model
 * 
 * @author matteocantarelli
 * 
 */
public class DataModelFactory
{

	private static final SimpleType FLOAT_TYPE = DataModelFactory.getSimpleType(Type.FLOAT);
	private static final SimpleType DOUBLE_TYPE = DataModelFactory.getSimpleType(Type.DOUBLE);
	private static final SimpleType INTEGER_TYPE = DataModelFactory.getSimpleType(Type.INTEGER);

	/**
	 * @param name
	 * @param type
	 * @return
	 */
	public static SimpleVariable getSimpleVariable(String name, AType type)
	{
		SimpleVariable sv = new SimpleVariable();
		sv.setName(name);
		sv.setType(type);
		return sv;
	}

	/**
	 * @param name
	 * @param type
	 * @param size
	 * @return
	 */
	public static ArrayVariable getArrayVariable(String name, AType type, int size)
	{
		ArrayVariable av = new ArrayVariable();
		av.setName(name);
		av.setType(type);
		av.setSize(size);
		return av;
	}

	/**
	 * @param type
	 * @return
	 */
	public static SimpleType getSimpleType(SimpleType.Type type)
	{
		SimpleType st = new SimpleType();
		st.setType(type);
		return st;
	}

	/**
	 * @param type
	 * @return
	 */
	public static SimpleType getCachedSimpleType(SimpleType.Type type)
	{
		switch(type)
		{
			case DOUBLE:
				return DOUBLE_TYPE;
			case FLOAT:
				return FLOAT_TYPE;
			case INTEGER:
				return INTEGER_TYPE;
			default:
				return null;
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public static StructuredType getStructuredType(String name)
	{
		StructuredType st = new StructuredType();
		st.setName(name);
		return st;
	}
}
