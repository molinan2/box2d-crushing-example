package com.jmolina.crushing;

import com.badlogic.gdx.Game;
import com.jmolina.crushing.screens.MainScreen;


/**
 * Creates the game
 */
public class Box2DCrushingExample extends Game {

    private MainScreen mainScreen;

	public Box2DCrushingExample() {
    }

	@Override
	public void create () {
        mainScreen = new MainScreen();
        setScreen(mainScreen);
	}

	@Override
	public void dispose () {
		mainScreen.dispose();
	}
}
