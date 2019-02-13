package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Scenes.Hud;
import com.felipereina.tutorial.Screens.PlayScreen;
import com.felipereina.tutorial.Sprites.Items.ItemDefinition;
import com.felipereina.tutorial.Sprites.Items.Mushroom;

public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);

        //Changing the graphic of the Tile when Mario hits the Coin
        this.tileSet = map.getTileSets().getTileSet("tileset_gutter");

        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }
    @Override
    public void onHeadHit() {

        Gdx.app.log("Coin", "Collision");

        //dealing with different sounds in Coin lifecicle
        if(getCell().getTile().getId() == BLANK_COIN){
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else{
            //give a item to Mario
            if(object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDefinition(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM), Mushroom.class));
                MarioBros.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            } else {
                MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
        }

        //changing the Tile
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);



    }
}

