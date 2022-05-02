package com.mygdx.game.Entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.EnemyState;
import com.mygdx.game.Components.*;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;

import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

import static com.mygdx.utils.Constants.HALF_DIMENSIONS;

/**
 * NPC ship entity class.
 */
public class NPCShip extends Ship implements CollisionCallBack {
    public StateMachine<NPCShip, EnemyState> stateMachine;
    private static JsonValue AISettings;
    private final QueueFIFO<Vector2> path;

    //Roscoe - added fire rate timer, initialised to 1 so can fire immediately
    private float fireTimer = 1;

    /**
     * Creates an initial state machine
     */
    public NPCShip() {
        super();
        path = new QueueFIFO<>();

        if (AISettings == null) {
            AISettings = GameManager.getSettings().get("AI");
        }

        stateMachine = new DefaultStateMachine<>(this, EnemyState.WANDER);

        setName("NPC");
        AINavigation nav = new AINavigation();

        addComponent(nav);


        RigidBody rb = getComponent(RigidBody.class);
        // rb.setCallback(this);

        JsonValue starting = GameManager.getSettings().get("starting");

        // agro trigger
        rb.addTrigger(Utilities.tilesToDistance(starting.getFloat("argoRange_tiles")), "agro");
    }

    /**
     * gets the top of targets from pirate component
     *
     * @return the top target
     */
    private Ship getTarget() {
        return getComponent(Pirate.class).getTarget();
    }

    /**
     * updates the state machine
     */
    @Override
    public void update() {
        super.update();
        stateMachine.update();
        //Roscoe - added check for if npcship is alive, kills if true, and adds time to fire rate timer
        if (!isAlive()) {
            dead();
        }
        fireTimer += Gdx.graphics.getDeltaTime();


        // System.out.println(getComponent(Pirate.class).targetCount());
    }

    /**
     * is meant to path find to the target but didn't work
     */
    public void goToTarget() {
        /*path = GameManager.getPath(
                Utilities.distanceToTiles(getPosition()),
                Utilities.distanceToTiles(getTarget().getPosition()));*/
    }

    /**
     * creates a new steering behaviour that will make the NPC beeline for the target doesn't factor in obstetrical
     */
    public void followTarget() {
        if (getTarget() == null) {
            stopMovement();
            return;
        }
        AINavigation nav = getComponent(AINavigation.class);

        Arrive<Vector2> arrives = new Arrive<>(nav,
                getTarget().getComponent(Transform.class))
                .setTimeToTarget(AISettings.getFloat("accelerationTime"))
                .setArrivalTolerance(AISettings.getFloat("arrivalTolerance"))
                .setDecelerationRadius(AISettings.getFloat("slowRadius"));

        nav.setBehavior(arrives);
    }

    /**
     * stops all movement and sets the behaviour to null
     */
    public void stopMovement() {
        AINavigation nav = getComponent(AINavigation.class);
        nav.setBehavior(null);
        nav.stop();
    }

    /**
     * Meant to cause the npc to wander
     */
    public void wander() {

    }

    //Roscoe - affect associated with npcship becoming ally
    public void dead() {
        if (getComponent(Pirate.class).getFaction().id != 1) {
            //sets faction variable in pirate class to desired faction
            setFaction(1);

            //set health of new npcship ally to 50
            getComponent(Pirate.class).setHealth(50);

            //sets the faction variables in faction class to desired values
            //getComponent(Faction.class).changeFaction(getComponent(Faction.class).getName(), getComponent(Faction.class).getColour(), getComponent(Faction.class).getId());

        } else if (getComponent(Pirate.class).getFaction().id == 1) {
            removeOnDeath();
        }
    }

    //Roscoe -- added remove method
    public void removeOnDeath() {
            getComponent(Renderable.class).hide();
            Transform t = getComponent(Transform.class);
            t.setPosition(10000, 10000);

            RigidBody rb = getComponent(RigidBody.class);
            rb.setPosition(t.getPosition());
            rb.setVelocity(0, 0);

            //Sets the ship back to alive to avoid the above being called more than once
            //getComponent(Pirate.class).resetHealth();
            //TODO - can't remove dead ship from other ships target list, crashes
            removeFromTargets();
    }

    //Roscoe - remove from other ships target list
    public void removeFromTargets() {
        for (Ship ship : GameManager.getShipsList()) {
            Pirate p = ship.getComponent(Pirate.class);
            for (Ship targ : p.getTargets()) {
                if (targ == this) {
                    p.getTargets().remove(targ);
                    break;
                }
            }
        }
    }

    //Roscoe - added damage method
    public void cannonBallAffect(CollisionInfo info) {
        //npcship takes x2 damage than the player
        getComponent(Pirate.class).takeDamage(((CannonBall) info.a).getDamage() * 2);
        GameManager.getPlayer().getComponent(Pirate.class).addPlunder(50);
    }

    //Roscoe - added fire rate method
    private boolean setCanFire() {
        if (fireTimer >= 1f) {
            return true;
        } else {
            return false;
        }
    }

    //Roscoe - override shoot so npcship fires in direction of first element of target list
    @Override
    public void shoot() {
        if (setCanFire() && !(getComponent(Pirate.class).getTarget().getComponent(Pirate.class).getFaction().equals(getComponent(Pirate.class).getFaction()))) {
            Vector2 delta = getComponent(Pirate.class).getTarget().getPosition();
            delta.sub(getComponent(Transform.class).getPosition());
            delta.nor();
            //delta.y *= -1;

            getComponent(Pirate.class).shoot(delta);
            fireTimer = 0;
        }
    }

    @Override
    public void BeginContact(CollisionInfo info) {

    }

    @Override
    public void EndContact(CollisionInfo info) {

    }

    //Roscoe - added contact with npcship and player/npcship cannonball
    /**
     * if the agro fixture hit a ship set it as the target
     *
     * @param info the collision info
     */
    @Override
    public void EnterTrigger(CollisionInfo info) {
        if (!(info.a instanceof Ship)) {
            if (info.a instanceof CannonBall) {
                if (!getComponent(Pirate.class).getFaction().equals(((CannonBall) info.a).getShooter().getComponent(Pirate.class).getFaction())) {
                    cannonBallAffect(info);
                    ((CannonBall) info.a).kill();
                } else {
                    //insert friendly fire stuff if needed
                }
            }
            return;
        }
        Ship other = (Ship) info.a;
        if (other instanceof Player) {
            Pirate pirate = getComponent(Pirate.class);
            pirate.addTarget(other);
        } else if (info.b instanceof NPCShip && (!Objects.equals(info.b.getComponent(Pirate.class).getFaction().getName(), getComponent(Pirate.class).getFaction().getName()))) {
            Pirate pirate = getComponent(Pirate.class);
            pirate.addTarget((Ship) info.b);
        } return;
    }

    /**
     * if a taget has left remove it from the potential targets Queue
     *
     * @param info collision info
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {
        if (!(info.a instanceof Ship)) {
            return;
        }
        Pirate pirate = getComponent(Pirate.class);
        Ship o = (Ship) info.a;
        // remove the object from the targets list
        for (Ship targ : pirate.getTargets()) {
            if (targ == o) {
                pirate.getTargets().remove(targ);
                break;
            }
        }
    }
}
