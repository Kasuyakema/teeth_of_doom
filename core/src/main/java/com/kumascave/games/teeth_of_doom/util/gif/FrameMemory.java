package com.kumascave.games.teeth_of_doom.util.gif;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kumascave.games.teeth_of_doom.util.DynamicVariables;

public class FrameMemory {

	private static LinkedList<Pixmap> frames = new LinkedList<>();

	private static float delay = 0;

	public static boolean act(float delta) {
		delay -= delta;
		if (delay <= 0f) {
			Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			frames.add(pixmap);
			if (frames.size() > DynamicVariables.frameMemoryRate * DynamicVariables.frameMemoryTime) {
				Pixmap oldest = frames.pollFirst();
				oldest.dispose();
			}
			delay = 1f / DynamicVariables.frameMemoryRate;
		}
		return false;
	}

	public static List<Pixmap> getFrames() {
		@SuppressWarnings("unchecked")
		List<Pixmap> framesCpy = (List<Pixmap>) frames.clone();
		frames.clear();
		return framesCpy;
	}

	public static void dispose() {
		frames.forEach(x -> x.dispose());
		frames.clear();
	}

}
