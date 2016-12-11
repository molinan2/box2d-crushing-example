package com.jmolina.crushing;

import com.badlogic.gdx.Game;

public class Crushing extends Game {

    private MainScreen mainScreen;

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
