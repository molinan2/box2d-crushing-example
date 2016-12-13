package com.jmolina.crushing.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends ScreenAdapter {

    private final float WORLD_WIDTH = 32f;
    private final float WORLD_HEIGHT = 24f;
    private final float BALL_X = 4f;
    private final float BALL_Y = 12f;

    private int windowHeight;
    private boolean locked = false;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Viewport viewport;
    private Body crusher, hidden, destroyable;
    private SpriteBatch batch;
    private BitmapFont font;


    public MainScreen(int windowHeight) {
        world = new World(new Vector2(0, -40f), false);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        viewport.getCamera().position.set(WORLD_WIDTH /2, WORLD_HEIGHT /2, 0);
        debugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        this.windowHeight = windowHeight;

        createBodies();

        world.setContactListener(new com.jmolina.crushing.handlers.ContactHandler(new com.jmolina.crushing.handlers.GameHandler() {
            @Override
            public void destroy() {
                lock();
            }
        }));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!locked) {
            world.step(1/60f, 8, 3);
            updateCrusher();
            updateBall();
        }

        checkRestart();
        debugRenderer.render(world, viewport.getCamera().combined);
        renderText();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
        world.dispose();
        batch.dispose();
        font.dispose();
    }

    private void checkRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            unlock();
            restart();
        }
    }

    private void updateCrusher() {
        if (crusher.getPosition().y < 0) {
            crusher.setLinearVelocity(0, 4);
            hidden.setLinearVelocity(0, 4);
        }
        else if (crusher.getPosition().y > WORLD_HEIGHT /2) {
            crusher.setLinearVelocity(0, -4);
            hidden.setLinearVelocity(0, -4);
        }
    }

    private void updateBall() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            destroyable.applyLinearImpulse(-1, 0, destroyable.getPosition().x, destroyable.getPosition().y, true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            destroyable.applyLinearImpulse(1, 0, destroyable.getPosition().x, destroyable.getPosition().y, true);
        }
    }

    private void renderText() {
        batch.begin();

        font.draw(batch, "'LEFT ARROW': Move to left", 40, windowHeight - 20);
        font.draw(batch, "'RIGHT ARROW': Move to right", 40, windowHeight - 40);
        font.draw(batch, "'R': Restart", 40, windowHeight -60);

        if (locked)
            font.draw(batch, "CRUSHED!", 40, windowHeight - 100);

        batch.end();
    }

    private void createBodies() {
        createWall(WORLD_WIDTH, 2, WORLD_WIDTH /2, 0);
        createWall(2, WORLD_HEIGHT, 0, WORLD_HEIGHT /2);
        createWall(2, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT /2);
        crusher = createBox(10, 8, 22, WORLD_HEIGHT /2);
        hidden = createBox(9.8f, 7.8f, crusher.getPosition().x, crusher.getPosition().y);
        hidden.setUserData(new com.jmolina.crushing.data.UserData(true, false));
        destroyable = createBall(1, BALL_X, BALL_Y);
        destroyable.setUserData(new com.jmolina.crushing.data.UserData(false, true));
        crusher.setLinearVelocity(0, -4);
        hidden.setLinearVelocity(0, -4);
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
        body.setUserData(new com.jmolina.crushing.data.UserData());

        fixtureDef.shape.dispose();

        return body;
    }

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    private void restart() {
        destroyable.setLinearVelocity(0, 0);
        destroyable.setAngularVelocity(0);
        destroyable.setTransform(BALL_X, BALL_Y, 0);
        unlock();
    }

}
