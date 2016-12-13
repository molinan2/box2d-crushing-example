package com.jmolina.crushing;

import com.badlogic.gdx.Game;
import com.jmolina.crushing.screens.MainScreen;


/**
 * Creates the game
 */
public class Box2DCrushingExample extends Game {

    private MainScreen mainScreen;
	private int height;

	public Box2DCrushingExample() {
        this(480);
    }

	public Box2DCrushingExample(int height) {
		this.height = height;
	}

	@Override
	public void create () {
        mainScreen = new MainScreen(height);
        setScreen(mainScreen);
	}

	@Override
	public void dispose () {
		mainScreen.dispose();
	}
}
