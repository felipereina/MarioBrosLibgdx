package com.felipereina.tutorial.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Scenes.Hud;
import com.felipereina.tutorial.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

        if(mario.marioIsBig()) {
            Gdx.app.log("Brick", "Collision");
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            //playing sound
            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();

    }
}
