package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
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

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
        String logging = " " + filterBit;
        Gdx.app.log("filterBit", logging);

    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / 16), (int) (body.getPosition().y * MarioBros.PPM / 16));
    }
}
