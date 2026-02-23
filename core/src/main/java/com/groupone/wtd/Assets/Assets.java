package com.groupone.wtd.Assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.groupone.wtd.GameLauncher;

public class Assets {
    public static AssetManager manager = new AssetManager();
    private static char[] duckColor = {'y', 'g', 'w'};
    private static Texture whiteFlash;
    public static AssetManager getManager(){
        return manager;
    }

    public static Image getFlash(){
        Image flashOverlay = new Image(whiteFlash);
        flashOverlay.addAction(
            Actions.sequence(
                Actions.alpha(0.9f),
                Actions.delay(0.05f),
                Actions.removeActor()
            )
        );
        flashOverlay.setSize(GameLauncher.gameWidth, GameLauncher.gameHeight);
        return flashOverlay;
    };

    public static void loadMenuAssets(){
        loadFlash();
        manager.load("Buttons/word_hunt.png", Texture.class);
        manager.load("Buttons/num_hunt.png", Texture.class);
        manager.load("Buttons/quit.png", Texture.class);
        manager.load("Buttons/continue.png", Texture.class);
        manager.load("Buttons/try_again.png", Texture.class);
        manager.load("Buttons/about.png", Texture.class);
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
        manager.load("Guns/division.png", Texture.class);
        manager.load("Guns/sum.png", Texture.class);
        manager.load("Guns/product.png", Texture.class);
        manager.load("Guns/minus.png", Texture.class);
        manager.load("Guns/change_mode.png", Texture.class);
        manager.load("UI/wooden_board.png", Texture.class);
        manager.load("Sounds/Guns/gun_shot.mp3", Sound.class);
        manager.load("Sounds/Buttons/quit.mp3", Sound.class);
        manager.load("Sounds/Buttons/continue.mp3", Sound.class);
        manager.load("Sounds/Buttons/num_hunt.mp3", Sound.class);
        manager.load("Sounds/Buttons/word_hunt.mp3", Sound.class);
        manager.load("Sounds/Buttons/play_again.mp3", Sound.class);
        manager.load("Sounds/Buttons/about.mp3", Sound.class);
        manager.load("Sounds/Buttons/click.mp3", Sound.class);
        manager.load("Sounds/Guns/reload.mp3", Sound.class);
        manager.load("Sounds/Guns/switch.mp3", Sound.class);
        manager.load("Sounds/Guns/times.mp3", Sound.class);
        manager.load("Sounds/Guns/minus.mp3", Sound.class);
        manager.load("Sounds/Guns/divide.mp3", Sound.class);
        manager.load("Sounds/Guns/plus.mp3", Sound.class);
        manager.load("Sounds/Buttons/logo.mp3", Sound.class);
        manager.load("Sounds/GameOver/game_over.mp3", Sound.class);
        manager.load("Sounds/GameOver/wonk.mp3", Sound.class);
        manager.load("Sounds/Background/main_menu.mp3", Music.class);
        manager.load("Sounds/Buttons/main_menu_blox.mp3", Sound.class);

        for(int i = 1; i <= 11; i++){
            manager.load("Sounds/Applause/" + i + ".mp3", Sound.class);
            if(i > 9) continue;
            manager.load("Sounds/Disappoint/" + i + ".mp3", Sound.class);
        }

        for(int i = 0; i < 10; i++){
            manager.load("Sounds/alphanumerics/" + (i + 1) + ".mp3", Sound.class);
        }

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

        for(int i = 1; i <= 3; i++){
            manager.load("Sounds/Ducks/fall_" + i +".mp3", Sound.class);
            manager.load("Sounds/Guns/laugh_" + i +".mp3", Sound.class);
            manager.load("Sounds/Guns/cry_" + i +".mp3", Sound.class);
            manager.load("Sounds/GameOver/" + i + ".mp3", Sound.class);
        }

    }

    private static void loadFlash(){
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whiteFlash = new Texture(pixmap);
        pixmap.dispose();
    }
}
