package com.felipereina.tutorial.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Sprites.Enemy;
import com.felipereina.tutorial.Sprites.InteractiveTileObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int colisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //check if one of the fixtures beginning contact is the head of Mario
        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            //check if the other object is an interactive Tile object
            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        //check if Mario colided with Enemy head
        switch (colisionDef){
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead();
                } else if(fixB.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
                ((Enemy)fixB.getUserData()).hitOnHead();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
