package com.groupone.wtd.Assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.GameLauncher;

public class SoundManager {
    private static Sound gunShoot;
    private static final Sound[] duckFall = new Sound[3];
    private static final Sound[] gunCry = new Sound[3];
    private static final Sound[] gunLaugh = new Sound[3];
    private static final Sound[] numbers = new Sound[10];
    private static final Sound[] operators = new Sound[4];
    private static Sound quit;
    private static Sound numHunt;
    private static Sound wordHunt;
    private static Sound about;
    private static Sound _continue;
    private static Sound reload;
    private static Sound click;
    private static Sound switchGun;
    private static Sound logo;
    private static Music mainMenu;
    private static GameLauncher game;
    private static Music backgroundMusic;

    public static void initializeSound(GameLauncher game){
        SoundManager.game = game;
        gunShoot = game.manager.get("Sounds/Guns/gun_shot.mp3");
        quit = game.manager.get("Sounds/Buttons/quit.mp3");
        _continue = game.manager.get("Sounds/Buttons/continue.mp3");
        about = game.manager.get("Sounds/Buttons/about.mp3");
        wordHunt = game.manager.get("Sounds/Buttons/word_hunt.mp3");
        numHunt = game.manager.get("Sounds/Buttons/num_hunt.mp3");
        click = game.manager.get("Sounds/Buttons/click.mp3");
        reload = game.manager.get("Sounds/Guns/reload.mp3");
        logo = game.manager.get("Sounds/Buttons/logo.mp3");
        switchGun = game.manager.get("Sounds/Guns/switch.mp3");
        mainMenu = game.manager.get("Sounds/Background/main_menu.mp3");
        operators[0] = game.manager.get("Sounds/Guns/plus.mp3");
        operators[1] = game.manager.get("Sounds/Guns/minus.mp3");
        operators[2] = game.manager.get("Sounds/Guns/times.mp3");
        operators[3] = game.manager.get("Sounds/Guns/divide.mp3");

        for(int i = 0; i < 10; i++){
            numbers[i] = game.manager.get("Sounds/alphanumerics/" + (i + 1) + ".mp3");
        }

        for(int i = 1; i <= 3; i++){
            duckFall[i - 1] = game.manager.get("Sounds/Ducks/fall_" + i +".mp3", Sound.class);
            gunLaugh[i - 1] = game.manager.get("Sounds/Guns/laugh_" + i + ".mp3", Sound.class);
            gunCry[i - 1] = game.manager.get("Sounds/Guns/cry_" + i + ".mp3", Sound.class);
        }
    }

    public static void playOperation(int operator, int number){
        operators[operator - 1].play();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                numbers[number - 1].play();
            }
        }, 0.5f);
    }

    public static void playLogo(){
        logo.play();
    }

    public static void setMainMenu(){
        backgroundMusic = mainMenu;
        backgroundMusic.setLooping(true);
    }

    public static void stopBackgroundMusic(){
        backgroundMusic.stop();
    }

    public static void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public static void playQuit(){
        quit.play();
    }

    public static void playSwitch(){
        switchGun.play();
    }

    public static void playGunCry(){
        int random = MathUtils.random(0, 2);
        gunCry[random].play();
    }

    public static void playGunLaugh(){
        int random = MathUtils.random(0, 2);
        gunLaugh[random].play();
    }

    public static void playReload(){
        reload.play();
    }

    public static void playNumHunt(){
        numHunt.play();
    }

    public static void playWordHunt(){
        wordHunt.play();
    }

    public static void playAbout(){
        about.play();
    }

    public static void playContinue(){
        _continue.play();
    }

    public static void playDuckFall(){
        int random = MathUtils.random(0, 2);
        duckFall[random].play();
    }

    public static void playGunShot(){
        gunShoot.play();
    }

    public static void playClick() {
        click.play();
    }
}
