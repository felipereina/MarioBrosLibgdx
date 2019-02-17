package com.felipereina.tutorial.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Screens.PlayScreen;
import com.felipereina.tutorial.Sprites.Brick;
import com.felipereina.tutorial.Sprites.Coin;
import com.felipereina.tutorial.Sprites.Enemies.Enemy;
import com.felipereina.tutorial.Sprites.Enemies.Goomba;
import com.felipereina.tutorial.Sprites.Enemies.Turtle;

import java.util.Iterator;

public class B2WorldCreator {

    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

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
            fixtureDef.filter.categoryBits = MarioBros.OBJECT_BIT; //defining a identity to the fixture;
            body.createFixture(fixtureDef); //add the fixture to the body.
        }

        //---the Brick Body / fixtures: ---
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)

            //instantiate Brick object
            new Brick(screen, object);
        }

        //---the Coin Body / fixtures: ---
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            //instantiate Coin object
            new Coin(screen, object);
        }

        //Create All Goombas for this phase screen
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //instantiate Goombas
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }

        //Create All Turtles for this phase screen
        turtles = new Array<Turtle>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //instantiate Turtles
            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));

        }

    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);

        return enemies;
    }

    public Iterator<Goomba> getGoombaIterator(){
        return goombas.iterator();
    }
}
