package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;

public class Goomba extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y){
        super(screen, x, y);

        //walking animation for Goombas
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
            walkAnimation = new Animation <TextureRegion>(0.4f, frames);
            stateTime = 0;
        }
        setBounds(getX(), getY(), 16 /MarioBros.PPM, 16 /MarioBros.PPM);
    }

    @Override
    protected void defineEnemy() {
        // -- Goomba Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(64 / MarioBros.PPM,64 / MarioBros.PPM); //defines the position of the body in the screen
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //--Goomba Fixture --
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.shape = shape;

        //give a identity BIT to the fixture and define with wich fixtures it can collide with
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.MARIO_BIT;

        b2body.createFixture(fixtureDef);
    }

    public void update(float deltaTime){
        stateTime += deltaTime;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));//true to say its a looping animation
    }

}
