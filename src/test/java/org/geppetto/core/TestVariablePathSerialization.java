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

package org.geppetto.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.geppetto.core.data.model.AVariable;
import org.geppetto.core.data.model.ArrayVariable;
import org.geppetto.core.data.model.SimpleType;
import org.geppetto.core.data.model.SimpleVariable;
import org.geppetto.core.data.model.StructuredType;
import org.geppetto.core.data.model.SimpleType.Type;
import org.geppetto.core.utilities.VariablePathSerializer;
import org.junit.Test;

public class TestVariablePathSerialization {

	@Test
	public void testVariablePathSerialization() {
		List<String> results = Arrays.asList("particle.position.x",	
											 "particle.position.y", 
											 "particle.position.z",
											 "particle.velocity.x",
											 "particle.velocity.y",
											 "particle.velocity.z");
		
		SimpleType floatType = new SimpleType();
		floatType.setType(Type.FLOAT);
		
		// structure type vector
		StructuredType vector = new StructuredType();
		List<AVariable> vectorVars = new ArrayList<AVariable>();
		SimpleVariable x = new SimpleVariable();
		SimpleVariable y = new SimpleVariable();
		SimpleVariable z = new SimpleVariable();
		x.setName("x");
		x.setType(floatType);
		y.setName("y");
		y.setType(floatType);
		z.setName("z");
		z.setType(floatType);
		vectorVars.addAll(Arrays.asList(x, y, z));
		vector.setVariables(vectorVars);
		
		// structure type particle
		StructuredType particle = new StructuredType();
		List<AVariable> particleVars = new ArrayList<AVariable>();
		SimpleVariable position = new SimpleVariable();
		SimpleVariable velocity = new SimpleVariable();
		position.setName("position");
		position.setType(vector);
		velocity.setName("velocity");
		velocity.setType(vector);
		particleVars.addAll(Arrays.asList(position, velocity));
		particle.setVariables(particleVars);
		
		// array of particles
		ArrayVariable particles = new ArrayVariable();
		particles.setName("particle");
		particles.setType(particle);
		particles.setSize(100);
		
		List<String> varPaths = new ArrayList<String>();
		VariablePathSerializer.GetFullVariablePath(particles, "", varPaths);
		
		for(int i=0; i< varPaths.size(); i++)
		{
			System.out.println(varPaths.get(i));
			Assert.assertEquals(varPaths.get(i), results.get(i));
		}
	}

}
