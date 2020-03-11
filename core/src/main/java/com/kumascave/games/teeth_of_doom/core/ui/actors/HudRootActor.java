package com.kumascave.games.teeth_of_doom.core.ui.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kumascave.games.teeth_of_doom.core.GameContext;

public class HudRootActor extends Group implements HudElement {

	private OrthographicCamera cam = (OrthographicCamera) GameContext.getHudStage().getCamera();

	public HudRootActor() {
		super();
		initActors();
		updateGeometry();
		this.setTouchable(Touchable.childrenOnly);
	}

	private void initActors() {
		this.addActor(new GridOverlay());
		this.addActor(new CollisionOverlay());
		this.addActor(new NavigationOverlay());
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
