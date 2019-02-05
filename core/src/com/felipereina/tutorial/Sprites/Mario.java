package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;

public class Mario extends Sprite {

    public World world;
    public Body b2body;

    public Mario(World world){
        this.world = world;
        defineMario();
    }

    public void defineMario(){
        // -- Mario Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioBros.PPM,32 / MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //--Mario Fixture --
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioBros.PPM);
        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef);

    }

}
