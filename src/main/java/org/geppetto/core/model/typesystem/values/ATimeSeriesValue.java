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
package org.geppetto.core.model.typesystem.values;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract node used for nodes that don't have other nodes as children, a leaf node.
 * 
 * @author matteocantarelli
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class ATimeSeriesValue extends AValue
{
	// A state variable has intrinsic dynamics that allow for it to change as part of the evolution of the model.
	private List<QuantityValue> _timeSeries = new ArrayList<QuantityValue>();
	private Unit unit;
	private boolean watched;

	public ATimeSeriesValue(String name)
	{
		super(name);
	}

	public Unit getUnit()
	{
		return unit;
	}

	public void setUnit(Unit unit)
	{
		this.unit = unit;
	}

	@Override
	public String toString()
	{
		return _name + "[unit=" + unit + "_timeSeries=" + _timeSeries + "]";
	}

	public void addQuantity(QuantityValue value)
	{
		_timeSeries.add(value);
	}

	public List<QuantityValue> getTimeSeries()
	{
		return _timeSeries;
	}

	public QuantityValue consumeFirstValue()
	{
		if(_timeSeries.size() == 0)
		{
			return null;
		}
		QuantityValue first = _timeSeries.get(0);
		_timeSeries.remove(0);
		return first;
	}

	public boolean isWatched()
	{
		return watched;
	}

	public void setWatched(boolean watched)
	{
		this.watched = watched;
	}
}
