package com.jmolina.crushing;

import com.badlogic.gdx.Game;

public class Box2DCrushingExample extends Game {

    private com.jmolina.crushing.screens.MainScreen mainScreen;
	private int height;

	public Box2DCrushingExample(int height) {
		this.height = height;
	}

	@Override
	public void create () {
        mainScreen = new com.jmolina.crushing.screens.MainScreen(height);
        setScreen(mainScreen);
	}

	@Override
	public void dispose () {
		mainScreen.dispose();
	}
}
