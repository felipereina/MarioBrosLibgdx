package com.felipereina.tutorial.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Sprites.Brick;
import com.felipereina.tutorial.Sprites.Coin;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map){

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

            //instantiate Brick object
            new Brick(world, map, rect);
        }

        //---the Coin Body / fixtures: ---
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //first get the rectangle object itself (type cast)
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //instantiate Coin object
            new Coin(world, map, rect);
        }



    }

}
