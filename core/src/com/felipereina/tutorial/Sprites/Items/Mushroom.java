package com.felipereina.tutorial.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;
import com.felipereina.tutorial.Sprites.Mario;

public class Mushroom extends Item {


    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        setRegion(screen.getAtlas().findRegion("mushroom"), 0,0,16,16);
        velocity = new Vector2(0.7f,0); //mushrooms will not gonna move.
    }

    @Override
    public void defineItem() {
        // -- Goomba Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY()); //defines the position of the body in the screen
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        //--Goomba Fixture --
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.shape = shape;
        //identifyer
        fixtureDef.filter.categoryBits = MarioBros.ITEM_BIT;
        //what mushroom can colide with
        fixtureDef.filter.maskBits = MarioBros.MARIO_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        destroy(); //destroy mushroom after use
        mario.grow();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
            //setting the position of the texture region to the center of our Box2d body.
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            velocity.y = body.getLinearVelocity().y;
            body.setLinearVelocity(velocity);
    }

    @Override
    public void reverseVelocity(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }
        if(y){
            velocity.y = -velocity.y;
        }
    }
}
