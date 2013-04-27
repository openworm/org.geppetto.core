package org.geppetto.core.model;

import java.util.Random;

public class MathUtils {

	
	public static float scale(float min, float max, float r){
		return (float)(min + r * (max - min));
	}
	
	public static Random randomGenerator = new Random();
	
}
