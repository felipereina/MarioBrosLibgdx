package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.felipereina.tutorial.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected TiledMap map;
    public Body b2body;
    protected PlayScreen screen;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void hitOnHead();

}
