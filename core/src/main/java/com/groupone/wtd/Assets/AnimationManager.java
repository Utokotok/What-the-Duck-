package com.groupone.wtd.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class AnimationManager {
    public static Array<Animation<TextureRegion>> wDuck = new Array<>();
    public static Array<Animation<TextureRegion>> yDuck = new Array<>();
    public static Array<Animation<TextureRegion>> gDuck = new Array<>();
    private static GameLauncher game;

    public static void setGame(GameLauncher game){
        AnimationManager.game = game;
    }

    public static void initializeDucks(){
        wDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/w_duck_fall.png"), 4, 1)));
        wDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/w_duck_down.png"), 2, 1)));
        wDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/w_duck_up.png"), 4, 1)));
        wDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/w_duck_left.png"), 4, 1)));
        wDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/w_duck_shock.png"), 2, 1)));
        gDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/g_duck_fall.png"), 4, 1)));
        gDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/g_duck_down.png"), 2, 1)));
        gDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/g_duck_up.png"), 4, 1)));
        gDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/g_duck_left.png"), 4, 1)));
        gDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/g_duck_shock.png"), 2, 1)));
        yDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/y_duck_fall.png"), 4, 1)));
        yDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/y_duck_down.png"), 2, 1)));
        yDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/y_duck_up.png"), 4, 1)));
        yDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/y_duck_left.png"), 4, 1)));
        yDuck.add(new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/y_duck_shock.png"), 2, 1)));
    }

}
