package application;

import ru.dz.vita2d.maps.IMapData;

public interface IMapScene {

	public void setMapData(IMapData mapData); 

	/** Set zoom factor so that we can see all of the image. */
	public void setOverviewScale();
}
