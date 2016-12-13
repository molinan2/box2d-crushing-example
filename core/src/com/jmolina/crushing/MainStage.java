package com.jmolina.crushing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainStage extends Stage {

    Image image;

    public MainStage(Viewport viewport) {
        super(viewport);

        Texture texture = new Texture(Gdx.files.internal("tile_black.png"));
        image = new Image(texture);
        image.setPosition(0, 0);
        image.setScale(1, 1);
        image.setSize(25.6f, 25.6f);
        //addActor(image);
    }

}
