package com.jmolina.crushing.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jmolina.crushing.Box2DCrushingExample;

public class DesktopLauncher {
	public static void main (String[] arg) {
		int width = 640;
		int height = 480;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = width;
        config.height = height;
		new LwjglApplication(new Box2DCrushingExample(), config);
	}
}
