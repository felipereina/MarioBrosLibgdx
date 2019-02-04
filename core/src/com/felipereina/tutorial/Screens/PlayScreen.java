package com.felipereina.tutorial.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Scenes.Hud;
import com.felipereina.tutorial.Sprites.Mario;

public class PlayScreen implements Screen {

    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort; // defines how the game screen will adapt to different devices screen sizes
    //the hud class is where we have the stage and table with bitmap font for score, level and life information.
    private Hud hud;


    private TmxMapLoader mapLoader; //responsible to load our map into the game
    private TiledMap map; // reference to the mao itself
    private OrthogonalTiledMapRenderer renderer; // what renders our map to the screen

    //Box2d Properties
    private World world;
    private Box2DDebugRenderer b2dr;

    //Mario Sprite property
    private Mario player;

    public PlayScreen(MarioBros game){
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

        //Instantiate Mario
        this.player = new Mario(world);

        //will create a body and fixture in every corresponding body in our tiledmap layers.

        BodyDef bodyDef = new BodyDef(); //body definition
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef(); //fixture definition
        Body body; //the body itself

        //---the Ground Body / fixtures: ---
        //get(2) = counting layers in Tiledmap software from the bottom up(the first object before the images)
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //define our body as static
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() /2) / MarioBros.PPM, (rect.getY() + rect.getHeight() /2) / MarioBros.PPM);
            body = world.createBody(bodyDef); //add the body to the world.

            polygonShape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM, (rect.getHeight() /2) / MarioBros.PPM);
            fixtureDef.shape = polygonShape; //defining the shape of the fixture.
            body.createFixture(fixtureDef); //add the fixture to the body.
        }

        //---the Pipe Body / fixtures: ---
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //define our body as static
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() /2) / MarioBros.PPM, (rect.getY() + rect.getHeight() /2) / MarioBros.PPM);
            body = world.createBody(bodyDef); //add the body to the world.

            polygonShape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM, (rect.getHeight() /2) / MarioBros.PPM);
            fixtureDef.shape = polygonShape; //defining the shape of the fixture.
            body.createFixture(fixtureDef); //add the fixture to the body.
        }

        //---the Brick Body / fixtures: ---
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //define our body as static
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() /2) / MarioBros.PPM, (rect.getY() + rect.getHeight() /2) / MarioBros.PPM);
            body = world.createBody(bodyDef); //add the body to the world.

            polygonShape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM, (rect.getHeight() /2) / MarioBros.PPM);
            fixtureDef.shape = polygonShape; //defining the shape of the fixture.
            body.createFixture(fixtureDef); //add the fixture to the body.
        }

        //---the Coin Body / fixtures: ---
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //define our body as static
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() /2) / MarioBros.PPM, (rect.getY() + rect.getHeight() /2) / MarioBros.PPM);
            body = world.createBody(bodyDef); //add the body to the world.

            polygonShape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM, (rect.getHeight() /2)/MarioBros.PPM );
            fixtureDef.shape = polygonShape; //defining the shape of the fixture.
            body.createFixture(fixtureDef); //add the fixture to the body.
        }

    }



    //new method to do all the updating of our game world
    public void update(float deltaTime){

        //check if there is any inputs happening
        handleInput(deltaTime);

        world.step(1/60f, 6, 2);

        //Always center the Camera in Mario position on the screen
        gameCam.position.x = player.b2body.getPosition().x;

        gameCam.update();

        //let the map renderer now what it needs to render - Only render what our gameCam can see.
        renderer.setView(gameCam);

    }

    //if there is any input, gameCam position moves x to show the world
    public void handleInput(float deltaTime){

        // -- Input Handler to capture up button (Jump) --
      if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
          //linear Impulse (dont have acceleration) : param 1st - direction and force; 2nd where in the body the force will be applied; 3trd is to awake the body.
          player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
      }

      // -- Input Handler to capture Right and Left buttons (Move) --
        // isKeyPressed != isJustKeyPressed - the first sees if the button is being hold down
        // second condition checks if Mario is not moving faster than a specifc speed.
      if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
          player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
      }
      if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
          player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
      }

    }

    @Override
    public void render(float delta) {
        //call the custom method update to check if there is any input happening
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //renderer out Box2dDebugLines (green box2d lines)
        b2dr.render(world,gameCam.combined);



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
