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

package org.geppetto.core.utilities;

import java.util.ArrayList;
import java.util.List;

public class MatrixBuilder
{
	/**
	 * @param dimension
	 *            : quadratic matrix size
	 * @param flatMatrices
	 *            : a list of flattened out quadratic matrices (row by row)
	 * @return
	 */
	public static List<List<List<Double>>> buildQuadraticMatrices(int dimension, double[] flatMatrices)
	{
		// data structures to hold matrices as they get built
		List<List<List<Double>>> transformations = new ArrayList<List<List<Double>>>();
		List<List<Double>> matrix = new ArrayList<List<Double>>();
		List<Double> row = new ArrayList<Double>();

		// build list of matrices
		for(int i = 0; i < flatMatrices.length; i++)
		{
			// build rows and add to matrix when completed
			if(row.size() < dimension)
			{
				// add to row
				row.add((Double) flatMatrices[i]);
			}

			// check if row is completed
			if(row.size() == dimension)
			{
				// row completed - add to matrix
				matrix.add(row);
				// init new row
				row = new ArrayList<Double>();
			}

			// check if matrix is completed and add to list of matrices
			if(matrix.size() == dimension)
			{
				// matrix is complted - add to list of matrices
				transformations.add(matrix);
				// create new matrix
				matrix = new ArrayList<List<Double>>();
			}

		}
		return transformations;
	}
}
