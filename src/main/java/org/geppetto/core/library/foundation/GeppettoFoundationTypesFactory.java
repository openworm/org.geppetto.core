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
package org.geppetto.core.library.foundation;

import org.geppetto.core.model.typesystem.GeppettoAspect;
import org.geppetto.core.model.typesystem.types.AType;
import org.geppetto.core.model.typesystem.types.CompositeType;
import org.geppetto.core.model.typesystem.types.IType;
import org.geppetto.core.model.typesystem.types.SimpleType;
import org.geppetto.core.model.typesystem.types.TypesFactory;
import org.geppetto.core.model.typesystem.variables.VariablesFactory;

/**
 * @author matteocantarelli
 *
 */
public class GeppettoFoundationTypesFactory
{

	private static CompositeType quantity;
	private static CompositeType physicalQuantity;
	private static CompositeType function;
	private static SimpleType unit;
	private static SimpleType expression;

	public static AType getDynamicsSpecificationType()
	{
		CompositeType type = TypesFactory.getCompositeType(GeppettoAspect.get(), "DynamicsSpecification");
		type.addVariable(VariablesFactory.getVariable("initialCondition",getPhysicalQuantityType()));
		type.addVariable(VariablesFactory.getVariable("dynamics",getFunctionType()));
		return type;
	}

	private static IType getFunctionType()
	{
		if(function == null)
		{
			function = TypesFactory.getCompositeType(GeppettoAspect.get(), "Function");
			function.addVariable(VariablesFactory.getArrayVariable("arguments", TypesFactory.getStringType()));
			function.addVariable(VariablesFactory.getVariable("expression", getExpressionType()));
		}
		return function;
	}

	private static IType getExpressionType()
	{
		if(expression == null)
		{
			expression = TypesFactory.getSimpleType(GeppettoAspect.get(), "Expression", TypesFactory.getStringType());
		}
		return expression;
	}

	public static CompositeType getQuantityType()
	{
		if(quantity == null)
		{
			quantity = TypesFactory.getCompositeType(GeppettoAspect.get(), "Quantity");
			quantity.addVariable(VariablesFactory.getAnyPrimitiveVariable("value"));
			quantity.addVariable(VariablesFactory.getIntVariable("scalingFactor"));
		}
		return quantity;
	}

	/**
	 * @return
	 */
	public static AType getPhysicalQuantityType()
	{
		if(physicalQuantity == null)
		{
			physicalQuantity = TypesFactory.getCompositeType(GeppettoAspect.get(), "PhysicalQuantity");
			physicalQuantity.setBaseType(getQuantityType());
			physicalQuantity.addVariable(VariablesFactory.getVariable("unit", getUnitType()));
		}
		return physicalQuantity;
	}

	/**
	 * @return
	 */
	private static SimpleType getUnitType()
	{
		if(unit == null)
		{
			unit = TypesFactory.getSimpleType(GeppettoAspect.get(), "Unit",TypesFactory.getStringType());
		}
		return unit;
	}

}
