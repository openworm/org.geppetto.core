package org.geppetto.core.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.simulation.ConnectionType;
import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ConnectionNode extends ANode{
	
	private String _entityInstancePath;
	private ConnectionType _type;
	private List<ANode> _customProperties = new ArrayList<ANode>();
	
	public List<ANode> getCustomProperties() {
		return _customProperties;
	}

	public void setCustomProperties(List<ANode> customProperties) {
		this._customProperties = customProperties;
	}

	public ConnectionNode(String id) {
		super(id);
	}

	public void setEntityInstancePath(String entityId){
		this._entityInstancePath = entityId;
	}
	
	public String getEntityInstancePath(){
		return this._entityInstancePath;
	}
	
	public void setType(ConnectionType connectionType){
		this._type = connectionType;
	}
	
	public ConnectionType getConnectionType(){
		return this._type;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitConnectionNode(this);
	}
}
