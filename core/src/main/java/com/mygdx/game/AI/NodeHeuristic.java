package com.mygdx.game.AI;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

/**
 * Heuristic function for use in A* pathfinding
 */
public class NodeHeuristic implements Heuristic<Node> {
    /**
     * Euclidean distance squared
     *
     * @param node    src
     * @param endNode dst
     * @return distance^2
     */
    @Override
    public float estimate(Node node, Node endNode) {
        Vector2 a = node.getPosition();
        Vector2 b = endNode.getPosition();
        return Vector2.dst2(a.x, a.y, b.x, b.y);
    }
}
