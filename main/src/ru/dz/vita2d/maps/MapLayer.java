package ru.dz.vita2d.maps;

import javafx.scene.paint.Color;

public class MapLayer
{
	private String	readableName;
	private String	id;
	private Color	color;
	private int 	priority;
	
	private boolean enabled = true;
	
	MapLayer(String shortName, String readableName, String color, int priority) {
		id = shortName;
		this.readableName = readableName;
		this.priority = priority;
		
		try { this.color = Color.valueOf(color); } catch (IllegalArgumentException e) {
			this.color = Color.RED;
		}
		
		
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean selected) {
		enabled = selected;
	}

	public String getReadableName() {
		return readableName;
	}

	/** 
	 * <p>Id (reference name) of layer.</p>
	 * <p>Used in JSON data to attach object to some layer.</p>
	 * 
	 * @return String identifier of layer.
	 */
	public String getId() {		return id;	}

	/** 
	 * 
	 * @return Color to paint this layer with. Used in MapPath.
	 */
	public Color getColor() {		return color;	}

	/**
	 * 
	 * @return Priority (z coordinate) of layer. Bigger one is above (painted later).
	 */
	public int getPriority() {		return priority;	}

}
