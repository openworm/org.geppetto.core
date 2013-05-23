package org.geppetto.core.constants;

public class PhysicsConstants {
	
	// TODO: add reference of where these constant / formulas come from
	public static final float M_PI = 3.1415927f;
	public static final int RAND_MAX = 0x7fff;

	public static final float RHO0 = 1000.0f;
	public static final float STIFFNESS = 0.75f;
	public static final float H = 3.34f;
	public static final float R0 = 0.5f * H;

	public static final float MASS = 0.003f;
	public static final float HASH_GRID_CELL_SIZE = 2.0f * H;
	public static final float HASH_GRID_CELL_SIZE_INV = 1.0f / HASH_GRID_CELL_SIZE;
	public static final float SIMULATION_SCALE = 0.004f;
	public static final float SIMULATION_SCALE_INV = 1.0f / SIMULATION_SCALE;
	public static final float MU = 10.0f;
	public static final float TIME_STEP = 0.001f; 
	public static final float CFLLimit = 100.0f;

	public static final float DAMPING = 0.75f;

	public static final float W_POLY_6_COEFFICIENT = (float) (315.0f / ( 64.0f * M_PI * Math.pow( H * SIMULATION_SCALE, 9.0f ) ));
	public static final float GRAD_W_SPIKY_COEFFICIENT= (float) (-45.0f / ( M_PI * Math.pow( H * SIMULATION_SCALE, 6.0f ) ));
	public static final float DEL_2_W_VISCOSITY_COEFFICIENT = -GRAD_W_SPIKY_COEFFICIENT;

	public static final float GRAVITY_X = 0.0f;
	public static final float GRAVITY_Y = -9.8f;
	public static final float GRAVITY_Z = 0.0f;
	
	// B. Solenthaler's dissertation, formula 3.6 (end of page 30)
	public static final float BETA = TIME_STEP*TIME_STEP * MASS*MASS * 2 / ( RHO0*RHO0 );
	public static final float BETA_INV = 1.0f / BETA;
	
	public static final float DELTA = getDELTA();
	
	private static float getDELTA(){
	    float x[] = { 1, 1, 0,-1,-1,-1, 0, 1, 1, 1, 0,-1,-1,-1, 0, 1, 1, 1, 0,-1,-1,-1, 0, 1, 2,-2, 0, 0, 0, 0, 0, 0 };
	    float y[] = { 0, 1, 1, 1, 0,-1,-1,-1, 0, 1, 1, 1, 0,-1,-1,-1, 0, 1, 1, 1, 0,-1,-1,-1, 0, 0, 2,-2, 0, 0, 0, 0 };
	    float z[] = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,-1,-1,-1,-1,-1,-1,-1,-1, 0, 0, 0, 0, 2,-2, 1,-1 };

		float sum1_x = 0.f;
		float sum1_y = 0.f;
		float sum1_z = 0.f;
	    float sum1 = 0.f, sum2 = 0.f;
		float v_x = 0.f;
		float v_y = 0.f;
		float v_z = 0.f;
		float dist;
		float particleRadius = (float) Math.pow(MASS/RHO0,1.f/3.f);
		float h_r_2;									

	    for (int i = 0; i < 32; i++)
	    {
			v_x = x[i] * 0.8f * particleRadius;
			v_y = y[i] * 0.8f * particleRadius;
			v_z = z[i] * 0.8f * particleRadius;

	        dist = (float) Math.sqrt(v_x*v_x+v_y*v_y+v_z*v_z);

	        if (dist <= H)
	        {
				h_r_2 = (float) Math.pow((H*SIMULATION_SCALE - dist),2);

	            sum1_x += h_r_2 * v_x / dist;
				sum1_y += h_r_2 * v_y / dist;
				sum1_z += h_r_2 * v_z / dist;

	            sum2 += h_r_2 * h_r_2;
	        }
	    }

		sum1 = sum1_x*sum1_x + sum1_y*sum1_y + sum1_z*sum1_z;

		return  1.0f / (BETA * GRAD_W_SPIKY_COEFFICIENT * GRAD_W_SPIKY_COEFFICIENT * (sum1 + sum2));
	}
}
