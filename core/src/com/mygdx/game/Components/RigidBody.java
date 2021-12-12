package com.mygdx.game.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.PhysicsBodyType;

public class RigidBody extends Component implements CollisionCallBack {
    int bodyId;
    PhysicsBodyType bodyType;
    Vector2 halfDim;
    public RigidBody() {
        super();
        type = ComponentType.RigidBody;
        halfDim = new Vector2();
    }

    public RigidBody(PhysicsBodyType type, Renderable r, Transform t){
        this();
        bodyType = type;
        BodyDef def = new BodyDef();
        switch (type){
            case Static:
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case Dynamic:
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case Kinematic:
                def.type = BodyDef.BodyType.KinematicBody;
                break;
        }
        float h_x = r.sprite.getWidth() * 0.5f;
        float h_y = r.sprite.getHeight() * 0.5f;
        halfDim.set(h_x, h_y);

        def.position.set(t.getPosition().x + h_x, t.getPosition().y + h_y);
        def.angle = t.getRotation();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(h_x, h_y);

        FixtureDef f = new FixtureDef();
        f.shape = shape;
        f.density = type == PhysicsBodyType.Static ? 0.0f : 1.0f;
        f.restitution = 1;

        bodyId = PhysicsManager.createBody(def, f, this);

        shape.dispose();
    }

    public void setVelocity(Vector2 vel){
        PhysicsManager.getBody(bodyId).setLinearVelocity(vel);
    }

    @Override
    public void update() {
        super.update();
        // parent.getComponent(Transform.class).setPosition(PhysicsManager.getBody(bodyId).getPosition());
        Transform t = parent.getComponent(Transform.class);
        Body b = PhysicsManager.getBody(bodyId);
        Vector2 p = b.getPosition().cpy();
        p.sub(halfDim);
        t.setPosition(p);
    }

    @Override
    public void BeginContact() {

    }

    @Override
    public void EndContact() {

    }
}