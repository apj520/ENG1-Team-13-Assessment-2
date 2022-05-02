package com.mygdx.game.Entitys;

import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;

/**
 * Player's ship entity.
 */
public class Player extends Ship {

    /**
     * Adds ship with PlayerController component and sets its speed.
     *
     * @param speed of movement across map
     */
    private Player(float speed) {
        super();

        PlayerController pc = new PlayerController(this, speed);
        addComponent(pc);

        setName("Player");
    }

    /**
     * Adds ship with PlayerController component, loading its speed from GameManager settings.
     */
    public Player() {
        this(GameManager.getSettings().get("starting").getFloat("playerSpeed"));
    }

    //Roscoe - added method to update speed of player ship, added method to restore original speed
    /**
     * Updates the speed of the player ship by multiplying with a scalar
     *
     * @param scalar scale factor for updating player speed
     */
    public void updateSpeed(float scalar) {
        PlayerController pc = getComponent(PlayerController.class);
        float updatedSpeed = getComponent(PlayerController.class).getSpeed() * scalar;
        pc.setSpeed(updatedSpeed);
    }

    public void restoreSpeed() {
        PlayerController pc = getComponent(PlayerController.class);
        pc.setSpeed(GameManager.getSettings().get("starting").getFloat("playerSpeed"));
    }

    //Roscoe - added cannonBall affects
    public void cannonBallAffect(CollisionInfo info) {
        getComponent(Pirate.class).takeDamage(((CannonBall) info.a).getDamage());
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    public int getAmmo() {
        return getComponent(Pirate.class).getAmmo();
    }
}
