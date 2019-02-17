package com.felipereina.tutorial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.felipereina.tutorial.Screens.PlayScreen;

public class MarioBros extends Game {

	//defines virtual width and virtual height of our game
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100; //pixels per meter

	public static final short ENEMY_WAKE_DISTANCE = 256;  //distance (in pixels) to wake up enemies

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	//public static final short MARIO_FOOT_BIT = 1024;


	public SpriteBatch batch;

	/*WARNING Using AssetManager in a static way can cause issues, especially on Android. Instead you may want to
	pass arount AssetManager to those classes that need it */
	public static AssetManager manager;


	@Override
	public void create(){
		batch = new SpriteBatch();
		manager = new AssetManager();
		//loading the assets
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);



		manager.finishLoading(); //assynchronous loading

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render(); // important!!

	}

	@Override
	public void dispose(){
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

}

