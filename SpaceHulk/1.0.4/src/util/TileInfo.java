package util;

import main.managers.Tile;

public class TileInfo {
	
	private Tile tile;
	private int offsetX;
	private int offsetY;
	
	public TileInfo(Tile tile, int offsetX, int offsetY) {
		super();
		setTile(tile);
		setOffsetX(offsetX);
		setOffsetY(offsetY);
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
		
}
