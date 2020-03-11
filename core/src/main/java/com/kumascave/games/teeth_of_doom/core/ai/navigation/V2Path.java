package com.kumascave.games.teeth_of_doom.core.ai.navigation;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.math.Vector2;

public class V2Path extends DefaultGraphPath<Vector2> implements SmoothableGraphPath<Vector2, Vector2> {

	@Override
	public Vector2 getNodePosition(int index) {
		return get(index);
	}

	@Override
	public void swapNodes(int index1, int index2) {
		nodes.swap(index1, index2);
	}

	@Override
	public void truncatePath(int newLength) {
		nodes.truncate(newLength);
	}

}
