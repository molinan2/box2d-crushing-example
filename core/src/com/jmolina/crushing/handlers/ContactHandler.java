package com.jmolina.crushing.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jmolina.crushing.data.UserData;
import com.jmolina.crushing.interfaces.GameActions;


/**
 * Detects a collision between bodies and activates their destruction if necessary.
 */
public class ContactHandler implements ContactListener {

    private GameActions gameActions;

    public ContactHandler(GameActions gameActions) {
        this.gameActions = gameActions;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        UserData userDataA = (UserData) bodyA.getUserData();
        UserData userDataB = (UserData) bodyB.getUserData();

        // Any collision between destructible and destroyer bodies leads to
        // the game "destroy" (locked) state
        if (userDataA.isDestructible() && userDataB.isDestroyer() ||
            userDataA.isDestroyer() && userDataB.isDestructible()) {
            gameActions.destroy();
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
