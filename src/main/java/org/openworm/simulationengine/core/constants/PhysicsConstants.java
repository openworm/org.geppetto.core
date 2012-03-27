package org.openworm.simulationengine.core.constants;

public class PhysicsConstants {

	public static final float M_PI = 3.1415927f;
	public static final int RAND_MAX = 0x7fff;

	public static final float RHO0 = 1000.0f;
	public static final float STIFFNESS = 0.75f;
	public static final float H = 3.34f;

	public static final float MASS = 0.001f;
	public static final float HASH_GRID_CELL_SIZE = 2.0f * H;
	public static final float HASH_GRID_CELL_SIZE_INV = 1.0f / HASH_GRID_CELL_SIZE;
	public static final float SIMULATION_SCALE = 0.004f;
	public static final float SIMULATION_SCALE_INV = 1.0f / SIMULATION_SCALE;
	public static final float MU = 10.0f;
	public static final float TIME_STEP = 0.0042f; 
	public static final float CFLLimit = 100.0f;

	public static final float DAMPING = 0.75f;

	public static final float W_POLY_6_COEFFICIENT = (float) (315.0f / ( 64.0f * M_PI * Math.pow( H * SIMULATION_SCALE, 9.0f ) ));
	public static final float GRAD_W_SPIKY_COEFFICIENT= (float) (-45.0f / ( M_PI * Math.pow( H * SIMULATION_SCALE, 6.0f ) ));
	public static final float DEL_2_W_VISCOSITY_COEFFICIENT = -GRAD_W_SPIKY_COEFFICIENT;

	public static final float GRAVITY_X = 0.0f;
	public static final float GRAVITY_Y = -9.8f;
	public static final float GRAVITY_Z = 0.0f;
}
