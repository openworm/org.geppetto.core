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
public interface IAnalysis
{

	void doStopVisiting();

	boolean stopVisiting();

	// Root

	boolean inRoot(Root root);

	boolean outRoot(Root root);

	//Aspect 
	
	boolean inAspect(Aspect aspect);

	boolean outAspect(Aspect aspect);
	
	// Variables

	boolean inVariable(Variable variable);

	boolean outVariable(Variable variable);

	// Types

	boolean inCompositeType(CompositeType type);

	boolean outCompositeType(CompositeType type);

	boolean visitSimpleType(SimpleType type);

	boolean visitPointerType(PointerType type);

	boolean visitArrayType(ArrayType type);
	
	boolean visitAnyPrimitiveType(AnyPrimitiveType primitiveType);
	
	boolean visitIntType(IntType intType);
	
	boolean visitFloatType(FloatType intType);
	
	boolean visitStringType(StringType intType);

	// Values

	boolean inCompositeValue(CompositeValue value); // going into a branch

	boolean outCompositeValue(CompositeValue value); // coming out

	boolean visitDoubleValue(DoubleValue value);

	boolean visitFloatValue(FloatValue value);

	boolean visitPointerValue(PointerValue value);

	boolean visitStringValue(StringValue value);

	boolean visitVariableValue(VariableValue value);

	boolean visitDynamicsSpecificationValue(DynamicsSpecificationValue dynamicsSpecificationValue);

	boolean visitParameterValue(ParameterValue parameterValue);

	boolean visitFunctionValue(FunctionValue functionValue);

	boolean visitTextMetadataValue(TextMetadataValue textMetadataValue);

	boolean visitURLMetadataValue(URLMetadataValue urlMetadataValue);

	boolean visitSphereValue(SphereValue sphereValue);

	boolean visitColladaValue(ColladaValue sphereValue);

	boolean visitCylinderValue(CylinderValue sphereValue);

	boolean visitParticleValue(ParticleValue sphereValue);

	boolean visitObjValue(OBJValue objValue);

	boolean inConnectionValue(ConnectionValue connectionValue);

	boolean outConnectionValue(ConnectionValue connectionValue);

	boolean visitSkeletonAnimationValue(SkeletonAnimationValue value);







	// IT FIXME Decide visual groups
	// boolean visitVisualObjectReferenceValue(VisualObjectReferenceValue visualReferenceValue);
	//
	// boolean visitVisualGroupElementValue(VisualGroupElementValue visualGroupElementValue);
	//
	// boolean inVisualGroupValue(VisualGroupValue visualGroupValue);
	//
	// boolean outVisualGroupValue(VisualGroupValue visualGroupValue);

}
