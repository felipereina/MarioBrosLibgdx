package com.felipereina.tutorial;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Drop extends ApplicationAdapter {

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;

	private Vector3 touchPosition;

	@Override
	public void create(){
		//load images for the droplet and the bucket, 64X64 pixels each
		this.dropImage = new Texture(Gdx.files.internal("droplet.png"));
		this.bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		//load the drop sound effct and the rain background music
		this.dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		this.rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		//start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		//instantiate the camera
		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, 800,480);

		//instantiate the Sprite Batch
		this.batch = new SpriteBatch();

		//instantiate a Rectangle that will represent the Bucket space in the game world
		this.bucket = new Rectangle();
		bucket.x = (800 / 2) - (64 /2); // center the bucket horizontally
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		//instantiate the Vector3
		this.touchPosition = new Vector3();
	}

	@Override
	public void render () {
		//clear the screen to blue
		Gdx.gl.glClearColor(0,0,0.2f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//update the camera in each frame
		camera.update();

		//Rendering our bucket
		batch.setProjectionMatrix(camera.combined); //tells the spritebatch to use the coordinate specified by the camera
		batch.begin(); //OpenGL wants to be told about as many images to render as possible at once
		batch.draw(bucketImage,bucket.x,bucket.y);
		batch.end();

		//moving the bucket
		if(Gdx.input.isTouched()){ //first we verify if the screen is currently touched
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(),0); // transform the mouse coordinate in coordinates from our game (Vector3)
			camera.unproject(touchPosition); // transform these coordinates to our camera coodinate system
			bucket.x = touchPosition.x - 64/2;
		}
	}
}
