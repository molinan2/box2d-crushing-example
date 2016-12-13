package com.jmolina.crushing.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jmolina.crushing.data.UserData;

public class ContactHandler implements ContactListener {

    private com.jmolina.crushing.interfaces.GameHandler gameHandler;

    public ContactHandler(com.jmolina.crushing.interfaces.GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        UserData userDataA = (UserData) bodyA.getUserData();
        UserData userDataB = (UserData) bodyB.getUserData();

        if (userDataA.isDestroyable() && userDataB.isDestroyer() || userDataA.isDestroyer() && userDataB.isDestroyable()) {
            gameHandler.destroy();
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
