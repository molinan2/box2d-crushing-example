package com.jmolina.crushing.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
import com.jmolina.crushing.data.UserData;
import com.jmolina.crushing.handlers.ContactHandler;
import com.jmolina.crushing.interfaces.GameActions;


/**
 * The only screen of the game, containing the Box2D physic world, visual elements and game logic.
 */
public class MainScreen extends ScreenAdapter {

    // World size
    private final float WORLD_WIDTH = 32f;
    private final float WORLD_HEIGHT = 24f;

    // Ball starting position
    private final float BALL_X = 4f;
    private final float BALL_Y = 12f;

    private boolean locked = false;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body crusher, hidden, ball;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture legend, legendCrushed;

    /**
     * The screen of the game
     */
    public MainScreen() {
        // Creates the world
        world = new World(new Vector2(0, -40f), false);
        debugRenderer = new Box2DDebugRenderer();

        // Creates the viewport and places the camera so the (0,0) is bottom left
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        viewport.getCamera().position.set(WORLD_WIDTH /2, WORLD_HEIGHT /2, 0);

        // Creates a batch to draw the legend (BitmapFont is broken in this version of libGDX)
        batch = new SpriteBatch();
        legend = new Texture(Gdx.files.internal("legend.png"), Pixmap.Format.RGBA8888, true);
        legendCrushed = new Texture(Gdx.files.internal("legend-crushed.png"), Pixmap.Format.RGBA8888, true);

        // Creates all bodies
        createBodies();

        // Attach a contact listener to the physics world, so that it will be able
        // to lock the game when the ball gets destroyed
        world.setContactListener(new ContactHandler(new GameActions() {
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

        checkRestart();
        update();

        // Render the bodies and the legend
        batch.begin();

        if (!locked) batch.draw(legend, 32, 260);
        else batch.draw(legendCrushed, 32, 260);

        batch.end();
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
        batch.dispose();
    }

    /**
     * Update physics and movements if the game is not locked
     */
    private void update() {
        if (!locked) {
            world.step(1/60f, 8, 3);
            updateCrusher();
            updateBall();
        }
    }

    /**
     * Polls the keyboard and restart the game if the key is pressed
     */
    private void checkRestart() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            unlock();
            restart();
        }
    }

    /**
     * Moves the crusher and the hidden destroyer up and down
     */
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

    /**
     * Polls the keyboard to let the user move the ball
     */
    private void updateBall() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            ball.applyLinearImpulse(-1, 0, ball.getPosition().x, ball.getPosition().y, true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            ball.applyLinearImpulse(1, 0, ball.getPosition().x, ball.getPosition().y, true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            ball.applyLinearImpulse(0, 40, ball.getPosition().x, ball.getPosition().y, true);
        }
    }

    /**
     * Create all the physic bodies
     */
    private void createBodies() {
        float sizeRatio = 0.98f;
        float crusherWidth = 10;
        float crusherHeight = 8;
        Vector2 crusherVelocity = new Vector2(0, -4);

        // Static walls
        createWall(WORLD_WIDTH, 2, WORLD_WIDTH /2, 0);
        createWall(2, WORLD_HEIGHT, 0, WORLD_HEIGHT /2);
        createWall(2, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT /2);

        // Kinetic crusher and its hidden destroyer. The hidden destroyer is placed
        // at the same point and moves at the same pace that the crusher.
        crusher = createBox(crusherWidth, crusherHeight, 22, WORLD_HEIGHT /2);
        crusher.setLinearVelocity(crusherVelocity);

        hidden = createBox(sizeRatio * crusherWidth, sizeRatio * crusherHeight, crusher.getPosition().x, crusher.getPosition().y);
        hidden.setLinearVelocity(crusherVelocity);
        hidden.setUserData(new UserData(true, false)); // UserData 'destroyer'

        // Dynamic ball to be destroyed
        ball = createBall(1, BALL_X, BALL_Y);
        ball.setUserData(new UserData(false, true)); // UserData 'destructible'
    }

    /**
     * Creates a static wall
     *
     * @param width     Width
     * @param height    Height
     * @param x         X position
     * @param y         Y position
     * @return          The body
     */
    private Body createWall(float width, float height, float x, float y) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);

        return createBody(shape, BodyDef.BodyType.StaticBody, x, y);
    }

    /**
     * Creates a kinetic box
     *
     * @param width     Width
     * @param height    Height
     * @param x         X position
     * @param y         Y position
     * @return          The body
     */
    private Body createBox(float width, float height, float x, float y) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);

        return createBody(shape, BodyDef.BodyType.KinematicBody, x, y);
    }

    /**
     * Creates a dynamic ball
     *
     * @param radius    Radius
     * @param x         X position
     * @param y         Y position
     * @return          The body
     */
    private Body createBall(float radius, float x, float y) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        return createBody(shape, BodyDef.BodyType.DynamicBody, x, y);
    }

    /**
     * Creates a generic body
     *
     * @param shape    Shape
     * @param type     Type (Dynamic, Kinetic or Static)
     * @param x        X position
     * @param y        Y position
     * @return         The body
     */
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

    /**
     * Locks the game
     */
    private void lock() {
        locked = true;
    }

    /**
     * Unlocks the game
     */
    private void unlock() {
        locked = false;
    }

    /**
     * Restart the game
     */
    private void restart() {
        ball.setLinearVelocity(0, 0);
        ball.setAngularVelocity(0);
        ball.setTransform(BALL_X, BALL_Y, 0);
        unlock();
    }

}
