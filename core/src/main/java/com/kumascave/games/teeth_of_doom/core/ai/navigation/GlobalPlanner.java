package com.kumascave.games.teeth_of_doom.core.ai.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Streams;
import com.kumascave.games.teeth_of_doom.core.Constants;
import com.kumascave.games.teeth_of_doom.core.GameContext;
import com.kumascave.games.teeth_of_doom.core.world.PPos;
import com.kumascave.games.teeth_of_doom.util.Util;

public class GlobalPlanner {

	public static NavigationGraph graph = new NavigationGraph();

	public static List<Vector2> findPath(Vector2 start, Vector2 end, float radius) {
		Heuristic<Vector2> heuristic = (node, endNode) -> node.dst2(endNode);
		NavigationGraph graphWithRadius = graph.withRadius(radius);
		IndexedAStarPathFinder<Vector2> pathFinder = new IndexedAStarPathFinder<Vector2>(graphWithRadius);
		V2Path out = new V2Path();
		pathFinder.searchNodePath(graph.mapToNearestNode(start), graph.mapToNearestNode(end), heuristic, out);
		List<Vector2> path = Streams.stream(out).collect(Collectors.toList());
		path = shortcut(path, radius);
		// remove starting position. we are already there
		path.remove(0);
		return path;
	}

	private static List<Vector2> shortcut(List<Vector2> path, float radius) {
		if (path.size() < 3) {
			return path;
		}
		List<Vector2> shortened = new ArrayList<>();
		shortened.add(path.get(0));
		for (int index = 0; index < path.size(); index++) {
			for (int candidateI = path.size() - 1; candidateI > index; candidateI--) {
				Vector2 node = path.get(index);
				Vector2 candidate = path.get(candidateI);
				boolean pathValid = true;
				if (candidateI != index + 1) {
					pathValid = checkConnection(radius, node, candidate);
				}
				if (pathValid) {
					shortened.add(candidate);
					index = path.indexOf(candidate);
					break;
				}
			}
		}
		return shortened;
	}

	protected static boolean checkConnection(float radius, Vector2 start, Vector2 end) {
		boolean pathValid = true;
		Vector2 newConnection = end.cpy().sub(start);
		int steps = (int) (newConnection.len() / Constants.PATHFINDING_PATH_CHECK_STEP);
		List<Vector2> nodesOnNewConnection = new ArrayList<>();
		Util.iterate(steps, x -> nodesOnNewConnection
				.add(start.cpy().add(newConnection.setLength((x + 1) * Constants.PATHFINDING_PATH_CHECK_STEP))));
		for (Vector2 nodeToCheck : nodesOnNewConnection) {
			if (!checkNode(nodeToCheck, radius)) {
				pathValid = false;
				break;
			}
		}
		return pathValid;
	}

	public static boolean checkNode(Vector2 node, float radius) {
		if (node == null) {
			return false;
		}

		return radius + Constants.PATHFINDING_BUFFER < GameContext.getGameStage().getTileLayer().getMap()
				.getDistanceMap().getTile(PPos.fromWorldPos(node));
	}
}
