package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() /2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight() /2) / MarioBros.PPM);
        body = world.createBody(bodyDef); //add the body to the world.

        polygonShape.setAsBox((bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getHeight() /2) / MarioBros.PPM);
        fixtureDef.shape = polygonShape; //defining the shape of the fixture.
        fixture = body.createFixture(fixtureDef); //add the fixture to the body.
    }

    public abstract void onHeadHit();

}
