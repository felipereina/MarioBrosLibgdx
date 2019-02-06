package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;

public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    //animations
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;

    private float stateTimer;

    private boolean runningRight;

    public Mario(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        //Running animation of Mario
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i*16 + 1, 11, 16, 16));
            marioRun = new Animation<TextureRegion>(0.1f,frames);
        }
        frames.clear();

        //Jump animation of Mario
        for(int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(), i*16 + 1, 11, 16, 16));
            marioJump = new Animation<TextureRegion>(0.1f,frames);
        }
        frames.clear();

        marioStand = new TextureRegion(getTexture(), 1, 11, 16, 16);

        defineMario();
        setBounds(0,0,16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);

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
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.shape = shape;

        //give a identity BIT to the fixture and define with wich fitures it can collide with
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT;

        b2body.createFixture(fixtureDef);

        //--Fixture to the feet of Mario (to walk smoothly through the boxes) --
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioBros.PPM, -6/MarioBros.PPM), new Vector2(2/MarioBros.PPM, -6/MarioBros.PPM));
        fixtureDef.shape = feet;
        b2body.createFixture(fixtureDef);

        //-- Fixture for Mario head --
        EdgeShape head = new EdgeShape(); //Edge Shape is a line between 2 diferents points.
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; //when its a sensor, it does not collide with anything, it just capture information.

        b2body.createFixture(fixtureDef).setUserData("head");
    }

    //update Mario sprite Position according to the body
    public void update(float deltaTime){
        setPosition(b2body.getPosition().x - (getWidth() /2), b2body.getPosition().y - (getHeight() /2));
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioStand;
                    break;
        }

        //turn running animation left when pressing left button
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if( (b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState
                ? stateTimer + deltaTime
                : 0;

        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        } else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        } else if(b2body.getLinearVelocity(). x != 0){
            return State.RUNNING;
        } else{
            return State.STANDING;
        }
    }
}
