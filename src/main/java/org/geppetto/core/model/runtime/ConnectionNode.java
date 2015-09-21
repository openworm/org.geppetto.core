package org.geppetto.core.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.geppettomodel.ConnectionType;
import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * 
 * @author Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class ConnectionNode extends ANode
{

	private String _entityInstancePath;
	private String _aspectInstancePath;
	private ConnectionType _type;
	private List<ANode> _customNodes = new ArrayList<ANode>();
	private List<VisualObjectReferenceNode> _visualReferences = new ArrayList<VisualObjectReferenceNode>();

	public List<VisualObjectReferenceNode> getVisualReferences()
	{
		return _visualReferences;
	}

	public void setVisualReferences(List<VisualObjectReferenceNode> _visualReferences)
	{
		this._visualReferences = _visualReferences;
	}

	public List<ANode> getCustomNodes()
	{
		return _customNodes;
	}

	public void setCustomNodes(List<ANode> customProperties)
	{
		this._customNodes = customProperties;
	}

	public ConnectionNode(String id, AspectNode aspectNode)
	{
		super(id);
		if(aspectNode != null)
		{
			_aspectInstancePath = aspectNode.getInstancePath();
		}
	}

	public void setEntityInstancePath(String entityId)
	{
		this._entityInstancePath = entityId;
	}

	@Override
	public String getAspectInstancePath()
	{
		return _aspectInstancePath;
	}

	public String getEntityInstancePath()
	{
		return this._entityInstancePath;
	}

	public void setType(ConnectionType connectionType)
	{
		this._type = connectionType;
	}

	public ConnectionType getConnectionType()
	{
		return this._type;
	}

	@Override
	public boolean apply(IStateVisitor visitor)
	{
		if(visitor.inConnectionNode(this))
		{
			for(ANode a : this.getCustomNodes())
			{
				a.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}

			for(VisualObjectReferenceNode vis : this.getVisualReferences())
			{
				vis.apply(visitor);
				if(visitor.stopVisiting())
				{
					break;
				}
			}

		}
		return visitor.outConnectionNode(this);
	}
}
