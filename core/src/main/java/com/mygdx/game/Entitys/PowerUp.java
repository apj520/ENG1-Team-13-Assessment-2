package com.mygdx.game.Entitys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;

public class PowerUp extends Entity implements CollisionCallBack {

    private String powerUpName;
    private static int atlas_id;
    private boolean isFlag;

    public PowerUp() {
        super(2);
        Transform t = new Transform();
        isFlag = false;
        atlas_id = ResourceManager.getId("powerups.txt");
        Renderable r = new Renderable(atlas_id, "doublepoints", RenderLayer.Transparent);
        addComponents(t,r);
    }

    /**
     * Creates a power-up with the given name at the specified location.
     *
     * @param pos  2D position vector
     * @param name name of power-up
     */
    public void create(Vector2 pos, String name) {
        Sprite s = ResourceManager.getSprite(atlas_id, name);
        Renderable r = getComponent(Renderable.class);
        r.setTexture(s);
        getComponent(Transform.class).setPosition(pos);
        powerUpName = name;

        RigidBody rb = new RigidBody(PhysicsBodyType.Static, r, getComponent(Transform.class));
        rb.setCallback(this);
        addComponent(rb);

    }

    /**
     * Removes the power-up offscreen once touched, call only when collision with player ship.
     */
    private void removeOnCollision() {
        if (isFlag) {
            getComponent(Renderable.class).hide();
            Transform t = getComponent(Transform.class);
            t.setPosition(10000, 10000);

            RigidBody rb = getComponent(RigidBody.class);
            rb.setPosition(t.getPosition());

            isFlag = false;
        }
    }

    public void kill() {
        isFlag = true;
    }

    @Override
    public void update() {
        super.update();
        removeOnCollision();
    }

    @Override
    public void BeginContact(CollisionInfo info) {

    }

    @Override
    public void EndContact(CollisionInfo info) {

    }

    @Override
    public void EnterTrigger(CollisionInfo info) {

    }

    @Override
    public void ExitTrigger(CollisionInfo info) {

    }
}
