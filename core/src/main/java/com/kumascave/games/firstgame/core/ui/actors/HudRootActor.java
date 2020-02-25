package com.kumascave.games.firstgame.core.ui.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.firstgame.core.GameContext;

import lombok.Getter;

public class HudRootActor extends Group implements HudElement {

	private OrthographicCamera cam = (OrthographicCamera) GameContext.inst().getHudStage().getCamera();

	@Getter
	private GridOverlay gridOverlay;

	@Getter
	private CollisionOverlay collisionOverlay;

	public HudRootActor() {
		super();
		initActors();
		updateGeometry();
		this.setTouchable(Touchable.childrenOnly);
	}

	private void initActors() {
		gridOverlay = new GridOverlay();
		collisionOverlay = new CollisionOverlay();
		this.addActor(gridOverlay);
		this.addActor(collisionOverlay);
		this.addActor(new FPSCounter());
		this.addActor(new MenuButton());
	}

	@Override
	public void updateGeometry() {
		this.setHeight(cam.viewportHeight * cam.zoom);
		this.setWidth(cam.viewportWidth * cam.zoom);

		float posX = cam.position.x - cam.viewportWidth * cam.zoom * 0.5f;
		float posY = cam.position.y - cam.viewportHeight * cam.zoom * 0.5f;
		this.setPosition(posX, posY);

		getChildren().forEach(x -> ((HudElement) x).updateGeometry());
	}

}
