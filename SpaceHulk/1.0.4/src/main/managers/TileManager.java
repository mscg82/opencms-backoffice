package main.managers;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import sh.Blip;
import sh.DoorState;
import sh.Face;
import sh.Marine;
import sh.Piece;
import sh.Stealer;
import sh.TileType;

public class TileManager {
	public static Tile activeTile = new Tile(ImageManager.load("/mselect.png"));
	public static Tile overwatchTile = new Tile(ImageManager
			.load("/overwatch.png"));
	public static Tile guardTile = new Tile(ImageManager.load("/guard.png"));
	public static Tile jammedTile = new Tile(ImageManager.load("/jammed.png"));

	private Tile floor_[];
	private Tile wall_[];
	private Tile door_[];
	private Tile object_[];
	private Tile entry_[];
	private Tile exit_[];
	private Tile marine_[];
	private Tile sergeant_[];
	private Tile weapons_[][];
	private Tile shoot_[];
	private Tile blip_[];
	private Tile stealer_[];

	public final static int BLIP_COUNT = 4;
	public final static int STEALER_COUNT = 2;

	public static int getTileWidth() {
		if (Tile.zoom_)
			return 32;
		else
			return 16;
	}

	public static int getTileHeight() {
		if (Tile.zoom_)
			return 32;
		else
			return 16;
	}

	public final Tile fire = new Tile(ImageManager.load("/items/48flame.png"));

	public TileManager() {
		floor_ = new Tile[26];
		for (char i = 'a'; i <= 'z'; ++i)
			floor_[i - 'a'] = new Tile(ImageManager.load("/tiles/32floor" + i + ".png"));

		wall_ = new Tile[26];
		for (char i = 'a'; i <= 'z'; ++i)
			wall_[i - 'a'] = new Tile(ImageManager.load("/tiles/32wall" + i + ".png"));

		door_ = new Tile[Face.ordered.length * DoorState.all.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			for (char s = 0; s < DoorState.all.length; ++s)
				door_[face.getValue() * DoorState.all.length + s] = new Tile(
						ImageManager.load("/tiles/" + face.getChar() + "door" + DoorState.all[s].getChar() + ".png"));
		}

		object_ = new Tile[14 + 12];
		{
			int on = 0;
			object_[on++] = new Tile(ImageManager.load("/items/archive.png"));
			object_[on++] = new Tile(ImageManager.load("/items/toxin.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cargo.png"));
			object_[on++] = new Tile(ImageManager.load("/items/damping.png"));
			object_[on++] = new Tile(ImageManager.load("/items/controls1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/controls2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/controls3.png"));
			object_[on++] = new Tile(ImageManager.load("/items/airpump1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/airpump2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/airpump3.png"));
			object_[on++] = new Tile(ImageManager.load("/items/airpump4.png"));
			object_[on++] = new Tile(ImageManager.load("/items/blood.png"));
			object_[on++] = new Tile(ImageManager.load("/items/splat.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cat.png"));

			object_[on++] = null; // new
									// Tile(ImageManager.load("/items/cryo-nw-2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-ne-2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-se-2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-sw-2.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-nw-1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-ne-1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-se-1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-sw-1.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-nw-0.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-ne-0.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-se-0.png"));
			object_[on++] = new Tile(ImageManager.load("/items/cryo-sw-0.png"));
		}

		entry_ = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			entry_[face.getValue()] = new Tile(ImageManager.load("/tiles/stlent" + face.getChar() + ".png"));
		}

		exit_ = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			exit_[face.getValue()] = new Tile(ImageManager.load("/tiles/exit" + face.getChar() + ".png"));
		}

		marine_ = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			marine_[face.getValue()] = new Tile(ImageManager.load("/items/man" + face.getChar() + ".png"));
		}

		sergeant_ = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			sergeant_[face.getValue()] = new Tile(ImageManager.load("/items/sman" + face.getChar() + ".png"));
		}

		Tile bolters_[] = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			bolters_[face.getValue()] = new Tile(ImageManager.load("/items/stormbolter" + face.getChar() + ".png"));
		}

		Tile flamers_[] = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			flamers_[face.getValue()] = new Tile(ImageManager.load("/items/flamer" + face.getChar() + ".png"));
		}
		weapons_ = new Tile[2][];
		weapons_[Marine.WEAPONS_BOLTER] = bolters_;
		weapons_[Marine.WEAPONS_FLAMER] = flamers_;

		shoot_ = new Tile[Face.ordered.length];
		for (int f = 0; f < Face.ordered.length; ++f) {
			Face face = Face.ordered[f];
			shoot_[face.getValue()] = new Tile(ImageManager.load("/items/shoot" + face.getChar() + ".png"));
		}

		blip_ = new Tile[BLIP_COUNT];
		for (int i = 0; i < BLIP_COUNT; ++i)
			blip_[i] = new Tile(ImageManager.load("/items/blip" + (i + 1)
					+ ".png"));

		stealer_ = new Tile[Face.ordered.length * STEALER_COUNT];
		for (int i = 0; i < STEALER_COUNT; ++i) {
			for (int f = 0; f < Face.ordered.length; ++f) {
				Face face = Face.ordered[f];
				stealer_[face.getValue() + i * Face.ordered.length] = new Tile(
						ImageManager.load("/items/stealer" + face.getChar() + (i + 1) + ".png"));
			}
		}
	}

	public Tile getTile(Piece p) {
		if (p instanceof Blip) {
			return blip_[p.getAnimation()];
		} else if (p instanceof Stealer) {
			Stealer s = (Stealer) p;
			return stealer_[s.getFace().getValue() + s.getAnimation() * Face.ordered.length];
		} else if (p instanceof Marine) {
			Marine m = (Marine) p;
			if (m.getType() == Marine.SERGEANT)
				return sergeant_[m.getFace().getValue()];
			else
				return marine_[m.getFace().getValue()];
		} else
			return null;
	}
	
	public Tile getWeapon(int weaponType, Face f) {
		if(weaponType >= weapons_.length)
			return null;
		else {
			if(f.getValue() >= weapons_[weaponType].length)
				return null;
			else
				return weapons_[weaponType][f.getValue()];
		}
	}

	public Tile getShoot(Face f) {
		return shoot_[f.getValue()];
	}

	public int getShootOffset() {
		return -2;
	}

	public Tile getTile(int tile) {
		if ((tile & TileType.TYPE_MASK) == TileType.FLOOR)
			return floor_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.WALL)
			return wall_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.DOOR)
			return door_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.BULKHEAD)
			return door_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.OBJECT)
			return object_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.ENTRY)
			return entry_[tile & TileType.TILE_MASK];
		else if ((tile & TileType.TYPE_MASK) == TileType.EXIT)
			return exit_[tile & TileType.TILE_MASK];
		else
			return null;
	}
}
