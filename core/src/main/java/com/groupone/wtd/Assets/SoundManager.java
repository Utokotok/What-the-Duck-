package com.groupone.wtd.Assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.GameLauncher;

public class SoundManager {
    private static Sound gunShoot;
    private static float voiceVolume = 1f;
    private static float gunVolume = 0.5f;
    private static float duckVolume = 0.3f;
    private static long backgroundID;
    private static float backgroundVolume = 1f;

    private static final Sound[] duckFall = new Sound[3];
    private static final Sound[] gunCry = new Sound[3];
    private static final Sound[] gunLaugh = new Sound[3];
    private static final Sound[] numbers = new Sound[10];
    private static final Sound[] applause = new Sound[11];
    private static final Sound[] disappoint = new Sound[9];
    private static final Sound[] operators = new Sound[4];
    private static final Sound[] gameOverAdd = new Sound[3];
    private static Sound gameOver;
    private static Sound wonk;
    private static Sound quit;
    private static Sound numHunt;
    private static Sound wordHunt;
    private static Sound about;
    private static Sound _continue;
    private static Sound reload;
    private static Sound click;
    private static Sound switchGun;
    private static Sound logo;
    private static Sound playAgain;
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
        playAgain = game.manager.get("Sounds/Buttons/play_again.mp3");
        reload = game.manager.get("Sounds/Guns/reload.mp3");
        logo = game.manager.get("Sounds/Buttons/logo.mp3");
        switchGun = game.manager.get("Sounds/Guns/switch.mp3");
        mainMenu = game.manager.get("Sounds/Background/main_menu.mp3");
        gameOver = game.manager.get("Sounds/GameOver/game_over.mp3");
        wonk = game.manager.get("Sounds/GameOver/wonk.mp3");
        operators[0] = game.manager.get("Sounds/Guns/plus.mp3");
        operators[1] = game.manager.get("Sounds/Guns/minus.mp3");
        operators[2] = game.manager.get("Sounds/Guns/times.mp3");
        operators[3] = game.manager.get("Sounds/Guns/divide.mp3");

        for(int i = 0; i < 10; i++){
            numbers[i] = game.manager.get("Sounds/alphanumerics/" + (i + 1) + ".mp3");
        }

        for(int i = 0; i < 11; i++){
            applause[i] = game.manager.get("Sounds/Applause/" + (i + 1) + ".mp3");
            if(i > 8) continue;
            disappoint[i] = game.manager.get("Sounds/Disappoint/" + (i + 1) + ".mp3");
        }

        for(int i = 1; i <= 3; i++){
            duckFall[i - 1] = game.manager.get("Sounds/Ducks/fall_" + i +".mp3");
            gunLaugh[i - 1] = game.manager.get("Sounds/Guns/laugh_" + i + ".mp3");
            gunCry[i - 1] = game.manager.get("Sounds/Guns/cry_" + i + ".mp3");
            gameOverAdd[i - 1] = game.manager.get("Sounds/GameOver/" + i + ".mp3");
        }
    }

    //UI
    public static void stopBackgroundMusic(){
        backgroundMusic.stop();
    }

    public static void playBackgroundMusic(){
        backgroundMusic.play();
    }

    public static void setMainMenu(){
        backgroundMusic = mainMenu;
        backgroundMusic.setLooping(true);
    }

    public static void playLogo(){
        long id = logo.play();
        logo.setVolume(id, voiceVolume);
    }


    //Voice
    public static void playApplause(){
        int random = MathUtils.random(0, 10);
        long id = applause[random].play();
        applause[random].setVolume(id, voiceVolume);
    }

    public static void playDisappoint(){
        int random = MathUtils.random(0, 8);
        long id = disappoint[random].play();
        disappoint[random].setVolume(id, voiceVolume);
    }

    public static void playQuit(){
        long id = quit.play();
        quit.setVolume(id, voiceVolume);
    }

    public static void playGameOver(){
        long gameOverID = gameOver.play();
        gameOver.setVolume(gameOverID, voiceVolume);
        long wonkID = wonk.play();
        wonk.setVolume(wonkID, voiceVolume - 0.5f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                int random = MathUtils.random(0, 2);
                long id = gameOverAdd[random].play();
                gameOverAdd[random].setVolume(id, voiceVolume);
            }
        }, 2f);
    }

    public static void playNumHunt(){
        long id = numHunt.play();
        numHunt.setVolume(id, voiceVolume);
    }

    public static void playWordHunt(){
        long id = wordHunt.play();
        wordHunt.setVolume(id, voiceVolume);
    }

    public static void playAbout(){
        long id = about.play();
        about.setVolume(id, voiceVolume);
    }

    public static void playContinue(){
        long id = _continue.play();
        _continue.setVolume(id, voiceVolume);
    }

    public static void playPlayAgain(){
        long id = playAgain.play();
        playAgain.setVolume(id, voiceVolume);
    }

    public static void playClick() {
        long id = click.play();
        click.setVolume(id, voiceVolume);
    }

    //Gun
    public static void playSwitch(){
        long id = switchGun.play();
        switchGun.setVolume(id, gunVolume);
    }

    public static void playGunCry(){
        int random = MathUtils.random(0, 2);
        long id = gunCry[random].play();
        gunCry[random].setVolume(id, gunVolume);
    }

    public static void playGunLaugh(){
        int random = MathUtils.random(0, 2);
        long id = gunLaugh[random].play();
        gunLaugh[random].setVolume(id, gunVolume);
    }

    public static void playGunShot(){
        long id = gunShoot.play();
        gunShoot.setVolume(id, gunVolume);
    }

    public static void playReload(){
        long id = reload.play();
        reload.setVolume(id, gunVolume);
    }


    //Duck
    public static void playDuckFall(){
        int random = MathUtils.random(0, 2);
        long id = duckFall[random].play();
        duckFall[random].setVolume(id, duckVolume);
    }

    //    public static void playOperation(int operator, int number){
//        operators[operator - 1].play();
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                numbers[number - 1].play();
//            }
//        }, 0.5f);
//    }

}
