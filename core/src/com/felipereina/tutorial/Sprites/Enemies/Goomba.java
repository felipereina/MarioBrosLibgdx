package com.felipereina.tutorial.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;

public class Goomba extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

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

        this.setToDestroy = false;
        this.destroyed = false;
    }

    @Override
    protected void defineEnemy() {
        // -- Goomba Body --
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY()); //defines the position of the body in the screen
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

        b2body.createFixture(fixtureDef).setUserData(this);

        //Creating the Head of Goomba
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(1/MarioBros.PPM);
        vertice[1] = new Vector2(5,8).scl(1/MarioBros.PPM);
        vertice[2] = new Vector2(-3,3).scl(1/MarioBros.PPM);
        vertice[3] = new Vector2(3,3).scl(1/MarioBros.PPM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f; //how much bouncing is the fixture (for Mario to bouncing up after stomping a goomba)
        fixtureDef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();

    }

    public void update(float deltaTime){
        stateTime += deltaTime;

        if(setToDestroy && !destroyed){
            //change the sprite texture region to smashed Goomba
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            stateTime = 0;

        } else if(!destroyed) {
            b2body.setLinearVelocity(velocity); //receceiving new Vector2 from Enemy superclass
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));//true to say its a looping animation
        }

    }
    //Making Goomba desapear after 1 second by overriding the draw method.
    public void draw(Batch batch){
        //Goomba sprite is only draw if goomba is not destroyed or the time is less than 1 second
        if(!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }

    public boolean isSetToDestroy() {
        return setToDestroy;
    }

    public void setSetToDestroy(boolean setToDestroy) {
        this.setToDestroy = setToDestroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
}
