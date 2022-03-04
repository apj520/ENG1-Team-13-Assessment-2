package com.mygdx.game.Physics;

/**
 * Allows for the callbacks during collision events
 */
public interface CollisionCallBack {
    /**
     * Called once a collision has being notices
     */
    void BeginContact(CollisionInfo info);

    /**
     * Called after the collision has being solved
     */
    void EndContact(CollisionInfo info);

    /**
     * Called on the object that enters the trigger
     */
    void EnterTrigger(CollisionInfo info);

    /**
     * Called upon exiting a trigger
     */
    void ExitTrigger(CollisionInfo info);
}
