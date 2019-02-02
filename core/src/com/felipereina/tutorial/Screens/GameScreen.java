package com.felipereina.tutorial.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.felipereina.tutorial.Drop;

import java.util.Iterator;

public class GameScreen implements Screen {

    final Drop game;

    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;
    private Rectangle bucket;
    private Array<Rectangle> rainDrops; // Array class is a libgdx class to be used instead of standard java collection
    private long lastDropTime;

    private int dropsGathered;

    //Constructor passing the Drop game object
    public GameScreen(final Drop game){
        this.game = game;

        //load images for the droplet and the bucket, 64X64 pixels each
        this.dropImage = new Texture(Gdx.files.internal("droplet.png"));
        this.bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        //load the drop sound effct and the rain background music
        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        this.rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        //start the playback of the background music immediately
        rainMusic.setLooping(true);

        //instantiate the camera
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800,480);

        //instantiate a Rectangle that will represent the Bucket space in the game world
        this.bucket = new Rectangle();
        bucket.x = (800 / 2) - (64 /2); // center the bucket horizontally
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        //instantiate the array of drops
        this.rainDrops = new Array<Rectangle>();
        spawnRaindrop(); //spawn our first raindrop



    }

    //auxiliary method to instantiate raindrops(rectangles) position it in a random position in the top of the screen
    //and add it to the raindrop array
    public void spawnRaindrop(){
        Rectangle rainDrop = new Rectangle();
        rainDrop.x = MathUtils.random(0,800 - 64);
        rainDrop.y = 480;
        rainDrop.width = 64;
        rainDrop.height = 64;
        rainDrops.add(rainDrop);
        lastDropTime = TimeUtils.nanoTime();

    }


    @Override
    public void render(float delta) {

        //clear the screen to blue
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update the camera in each frame
        camera.update();

        //Rendering our bucket
        game.batch.setProjectionMatrix(camera.combined); //tells the spritebatch to use the coordinate specified by the camera
        game.batch.begin(); //OpenGL wants to be told about as many images to render as possible at once

        //draw the font counting the number of drops collected
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);

        game.batch.draw(bucketImage,bucket.x,bucket.y);
        for(Rectangle rainDrop : rainDrops){ //render the raindrops
            game.batch.draw(dropImage, rainDrop.x, rainDrop.y);
        }
        game.batch.end();

        //moving the bucket throught the mouse (or touch in the smartphone screen)
        if(Gdx.input.isTouched()) { //first we verify if the screen is currently touched
            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0); // transform the mouse coordinate in coordinates from our game (Vector3)
            camera.unproject(touchPosition); // transform these coordinates to our camera coodinate system
            bucket.x = touchPosition.x - 64 / 2;
        }

        //moving the bucket by keyboard - 200 pixels per second (without acceleration)
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            bucket.x += 200 * Gdx.graphics.getDeltaTime(); // returns the time passed between the last and the actual frame
        }

        // stablishing limits right and left for the bucket
        if(bucket.x < 0){
            bucket.x = 0;
        }
        if(bucket.x > 800 - 64){
            bucket.x = 800 - 64;
        }

        //check how much time has passed since we spawned a raindrop. if its greater than some time, spawn another drop
        if(TimeUtils.nanoTime() - lastDropTime > 100000000) {
            spawnRaindrop();
        }

        //Make the raindrop move at a constant speed of 200pixels per second.
        // If the raindrop is beneath the botton of the screen, remove the raindrop from the Array
        Iterator<Rectangle> iter = rainDrops.iterator();

        while(iter.hasNext()) {
            Rectangle rainDrop = iter.next();
            rainDrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if(rainDrop.y + 64 < 0 ){
                iter.remove();
            }
            //If raindrop hits the bucket plays the drop sound
            if(rainDrop.overlaps(bucket)){
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        //starts playback of the background music when the screen is shown
        rainMusic.play();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
