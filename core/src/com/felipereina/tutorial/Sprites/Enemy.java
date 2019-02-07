package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.felipereina.tutorial.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected TiledMap map;
    public Body b2body;
    protected PlayScreen screen;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        this.velocity = new Vector2(1, 0);
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead();


    public void reverseVelocity(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }
        if(y){
            velocity.y = -velocity.y;
        }
    }

}
