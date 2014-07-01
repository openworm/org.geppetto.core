package org.geppetto.core.model.state;

import org.geppetto.core.model.state.visitors.IStateVisitor;
import org.geppetto.core.visualisation.model.Point;

/**
 * Node use to define a cylinder for visualization and serialization
 * 
 * @author  Jesus R. Martinez (jesus@metacell.us)
 *
 */
public class CylinderNode extends AVisualObjectNode{

	private Double radiusTop;
    private Double radiusBottom;
    private Double height;
    private Point distal;
    
    public Double getRadiusTop() {
		return radiusTop;
	}

	public void setRadiusTop(Double radiusTop) {
		this.radiusTop = radiusTop;
	}

	public Double getRadiusBottom() {
		return radiusBottom;
	}

	public void setRadiusBottom(Double radiusBottom) {
		this.radiusBottom = radiusBottom;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Point getDistal() {
		return distal;
	}

	public void setDistal(Point distal) {
		this.distal = distal;
	}
    
	@Override
	public boolean apply(IStateVisitor visitor) {
		return visitor.visitCylinderNode(this);
	}
}
