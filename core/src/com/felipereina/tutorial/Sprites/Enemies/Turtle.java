package com.felipereina.tutorial.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;
import com.felipereina.tutorial.Sprites.Mario;

public class Turtle extends Enemy {

    public static final int KICK_LEFT_SPEED = -2;
    public static final int KICK_RIGHT_SPEED = 2;

    public enum State{WALKING, STANDING_SHELL, MOVING_SHELL};
    public State currentState;
    public State previousState;

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private boolean setToDestroy;
    private boolean destroyed;


    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0,0,16,24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16,0,16,24));
        this.shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64,0,16,16);

        walkAnimation = new Animation<TextureRegion>(0.2f, frames);
        currentState = previousState = State.WALKING;

        setBounds(getX(),getY(),16/ MarioBros.PPM, 24/MarioBros.PPM);
    }


    @Override
    protected void defineEnemy() {
        // -- Turtle Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY()); //defines the position of the body in the screen
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //--Turtle Fixture --
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

        b2body.createFixture(fixtureDef).setUserData(this);

        //Creating the Head of Turtle
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(1/MarioBros.PPM);
        vertice[1] = new Vector2(5,8).scl(1/MarioBros.PPM);
        vertice[2] = new Vector2(-3,3).scl(1/MarioBros.PPM);
        vertice[3] = new Vector2(3,3).scl(1/MarioBros.PPM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 1.5f; //how much bouncing is the fixture (for Mario to bouncing up after stomping a turtle)
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead(Mario mario) {
/*
        if(currentState != State.STANDING_SHELL){
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        } else{
            //if mario kicks on the left of the shell, shell goes right and vice-versa
            kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
        } */

        if(currentState == State.STANDING_SHELL) {
            if(mario.b2body.getPosition().x > b2body.getPosition().x) {
                velocity.x = KICK_LEFT_SPEED;
            }else{
                velocity.x = KICK_RIGHT_SPEED;
            }
            currentState = State.MOVING_SHELL;
        }
        else {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }

    }

    public void kick(int speed){
        velocity.x = speed;
        currentState = State.MOVING_SHELL;

    }

    public TextureRegion getFrame(float deltaTime) {
        TextureRegion region;

        switch(currentState) {
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime,  true);
                break;
        }

        if(velocity.x > 0 && region.isFlipX() == false) {
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX() == true) {
            region.flip(true, false);
        }

        stateTime = currentState == previousState
                ? stateTime + deltaTime
                : 0;

        previousState = currentState;
        return region;
    }

    @Override
    public void update(float deltaTime) {
        setRegion(getFrame(deltaTime));
        if(currentState == State.STANDING_SHELL && stateTime > 5){
            currentState = State.WALKING;
            velocity.x = 1;
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 /MarioBros.PPM);
        b2body.setLinearVelocity(velocity);
    }

    public State getCurrentState(){
        return currentState;
    }

}
