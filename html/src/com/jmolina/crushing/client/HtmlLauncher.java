package com.jmolina.crushing.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.jmolina.crushing.Box2DCrushingExample;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(640, 480);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Box2DCrushingExample();
        }
}