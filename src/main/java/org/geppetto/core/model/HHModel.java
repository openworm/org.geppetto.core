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

package org.geppetto.core.model;

public class HHModel extends AModel  
{

	private float _V;
	private float _x_n;
	private float _x_m;
	private float _x_h;
	private float _i;
	
	// max conductances with default values
	private float _maxG_K = 36;
	private float _maxG_Na = 120;
	private float _maxG_Leak = (float) 0.3;
	
	// reverse potentials with default values
	private float _E_K = -12;
	private float _E_Na = 115;
	private float _E_Leak = (float) 10.613;
	
	/*
	 * A constructor with a typical set of initial conditions. Conductances and reverse potentials will pick-up default values.
	 * */
	public HHModel(String id,float V, float x_n, float x_m, float x_h, float i){
		super(id);
		_V = V; 
		_x_n = x_n;
		_x_m = x_m;
		_x_h = x_h;
		_i=i;
	}
	
	/* 
	 * A constructor with the full set of initial conditions including max conductances and reverse potentials.
	 * */
	public HHModel(String id,float V, float x_n, float x_m, float x_h, float i, float maxGK, float maxGNa, float maxGLeak, float EK, float ENa, float ELeak){
		super(id);
		
		_V = V; 
		_x_n = x_n;
		_x_m = x_m;
		_x_h = x_h;
		_i=i;
		
		_maxG_K = maxGK;
		_maxG_Na = maxGNa;
		_maxG_Leak = maxGLeak;
		
		_E_K = EK;
		_E_Na = ENa;
		_E_Leak = ELeak;
	}

	public float getI() {
		return _i;
	}
	
	public float getV() {
		return _V;
	}

	public float getXn() {
		return _x_n;
	}

	public float getXm() {
		return _x_m;
	}

	public float getXh() {
		return _x_h;
	}

	public void setI(Float i) 
	{
		_i=i;
	}

	private void setMaxG_K(float maxG_K) {
		_maxG_K = maxG_K;
	}

	private float getMaxG_K() {
		return _maxG_K;
	}

	private void setMaxG_Na(float maxG_Na) {
		_maxG_Na = maxG_Na;
	}

	private float getMaxG_Na() {
		return _maxG_Na;
	}

	private void setMaxG_Leak(float maxG_Leak) {
		_maxG_Leak = maxG_Leak;
	}

	private float getMaxG_Leak() {
		return _maxG_Leak;
	}

	private void setE_K(float e_K) {
		_E_K = e_K;
	}

	private float getE_K() {
		return _E_K;
	}

	private void setE_Na(float e_Na) {
		_E_Na = e_Na;
	}

	private float getE_Na() {
		return _E_Na;
	}

	private void setE_Leak(float e_Leak) {
		_E_Leak = e_Leak;
	}

	private float getE_Leak() {
		return _E_Leak;
	}
}
