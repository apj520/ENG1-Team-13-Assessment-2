package com.mygdx.game.AI;

import com.badlogic.gdx.math.Vector2;

/**
 * A node in the A* pathfinding graph
 */
public class Node {
    private final Vector2 position;
    public float cost;

    /**
     * Position the node exists at
     *
     * @param x co-ord
     * @param y co-ord
     */
    public Node(float x, float y) {
        position = new Vector2(x, y);
        cost = -1;
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets position
     *
     * @param x co-ord
     * @param y co-ord
     */
    public void set(float x, float y) {
        position.set(x, y);
    }
}
