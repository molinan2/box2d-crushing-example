package com.jmolina.crushing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen {

    private final float DENSITY = 1.0f;
    private final float RESTITUTION = 0.9f;
    private final float FRICTION = 0.8f;
    private final float WIDTH = 25.6f;
    private final float HEIGHT = 25.6f;
    private final float TIME_STEP = 1/60f;
    private final int VELOCITY_ITERATIONS = 8;
    private final int POSITION_ITERATIONS = 3;

    private Body ball;

    World world;
    Box2DDebugRenderer debugRenderer;
    Viewport viewport;
    MainStage mainStage;

    public MainScreen() {
        Vector2 gravity = new Vector2(0, -10f);
        world = new World(gravity, false);
        debugRenderer = new Box2DDebugRenderer();
        viewport = new FitViewport(WIDTH, HEIGHT);
        mainStage = new MainStage(viewport);

        createBox(25, 2, 0.5f * WIDTH, 1);
        createBall(1, 0.5f * WIDTH, 0.5f * HEIGHT);
    }

    @Override
    public void show() {
        Gdx.app.setLogLevel(Logger.DEBUG);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        mainStage.draw();
        debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
    }

    /**
     * Creates a squared static body
     *
     * @param width
     * @param height
     * @param x
     * @param y
     */
    private void createBox(float width, float height, float x, float y) {
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.restitution = RESTITUTION;
        fixtureDef.friction = FRICTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = 0 * MathUtils.degreesToRadians;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();
    }

    private void createBall(float radius, float x, float y) {
        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.restitution = RESTITUTION;
        fixtureDef.friction = FRICTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        ball = world.createBody(bodyDef);
        ball.createFixture(fixtureDef);

        fixtureDef.shape.dispose();
        ball.setAwake(true);
    }

}
