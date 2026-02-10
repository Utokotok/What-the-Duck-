package com.groupone.wtd.Assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static AssetManager manager = new AssetManager();
    private static char[] duckColor = {'y', 'g', 'w'};
    public static AssetManager getManager(){
        return manager;
    }

    public static void loadMenuAssets(){
        manager.load("Buttons/word_hunt.png", Texture.class);
        manager.load("Buttons/num_hunt.png", Texture.class);
        manager.load("Buttons/quit.png", Texture.class);
        manager.load("Buttons/continue.png", Texture.class);
        manager.load("Buttons/pause.png", Texture.class);
        manager.load("Background/bush.png", Texture.class);
        manager.load("Background/background.png", Texture.class);
        manager.load("Logo/wtd_logo.png", Texture.class);
        manager.load("Sounds/button_press.mp3", Sound.class);
        manager.load("Sounds/button_hover.mp3", Sound.class);
        manager.load("Guns/gun_crying.png", Texture.class);
        manager.load("Guns/gun_laughing.png", Texture.class);
        manager.load("Guns/gun_shot.png", Texture.class);
        manager.load("Guns/egg_shot.png", Texture.class);
        manager.load("Background/clouds.png", Texture.class);

        for(char color : duckColor){
            manager.load("Ducks/" + color +"_duck_down.png", Texture.class);
            manager.load("Ducks/" + color +"_duck_up.png", Texture.class);
            manager.load("Ducks/" + color +"_duck_left.png", Texture.class);
            manager.load("Ducks/" + color +"_duck_fall.png", Texture.class);
            manager.load("Ducks/" + color +"_duck_shock.png", Texture.class);
        }

        for(int i = 1; i < 7; i++){
            manager.load("Clouds/cloud_" + i +".png", Texture.class);
        }
    }
}
