package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;
import com.felipereina.tutorial.Sprites.Enemies.Enemy;
import com.felipereina.tutorial.Sprites.Enemies.Turtle;

public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    //animations
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;

    //Big Mario
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;

    private float stateTimer;

    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;

    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        //Running animation of Mario
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
            marioRun = new Animation<TextureRegion>(0.1f,frames);
        }
        frames.clear();

        //Big mario running animation
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
            bigMarioRun = new Animation<TextureRegion>(0.1f,frames);
        }

        //get animation set for Mario grow - repeting medium size big size mario sprite
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation<TextureRegion>(0.2f, frames);


        //clear frames for next animation sequence
        frames.clear();

        //Jump animation of Mario
        for(int i = 4; i < 6; i++){
            marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
            bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        }
        //clear frames for next animation sequence
        frames.clear();

        //create texture region for mario stand
        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);

        //create texture region for Big mario stand
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);

        //create texture region for dead mario
        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96,0,16,16);

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
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        b2body.createFixture(fixtureDef).setUserData(this);

        //--Fixture to the feet of Mario (to walk smoothly through the boxes) --
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioBros.PPM, -6/MarioBros.PPM), new Vector2(2/MarioBros.PPM, -6/MarioBros.PPM));
        fixtureDef.shape = feet;
        b2body.createFixture(fixtureDef);

        //-- Fixture for Mario head --
        EdgeShape head = new EdgeShape(); //Edge Shape is a line between 2 diferents points.
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; //when its a sensor, it does not collide with anything, it just capture information.

        b2body.createFixture(fixtureDef).setUserData(this);
    }

    public void defineBigMario(){

        //save the actual position of Mario
        Vector2 currentPosition = b2body.getPosition();
        //destroy little mario body to recreate big Mario
        world.destroyBody(b2body);


        // -- Mario Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0,10/MarioBros.PPM)); //equals to previous position and bring Mario up
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //--Mario Fixture --
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.shape = shape;

        //give a identity BIT to the fixture and define with wich fitures it can collide with
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        b2body.createFixture(fixtureDef).setUserData(this);

        //add another circle in the bottom of big Mario body 14 basic units
        shape.setPosition(new Vector2(0, -14 /MarioBros.PPM));
        b2body.createFixture(fixtureDef).setUserData(this);


        //--Fixture to the feet of Mario (to walk smoothly through the boxes) --
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioBros.PPM, -6/MarioBros.PPM), new Vector2(2/MarioBros.PPM, -6/MarioBros.PPM));
        fixtureDef.shape = feet;
        b2body.createFixture(fixtureDef);

        //-- Fixture for Mario head --
        EdgeShape head = new EdgeShape(); //Edge Shape is a line between 2 diferents points.
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; //when its a sensor, it does not collide with anything, it just capture information.

        b2body.createFixture(fixtureDef).setUserData(this);


        timeToDefineBigMario = false;
    }

    //update Mario sprite Position according to the body
    public void update(float deltaTime){

        //update the sprite to correspond with the position of the box2D body
        if(marioIsBig){
            setPosition(b2body.getPosition().x - (getWidth() /2), b2body.getPosition().y - (getHeight() /2) - 6/MarioBros.PPM);
        } else {
            setPosition(b2body.getPosition().x - (getWidth() / 2), b2body.getPosition().y - (getHeight() / 2));
        }

        setRegion(getFrame(deltaTime));

        if(timeToDefineBigMario){
            defineBigMario();
        }

        if(timeToRedefineMario){
            redefineMario();
        }
    }

    public TextureRegion getFrame(float deltaTime){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioIsBig? bigMarioStand : marioStand;
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

        if(marioIsDead){
            return State.DEAD;
        } else if(runGrowAnimation){
            return State.GROWING;
        } else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        } else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        } else if(b2body.getLinearVelocity(). x != 0){
            return State.RUNNING;
        } else{
            return State.STANDING;
        }
    }

    public void grow(){
        if(!marioIsBig) {
            runGrowAnimation = true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2); //bigger bounds for big Mario
            MarioBros.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }

    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public boolean marioIsBig() {
        return marioIsBig;
    }

    public void hit(Enemy enemy){

        if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
            ((Turtle) enemy).kick(enemy.b2body.getPosition().x > b2body.getPosition().x ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        } else {

            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2); // bounds of MArio shrinks again
                MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
                MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = MarioBros.NOTHING_BIT;
                //setting the nothing filter to all Mario's fixture in order to disable collisions ability
                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }

    }

    public void redefineMario(){

        //redefine mario in the same location he is
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body); // destroy big mario

        // -- Mario Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //--Mario Fixture --
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.shape = shape;

        //give a identity BIT to the fixture and define with wich fitures it can collide with
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        b2body.createFixture(fixtureDef).setUserData(this);

        /*
        //--Fixture to the feet of Mario (to walk smoothly through the boxes) --
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / MarioBros.PPM, -6/MarioBros.PPM), new Vector2(2/MarioBros.PPM, -6/MarioBros.PPM));
        fixtureDef.filter.categoryBits = MarioBros.MARIO_FOOT_BIT;
        fixtureDef.shape = feet;
        b2body.createFixture(fixtureDef).setUserData(this); */

        //-- Fixture for Mario head --
        EdgeShape head = new EdgeShape(); //Edge Shape is a line between 2 diferents points.
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fixtureDef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; //when its a sensor, it does not collide with anything, it just capture information.

        b2body.createFixture(fixtureDef).setUserData(this);

        timeToRedefineMario = false;
    }
}
