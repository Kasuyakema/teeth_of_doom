package com.kumascave.games.teeth_of_doom.core.world.tiles;

import com.kumascave.games.teeth_of_doom.core.save.Include;
import com.kumascave.games.teeth_of_doom.core.world.TPos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;;

@NoArgsConstructor
public abstract class Tile {

	@Getter
	@Setter
	TileStatusAdapter status;

	@Getter
	@Setter
	@Include
	private int x, y;

	@Getter
	@Setter
	private boolean colliding;

	protected Tile(int x, int y, boolean colliding) {
		this.setGridPosition(x, y);
		this.colliding = colliding;
	}

	public void setGridPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	public TPos getGridPosition() {
		return new TPos(getX(), getY());
	}

	// @Override
	// protected void drawDebugBounds(ShapeRenderer shapes) {
	// if (!getDebug()) {
	// return;
	// }
	// shapes.set(ShapeType.Line);
	// shapes.setColor(colliding ? Color.BLUE : Color.RED);
	// shapes.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(),
	// getHeight(), getScaleX(), getScaleY(),
	// getRotation());
	// }

	public void dispose() {
	}

	public abstract void destroy();

	public void initStatus() {
		if (status == null) {
			status = new TileStatusAdapter(this);
		}
	}

}
