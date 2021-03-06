package com.felipereina.tutorial.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Scenes.Hud;
import com.felipereina.tutorial.Sprites.Enemies.Enemy;
import com.felipereina.tutorial.Sprites.Enemies.Goomba;
import com.felipereina.tutorial.Sprites.Items.Item;
import com.felipereina.tutorial.Sprites.Items.ItemDefinition;
import com.felipereina.tutorial.Sprites.Items.Mushroom;
import com.felipereina.tutorial.Sprites.Mario;
import com.felipereina.tutorial.Tools.B2WorldCreator;
import com.felipereina.tutorial.Tools.WorldContactListener;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayScreen implements Screen {

    //Reference to our Game used to set Screens
    private MarioBros game;
    private TextureAtlas atlas;

    //Basic playscreen properties
    private OrthographicCamera gameCam;
    private Viewport gamePort; // defines how the game screen will adapt to different devices screen sizes
    //the hud class is where we have the stage and table with bitmap font for score, level and life information.
    private Hud hud;

    //Tiled Map Properties
    private TmxMapLoader mapLoader; //responsible to load our map into the game
    private TiledMap map; // reference to the mao itself
    private OrthogonalTiledMapRenderer renderer; // what renders our map to the screen

    //Box2d Properties
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Iterator<Goomba> goombaIterator;

    //Sprites properties
    private Mario player;

    //Game Music
    private Music music;

    //Items
    private Array<Item> items;
    private LinkedBlockingDeque<ItemDefinition> itemsToSpawn;

    public PlayScreen(MarioBros game){

        this.atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        this.gameCam = new OrthographicCamera();
        //FitViewport maintains the aspect ratio of the game and adds a black bar to the corners;
        this.gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM,gameCam);
        this.hud = new Hud(game.batch);

        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM); //second parameter is to scale to 1/100

        //we don't want that the camera to centralizes itself around the corner 0x0 (it would show only 1/4 of the screen).
        //so we centralize the camera by the ViewPort width and height divided by 2.
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //box2d instantiation
        this.world = new World(new Vector2(0,-10), true); //1st parm to set gravity, 2nd to set sleep bodies
        this.b2dr = new Box2DDebugRenderer(); // allows debug lines in our box2d world

        //Instantiate B2WorldCreator class
        creator = new  B2WorldCreator(this);

        //Instantiate Mario
        this.player = new Mario(this);

        //contact listener is what get called when 2 fixtures in Box2d colide with each other
        world.setContactListener(new WorldContactListener());

        //get the music
        this.music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true); //looping the music
        music.play();

        //instantiate items
        this.items = new Array<Item>();
        this.itemsToSpawn = new LinkedBlockingDeque<ItemDefinition>();

    }

    //method to spawn items
    public void spawnItem(ItemDefinition idef){
        itemsToSpawn.add(idef);
    }

    //if there is a item in the coin and it is a mushroom, then instantiate a mushroom and set position as the coin
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDefinition idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    //Custom method to return Atlas
    public TextureAtlas getAtlas(){
        return this.atlas;
    }

    //new method to do all the updating of our game world
    public void update(float deltaTime){

        //check if there is any inputs happening
        handleInput(deltaTime);

        //update the items situation
        handleSpawningItems();

        world.step(1/60f, 6, 2);

        //update Mario sprite Position according to the body
        player.update(deltaTime);

        //Goombas sprite Position update
        //Created a Iterator for Goomba Array in order to properly destroy Goombas from the Array
       /* goombaIterator = creator.getGoombaIterator();
        while(goombaIterator.hasNext()){

            Goomba nextGoomba = goombaIterator.next();
            nextGoomba.update(deltaTime);
            if(nextGoomba.getX() < player.getX()+(MarioBros.ENEMY_WAKE_DISTANCE/MarioBros.PPM)) {
                nextGoomba.b2body.setActive((true));
            }
            if (nextGoomba.isSetToDestroy() && !nextGoomba.isDestroyed()){
                world.destroyBody(nextGoomba.b2body);
                nextGoomba.setDestroyed(true);
            }

            if (nextGoomba.getStateTime() >= 1 && nextGoomba.isDestroyed()){
                Gdx.app.log("removing goomba from array", "");
                goombaIterator.remove();
            }
        } */

        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(deltaTime);
            if(enemy.getX() < player.getX() + MarioBros.ENEMY_WAKE_DISTANCE / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        //update each item every frame
        for(Item item : items){
            item.update(deltaTime);
        }



        //update the timer from the Hud
        hud.update(deltaTime);

        //Always center the Camera in Mario position on the screen
        if(player.currentState != Mario.State.DEAD) {
            gameCam.position.x = player.b2body.getPosition().x;
        }
        gameCam.update();

        //let the map renderer now what it needs to render - Only render what our gameCam can see.
        renderer.setView(gameCam);

    }

    //if there is any input, gameCam position moves x to show the world
    public void handleInput(float deltaTime){
        //only allows inputs if Mario is alive
        if(player.currentState != Mario.State.DEAD) {
            // -- Input Handler to capture up button (Jump) --
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                //linear Impulse (dont have acceleration) : param 1st - direction and force; 2nd where in the body the force will be applied; 3trd is to awake the body.
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }

            // -- Input Handler to capture Right and Left buttons (Move) --
            // isKeyPressed != isJustKeyPressed - the first sees if the button is being hold down
            // second condition checks if Mario is not moving faster than a specifc speed.
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void render(float delta) {
        //call the custom method update to check if there is any input happening
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render our game Map
        renderer.render();

        //Render Mario
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        //Render Enemies
        for(Enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
        }

        //Render the Items
        for(Item item : items){
            item.draw(game.batch);
        }

        game.batch.end();

        //renderer out Box2dDebugLines (green box2d lines)
        b2dr.render(world,gameCam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width, height); // when screen resizes, uses the new width and height

    }


    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
