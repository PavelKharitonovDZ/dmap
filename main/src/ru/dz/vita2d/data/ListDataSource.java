package ru.dz.vita2d.data;

import java.util.function.Consumer;

import org.json.JSONObject;

public class ListDataSource implements IListDataSource {
	private JSONObject origJson;

	public ListDataSource(JSONObject data) {
		this.origJson = data;		
	}

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IListDataSource#getJson()
	 */
	@Override
	public JSONObject getJson() {
		return origJson;
	}

	/* (non-Javadoc)
	 * @see ru.dz.vita2d.data.IListDataSource#forEach(java.util.function.Consumer)
	 */
	@Override
	public void forEach( Consumer<IEntityDataSource> recordSink )
	{
		
	}
	
}
