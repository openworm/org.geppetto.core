

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
