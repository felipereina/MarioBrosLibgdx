package com.felipereina.tutorial.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.felipereina.tutorial.MarioBros;
import com.felipereina.tutorial.Sprites.Enemies.Enemy;
import com.felipereina.tutorial.Sprites.InteractiveTileObject;
import com.felipereina.tutorial.Sprites.Items.Item;
import com.felipereina.tutorial.Sprites.Mario;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int colisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (colisionDef){

            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT) {
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                } else{
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                }
                    break;
            //check if Mario colided with Enemy head
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                } else{
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                }
                break;
                //Reverse Enemy velocity when colide with object
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                } else{
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
                //Reverse Item velocity when colide with object
            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                } else{
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
                //Mario dies when colide with Enemy
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT) {
                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
                } else{
                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                }
                break;
                //case 2 enemies colide between themselfs revert velocity
            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            //case Mario colide with Item he uses it
            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                } else{
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                }
                break;
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
