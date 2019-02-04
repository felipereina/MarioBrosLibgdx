package com.felipereina.tutorial.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Scenes.Hud;

public class PlayScreen implements Screen {

    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort; // defines how the game screen will adapt to different devices screen sizes
    //the hud class is where we have the stage and table with bitmap font for score, level and life information.
    private Hud hud;


    private TmxMapLoader mapLoader; //responsible to load our map into the game
    private TiledMap map; // reference to the mao itself
    private OrthogonalTiledMapRenderer renderer; // what renders our map to the screen



    public PlayScreen(MarioBros game){
        this.game = game;
        this.gameCam = new OrthographicCamera();
        //FitViewport maintains the aspect ratio of the game and adds a black bar to the corners;
        this.gamePort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT,gameCam);
        this.hud = new Hud(game.batch);

        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("level1.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map);

        //we don't want that the camera to centralizes itself around the corner 0x0 (it would show only 1/4 of the screen).
        //so we centralize the camera by the ViewPort width and height divided by 2.
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    }



    //new method to do all the updating of our game world
    public void update(float deltaTime){

        //check if there is any inputs happening
        handleInput(deltaTime);
        gameCam.update();

        //let the map renderer now what it needs to render - Only render what our gameCam can see.
        renderer.setView(gameCam);

    }

    //if there is any input, gameCam position moves x to show the world
    public void handleInput(float deltaTime){

        if(Gdx.input.isTouched()){
            gameCam.position.x += 100 * deltaTime;
        }
    }

    @Override
    public void render(float delta) {
        //call the custom method update to check if there is any input happening
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width, height); // when screen resizes, uses the new width and height

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

    }
}
