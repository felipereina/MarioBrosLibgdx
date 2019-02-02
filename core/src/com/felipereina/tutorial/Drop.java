package com.felipereina.tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.felipereina.tutorial.Screens.MainMenuScreen;

import java.util.Iterator;

public class Drop extends Game {

	public SpriteBatch batch;
	public BitmapFont font;


	@Override
	public void create(){
		this.batch = new SpriteBatch();
		//Use LibGDX default Arial font
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render(); // important!!

	}

	@Override
	public void dispose(){
		batch.dispose();
		font.dispose();
	}

}

