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
package org.geppetto.core.model.typesystem.visitor;

import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.model.typesystem.Root;
import org.geppetto.core.model.typesystem.aspect.Aspect;
import org.geppetto.core.model.typesystem.types.AnyPrimitiveType;
import org.geppetto.core.model.typesystem.types.ArrayType;
import org.geppetto.core.model.typesystem.types.CompositeType;
import org.geppetto.core.model.typesystem.types.FloatType;
import org.geppetto.core.model.typesystem.types.IntType;
import org.geppetto.core.model.typesystem.types.PointerType;
import org.geppetto.core.model.typesystem.types.SimpleType;
import org.geppetto.core.model.typesystem.types.StringType;
import org.geppetto.core.model.typesystem.values.ColladaValue;
import org.geppetto.core.model.typesystem.values.CompositeValue;
import org.geppetto.core.model.typesystem.values.ConnectionValue;
import org.geppetto.core.model.typesystem.values.CylinderValue;
import org.geppetto.core.model.typesystem.values.DoubleValue;
import org.geppetto.core.model.typesystem.values.DynamicsSpecificationValue;
import org.geppetto.core.model.typesystem.values.FloatValue;
import org.geppetto.core.model.typesystem.values.FunctionValue;
import org.geppetto.core.model.typesystem.values.OBJValue;
import org.geppetto.core.model.typesystem.values.ParameterValue;
import org.geppetto.core.model.typesystem.values.ParticleValue;
import org.geppetto.core.model.typesystem.values.PointerValue;
import org.geppetto.core.model.typesystem.values.SkeletonAnimationValue;
import org.geppetto.core.model.typesystem.values.SphereValue;
import org.geppetto.core.model.typesystem.values.StringValue;
import org.geppetto.core.model.typesystem.values.TextMetadataValue;
import org.geppetto.core.model.typesystem.values.URLMetadataValue;
import org.geppetto.core.model.typesystem.values.VariableValue;
import org.geppetto.core.model.typesystem.variables.Variable;

/**
 * @author matteocantarelli
 *
 */
public class AnalysisVisitor implements IAnalysis
{

	boolean _stopVisiting = false;

	protected GeppettoExecutionException exception = null;

	@Override
	public boolean stopVisiting()
	{
		return _stopVisiting;
	}

	@Override
	public void doStopVisiting()
	{
		_stopVisiting = true;
	}



	public void postProcessVisit() throws GeppettoExecutionException
	{
		if(exception != null)
		{
			throw new GeppettoExecutionException(exception);
		}
	}

	@Override
	public boolean inRoot(Root root)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outRoot(Root root)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean inAspect(Aspect aspect)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outAspect(Aspect aspect)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean inVariable(Variable variable)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outVariable(Variable variable)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean inCompositeType(CompositeType type)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outCompositeType(CompositeType type)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitSimpleType(SimpleType type)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitPointerType(PointerType type)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitArrayType(ArrayType type)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitAnyPrimitiveType(AnyPrimitiveType primitiveType)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitIntType(IntType intType)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitFloatType(FloatType intType)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitStringType(StringType intType)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean inCompositeValue(CompositeValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outCompositeValue(CompositeValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitDoubleValue(DoubleValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitFloatValue(FloatValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitPointerValue(PointerValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitStringValue(StringValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitVariableValue(VariableValue value)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitDynamicsSpecificationValue(DynamicsSpecificationValue dynamicsSpecificationValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitParameterValue(ParameterValue parameterValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitFunctionValue(FunctionValue functionValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitTextMetadataValue(TextMetadataValue textMetadataValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitURLMetadataValue(URLMetadataValue urlMetadataValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitSphereValue(SphereValue sphereValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitColladaValue(ColladaValue sphereValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitCylinderValue(CylinderValue sphereValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitParticleValue(ParticleValue sphereValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitObjValue(OBJValue objValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean inConnectionValue(ConnectionValue connectionValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean outConnectionValue(ConnectionValue connectionValue)
	{
		return !_stopVisiting;
	}

	@Override
	public boolean visitSkeletonAnimationValue(SkeletonAnimationValue value)
	{
		return !_stopVisiting;
	}
}
