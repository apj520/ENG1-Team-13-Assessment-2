package com.mygdx.game.Physics;

public enum PhysicsBodyType {
    /**
     * Has infinite mass doesn't move at all
     */
    Static,
    /**
     * Effected by all forces (like every thing in real life)
     */
    Dynamic,
    /**
     * Not effected by forces but can move
     */
    Kinematic
}