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
package org.geppetto.core.model;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.emfjson.common.EObjects;
import org.emfjson.jackson.JacksonOptions;
import org.emfjson.jackson.databind.ser.EObjectSerializer;
import org.geppetto.model.GeppettoPackage;
import org.geppetto.model.ISynchable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This serializer makes it so that only objects for which synched==false are serialized. Once an object is serialized then the attribute synched is set to true.
 * 
 * TODO: There is an assumption underlying this logic that, as soon as something is serialized, it can be considered synched while in fact for the system to be more robust we should considered things
 * synched only once we receive some kind of ack from the frontend.
 * 
 * @author matteocantarelli
 *
 */
public class SynchableSerializer extends EObjectSerializer
{
	JacksonOptions options;

	public SynchableSerializer(JacksonOptions options)
	{
		super(options);
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.emfjson.jackson.databind.ser.EObjectSerializer#serialize(org.eclipse.emf.ecore.EObject, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(EObject object, JsonGenerator jg, SerializerProvider provider) throws IOException
	{
		ISynchable synchable = (ISynchable) object;
		if(object.eContainer() != null && EObjects.isContainmentProxy(object.eContainer(), object))
		{
			this.options.referenceSerializer.serialize(object.eContainer(), object, jg, provider);
			return;
		}

		if(synchable.isSynched())
		{
			jg.writeStartObject();
			jg.writeBooleanField(GeppettoPackage.Literals.ISYNCHABLE__SYNCHED.getName(), synchable.isSynched());
			jg.writeEndObject();
			return;
		}
		else
		{
			super.serialize(object, jg, provider);
			synchable.setSynched(true);
		}

	}

}
