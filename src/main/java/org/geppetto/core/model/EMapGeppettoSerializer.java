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
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emfjson.handlers.BaseURIHandler;
import org.emfjson.handlers.URIHandler;
import org.emfjson.jackson.JacksonOptions;
import org.emfjson.jackson.databind.ser.EMapEntrySerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author matteocantarelli
 *
 */
public class EMapGeppettoSerializer extends EMapEntrySerializer
{

	private JacksonOptions options;

	public EMapGeppettoSerializer(JacksonOptions options)
	{
		this.options = options;
	}

	@Override
	public void serialize(Map.Entry entry, JsonGenerator jg, SerializerProvider serializers) throws IOException
	{
		if(entry.getKey() instanceof String)
		{
			jg.writeObjectField((String) entry.getKey(), entry.getValue());
		}
		else
		{
			jg.writeStartObject();
			jg.writeFieldName("key");
			
			final URIHandler handler = options.uriHandler;
			EObject target = (EObject) entry.getKey();

			URI targetURI = deresolve(handler, target, target.eContainer());

			jg.writeObject(targetURI.toString());
			jg.writeObjectField("value", entry.getValue());
			jg.writeEndObject();
		}
	}


	protected URI deresolve(URIHandler handler, EObject target, EObject source)
	{
		if(handler == null)
		{
			handler = new BaseURIHandler();
		}

		return handler.deresolve(EcoreUtil.getURI(source), EcoreUtil.getURI(target));
	}

}
