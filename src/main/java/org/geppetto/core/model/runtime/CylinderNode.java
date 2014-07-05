package org.geppetto.core.model.runtime;

import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.visualisation.model.Point;

/**
 * Node use to define a cylinder for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class CylinderNode extends AVisualObjectNode{

	private Double _radiusTop;
    private Double _radiusBottom;
    private Double _height;
    private Point _distal;
    
    public Double getRadiusTop() {
		return _radiusTop;
	}

	public void setRadiusTop(Double radiusTop) {
		this._radiusTop = radiusTop;
	}

	public Double getRadiusBottom() {
		return _radiusBottom;
	}

	public void setRadiusBottom(Double radiusBottom) {
		this._radiusBottom = radiusBottom;
	}

	public Double getHeight() {
		return _height;
	}

	public void setHeight(Double height) {
		this._height = height;
	}

	public Point getDistal() {
		return _distal;
	}

	public void setDistal(Point distal) {
		this._distal = distal;
	}
    
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitCylinderNode(this);
	}
}
