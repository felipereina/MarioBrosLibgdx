package com.felipereina.tutorial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.felipereina.tutorial.Screens.PlayScreen;

public class MarioBros extends Game {

	//defines virtual width and virtual height of our game
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100; //pixels per meter


	public SpriteBatch batch;

	@Override
	public void create(){
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render(); // important!!

	}

	@Override
	public void dispose(){

	}

}

