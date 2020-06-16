
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
