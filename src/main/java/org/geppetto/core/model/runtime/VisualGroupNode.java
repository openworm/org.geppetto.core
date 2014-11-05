package org.geppetto.core.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.geppetto.core.model.state.visitors.IStateVisitor;

/**
 * Combines nodes into one single group. 
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 */
public class VisualGroupNode extends ACompositeNode{

	private String _type;
	private String _lowSpectrumColor;
	private String _highSpectrumColor;
	private List<VisualGroupElementNode> _visualGroupElements = new ArrayList<VisualGroupElementNode>();
	
	public VisualGroupNode(String id) {
		super(id);
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	public String getLowSpectrumColor() {
		return _lowSpectrumColor;
	}

	public void setLowSpectrumColor(String lowSpectrumColor) {
		this._lowSpectrumColor = lowSpectrumColor;
	}

	public String getHighSpectrumColor() {
		return _highSpectrumColor;
	}

	public void setHighSpectrumColor(String highSpectrumColor) {
		this._highSpectrumColor = highSpectrumColor;
	}
	
	public List<VisualGroupElementNode> getVisualGroupElements() {
		return _visualGroupElements;
	}

	public void setVisualGroupElements(
			List<VisualGroupElementNode> visualGroupElements) {
		this._visualGroupElements = visualGroupElements;
	}

	@Override
	public boolean apply(IStateVisitor visitor) {
		if(visitor.inVisualGroupNode(this)){
			for(VisualGroupElementNode element : this.getVisualGroupElements()){
				element.apply(visitor);
				if (visitor.stopVisiting()) {
					break;
				}
			}
		}
		return visitor.outVisualGroupNode(this);
	}

}
