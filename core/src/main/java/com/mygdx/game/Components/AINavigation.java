package com.mygdx.game.Components;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.Utilities;

/**
 * Used to control NPCs with steerable for movement and state machines for behaviour
 */
public class AINavigation extends Component implements Steerable<Vector2> {
    /**
     * NPC settings for steerable
     */
    private static class Attributes {
        public float boundingRadius = 128;
        public float maxSpd = GameManager.getSettings().get("AI").getFloat("maxSpeed");
        public float maxAcc = 50000;
        public float maxAngSpd = 0;
        public float maxAngAcc = 0;
        public boolean isTagged = false;
    }

    RigidBody rb;
    Transform t;
    Attributes attributes;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;

    public AINavigation() {
        super();
        attributes = new Attributes();
        setRequirements(ComponentType.RigidBody);
        type = ComponentType.AINavigation;
        steeringOutput = new SteeringAcceleration<>(new Vector2());
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }

    /**
     * Gets the components if != null
     */
    private void getComps() {
        if (rb == null) {
            rb = parent.getComponent(RigidBody.class);
            t = parent.getComponent(Transform.class);
        }
    }

    /**
     * Called once per frame. Apply the steering behaviour and sets the ship direction, so it faces the right way
     */
    @Override
    public void update() {
        super.update();
        getComps();
        if (behavior != null) {
            behavior.calculateSteering(steeringOutput);
            applySteering();
        } else {
            stop();
        }

        Vector2 vel = rb.getVelocity().cpy();
        if (vel.x == 0 && vel.y == 0) {
            ((Ship) parent).setShipDirection("-up");
            return;
        }
        vel.nor();
        Utilities.round(vel);

        if (Ship.shipDirections.containsKey(vel)) {
            ((Ship) parent).setShipDirection(vel);
        }
    }

    /**
     * Calculates the forces required by the steering behaviour (no rotation)
     */
    private void applySteering() {
        boolean anyAcc = false;
        if (!steeringOutput.linear.isZero()) {
            Vector2 f = steeringOutput.linear;
            rb.applyForce(f);
            anyAcc = true;
        }

        if (anyAcc) {
            Vector2 vel = rb.getVelocity();
            float speed = vel.len2();
            if (speed > attributes.maxSpd * attributes.maxSpd) {
                rb.setVelocity(vel.scl(attributes.maxSpd / (float) Math.sqrt(speed)));
            }
        }
    }

    /**
     * Stops all motion
     */
    public void stop() {
        getComps();
        rb.setVelocity(new Vector2(0, 0));
    }

    // Overrides for steerable

    @Override
    public Vector2 getLinearVelocity() {
        getComps();
        return rb.getVelocity();
    }

    @Override
    public float getAngularVelocity() {
        getComps();
        return rb.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return attributes.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return attributes.isTagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        attributes.isTagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.01f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return attributes.maxSpd;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        attributes.maxSpd = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return attributes.maxAcc;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        attributes.maxAcc = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return attributes.maxAngSpd;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        attributes.maxAngSpd = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return attributes.maxAngAcc;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        attributes.maxAngAcc = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        getComps();
        return t.getPosition();
    }

    @Override
    public float getOrientation() {
        getComps();
        return t.getRotation();
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utilities.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utilities.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        getComps();
        return t;
    }
}
