package com.jmolina.crushing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {

    private final float WIDTH = 32f;
    private final float HEIGHT = 24f;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Viewport viewport;
    private Body crusher, hidden, destroyable;
    private boolean paused = false;

    public MainScreen() {
        Vector2 gravity = new Vector2(0, -40f);
        world = new World(gravity, false);
        debugRenderer = new Box2DDebugRenderer();
        viewport = new FitViewport(WIDTH, HEIGHT);
        viewport.getCamera().position.set(WIDTH/2, HEIGHT/2, 0);

        createWall(WIDTH, 2, WIDTH/2, 0);
        createWall(2, HEIGHT, 0, HEIGHT/2);
        createWall(2, HEIGHT, WIDTH, HEIGHT/2);
        crusher = createBox(10, 8, 22, HEIGHT/2);

        UserData hiddenUserData = new UserData(true, false);
        hidden = createBox(9.8f, 7.8f, crusher.getPosition().x, crusher.getPosition().y);
        hidden.setUserData(hiddenUserData);

        UserData destroyableUserData = new UserData(false, true);
        destroyable = createBall(1, 3, 12);
        destroyable.setUserData(destroyableUserData);

        crusher.setLinearVelocity(0, -4);
        hidden.setLinearVelocity(0, -4);

        ContactListener contactHandler = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                UserData userDataA = (UserData) bodyA.getUserData();
                UserData userDataB = (UserData) bodyB.getUserData();

                if (userDataA.isDestroyable() && userDataB.isDestroyer()) {
                    destroy();
                }
                else if (userDataA.isDestroyer() && userDataB.isDestroyable()) {
                    destroy();
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
        };

        world.setContactListener(contactHandler);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused) {
            world.step(1/60f, 8, 3);

            if (crusher.getPosition().y < 0) {
                crusher.setLinearVelocity(0, 4);
                hidden.setLinearVelocity(0, 4);
            }
            else if (crusher.getPosition().y > HEIGHT/2) {
                crusher.setLinearVelocity(0, -4);
                hidden.setLinearVelocity(0, -4);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                destroyable.applyLinearImpulse(-1, 0, destroyable.getPosition().x, destroyable.getPosition().y, true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                destroyable.applyLinearImpulse(1, 0, destroyable.getPosition().x, destroyable.getPosition().y, true);
            }
        }

        debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        world.dispose();
    }

    private Body createWall(float width, float height, float x, float y) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);

        return createBody(shape, BodyDef.BodyType.StaticBody, x, y);
    }

    private Body createBox(float width, float height, float x, float y) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);

        return createBody(shape, BodyDef.BodyType.KinematicBody, x, y);
    }

    private Body createBall(float radius, float x, float y) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        return createBody(shape, BodyDef.BodyType.DynamicBody, x, y);
    }

    private Body createBody(Shape shape, BodyDef.BodyType type, float x, float y) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.3f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new UserData());

        fixtureDef.shape.dispose();

        return body;
    }

    private void destroy() {
        paused = true;
    }

}
