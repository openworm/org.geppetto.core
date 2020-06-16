
package org.geppetto.core.model;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emfjson.jackson.handlers.BaseURIHandler;
import org.emfjson.jackson.handlers.URIHandler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author matteocantarelli
 *
 */
public class EMapGeppettoSerializer extends StdSerializer<Map.Entry> {
	private URIHandler uriHandler = null;
	
	public EMapGeppettoSerializer (URIHandler handler ) {
		super(Map.Entry.class);
		this.uriHandler = handler;
	}

	protected URI deresolve(URIHandler handler, EObject target, EObject source)
	{
		if(handler == null)
		{
			handler = new BaseURIHandler();
		}

		return handler.deresolve(EcoreUtil.getURI(source), EcoreUtil.getURI(target));
	}

	@Override
	public void serialize(Map.Entry entry, JsonGenerator jg, SerializerProvider arg2)
			throws IOException {
		if(entry.getKey() instanceof String)
		{
			jg.writeObjectField((String) entry.getKey(), entry.getValue());
		}
		else
		{
			jg.writeStartObject();
			jg.writeFieldName("key");
			
			final URIHandler handler = this.uriHandler;
			EObject target = (EObject) entry.getKey();

			URI targetURI = deresolve(handler, target, target.eContainer());

			jg.writeObject(targetURI.toString());
			jg.writeObjectField("value", entry.getValue());
			jg.writeEndObject();
		}
		
	}

}
