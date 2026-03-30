package com.groupone.wtd.Assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.GameLauncher;

public class SoundManager {
    private static Sound gunShoot;
    private static float voiceVolume = 0.15f;
    private static float gunVolume = 1f;
    private static float duckVolume = 1f;
    private static long backgroundID;
    private static float backgroundVolume = 1f;
    private static float sfxVolume = 1f;

    // ── Global / Master volume (0.0 – 1.0) ───────────────────────────────────
    private static float globalVolume = 1.0f;

    // ── Setters & Getters ────────────────────────────────────────────────────
    private static void updateBackgroundMusicVolume() {
        if (backgroundMusic != null) {
            float volume = backgroundVolume * globalVolume;
            if (currentGameMusicLevel >= 1 && currentGameMusicLevel <= 3) {
                volume *= 0.6f;
            }
            backgroundMusic.setVolume(volume);
        }
    }

    public static void setGlobalVolume(float v) {
        globalVolume = MathUtils.clamp(v, 0f, 1f);
        updateBackgroundMusicVolume();
    }
    public static float getGlobalVolume() { return globalVolume; }

    public static void setVoiceVolume(float v) {
        voiceVolume = MathUtils.clamp(v, 0f, 1f);
    }
    public static float getVoiceVolume() { return voiceVolume; }

    public static void setGunVolume(float v) {
        gunVolume = MathUtils.clamp(v, 0f, 1f);
    }
    public static float getGunVolume() { return gunVolume; }

    public static void setDuckVolume(float v) {
        duckVolume = MathUtils.clamp(v, 0f, 1f);
    }
    public static float getDuckVolume() { return duckVolume; }

    public static void setBackgroundVolume(float v) {
        backgroundVolume = MathUtils.clamp(v, 0f, 1f);
        updateBackgroundMusicVolume();
    }
    public static float getBackgroundVolume() { return backgroundVolume; }

    public static void setSfxVolume(float v) {
        sfxVolume = MathUtils.clamp(v, 0f, 1f);
    }
    public static float getSfxVolume() { return sfxVolume; }

    private static final Sound[] duckFall = new Sound[3];
    private static final Sound[] gunCry = new Sound[3];
    private static final Sound[] gunLaugh = new Sound[3];
    private static final Sound[] numbers = new Sound[10];
    private static final Sound[] applause = new Sound[11];
    private static final Sound[] disappoint = new Sound[9];
    private static final Sound[] operators = new Sound[4];
    private static final Sound[] gameOverAdd = new Sound[3];
    private static Sound gameOver;
    private static Sound gameoverDrop;
    private static Sound tryagainDrop;
    private static Sound blox;
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
    private static Music gameMusic1;
    private static Music gameMusic2;
    private static Music gameMusic3;
    private static int currentGameMusicLevel = 1;
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
        mainMenu = game.manager.get("Sounds/Background/main_menu.wav");
        gameMusic1 = game.manager.get("Sounds/Background/background_music1.wav");
        gameMusic2 = game.manager.get("Sounds/Background/background_music2.wav");
        gameMusic3 = game.manager.get("Sounds/Background/background_music3.wav");
        gameOver = game.manager.get("Sounds/GameOver/game_over.mp3");
        gameoverDrop = game.manager.get("Sounds/GameOver/gameover_drop.mp3");
        tryagainDrop = game.manager.get("Sounds/GameOver/tryagain_drop.mp3");
        wonk = game.manager.get("Sounds/GameOver/wonk.mp3");
        blox = game.manager.get("Sounds/Buttons/main_menu_blox.mp3");
        operators[0] = game.manager.get("Sounds/Guns/plus.mp3");
        operators[1] = game.manager.get("Sounds/Guns/minus.mp3");
        operators[2] = game.manager.get("Sounds/Guns/times.mp3");
        operators[3] = game.manager.get("Sounds/Guns/divide.mp3");

        for(int i = 0; i < 10; i++){
            numbers[i] = game.manager.get("Sounds/alphanumerics/" + (i + 1) + ".mp3");
        }

        for(int i = 0; i < 11; i++){
            applause[i] = game.manager.get("Sounds/Applause/" + (i + 1) + ".mp3");
            if(i < 9){
                disappoint[i] = game.manager.get("Sounds/Disappoint/" + (i + 1) + ".mp3");
            }
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
        updateBackgroundMusicVolume();
    }

    public static void setMainMenu(){
        currentGameMusicLevel = 0;
        backgroundMusic = mainMenu;
        backgroundMusic.setLooping(true);
    }

    public static void setGameMusic() {
        currentGameMusicLevel = 1;
        backgroundMusic = gameMusic1;
        backgroundMusic.setLooping(true);
    }

    public static void setGameMusicLevel(int level) {
        if (level == currentGameMusicLevel) return;

        float position = 0f;
        if (backgroundMusic != null) {
            position = backgroundMusic.getPosition();
            backgroundMusic.stop();
        }

        if (level == 1) {
            backgroundMusic = gameMusic1;
        } else if (level == 2) {
            backgroundMusic = gameMusic2;
        } else if (level == 3) {
            backgroundMusic = gameMusic3;
        }

        backgroundMusic.setLooping(true);
        currentGameMusicLevel = level;
        updateBackgroundMusicVolume();
        
        backgroundMusic.play();
        backgroundMusic.setPosition(position);
    }

    public static void playLogo(){
        long id = logo.play();
        logo.setVolume(id, voiceVolume * 5f * globalVolume);
    }


    //Voice
    public static void playApplause(){
        int random = MathUtils.random(0, 10);
        long id = applause[random].play();
        applause[random].setVolume(id, voiceVolume * globalVolume);
    }

    public static void playBlox(){
        long id = blox.play();
        blox.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playDisappoint(){
        int random = MathUtils.random(0, 8);
        long id = disappoint[random].play();
        disappoint[random].setVolume(id, voiceVolume * globalVolume);
    }

    public static void playQuit(){
        long id = quit.play();
        quit.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playGameOverHalf1(){
        long gameOverID = gameOver.play();
        gameOver.setVolume(gameOverID, voiceVolume * 2 * globalVolume);
    }

    public static void playGameOverDrop(){
        // Playing sound 3 times simultaneously to artificially boost volume
        long id1 = gameoverDrop.play();
        long id2 = gameoverDrop.play();
        long id3 = gameoverDrop.play();
        gameoverDrop.setVolume(id1, 1.0f * globalVolume);
        gameoverDrop.setVolume(id2, 1.0f * globalVolume);
        gameoverDrop.setVolume(id3, 1.0f * globalVolume);
    }

    /**
     * Zero-point special case: plays gameover_drop.mp3 but stops it halfway through.
     * Uses a Timer to stop the sound after half its duration.
     */
    public static void playGameOverDropHalf() {
        final long id1 = gameoverDrop.play();
        final long id2 = gameoverDrop.play();
        final long id3 = gameoverDrop.play();
        gameoverDrop.setVolume(id1, 1.0f * globalVolume);
        gameoverDrop.setVolume(id2, 1.0f * globalVolume);
        gameoverDrop.setVolume(id3, 1.0f * globalVolume);
        // Stop after half the clip duration (adjust seconds to match actual audio length)
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameoverDrop.stop(id1);
                gameoverDrop.stop(id2);
                gameoverDrop.stop(id3);
            }
        }, 1.63f); // stops halfway through gameover_drop.mp3
    }

    public static void playTryAgainDrop(){
        // Playing sound 3 times simultaneously to artificially boost volume
        long id1 = tryagainDrop.play();
        long id2 = tryagainDrop.play();
        long id3 = tryagainDrop.play();
        tryagainDrop.setVolume(id1, 1.0f * globalVolume);
        tryagainDrop.setVolume(id2, 1.0f * globalVolume);
        tryagainDrop.setVolume(id3, 1.0f * globalVolume);
    }

    public static void playGameOverHalf2(){
        int random = MathUtils.random(0, 2);
        long id = gameOverAdd[random].play();
        gameOverAdd[random].setVolume(id, voiceVolume * globalVolume);
    }

    public static void playNumHunt(){
        long id = numHunt.play();
        numHunt.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playWordHunt(){
        long id = wordHunt.play();
        wordHunt.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playAbout(){
        long id = about.play();
        about.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playContinue(){
        long id = _continue.play();
        _continue.setVolume(id, voiceVolume * globalVolume);
    }

    public static void playPlayAgain(){
        long id = playAgain.play();
        playAgain.setVolume(id, voiceVolume * globalVolume);
    }

    // SFX
    public static void playClick() {
        long id = click.play();
        click.setVolume(id, sfxVolume * globalVolume);
    }

    //Gun
    public static void playSwitch(){
        long id = switchGun.play();
        switchGun.setVolume(id, gunVolume * globalVolume);
    }

    public static void playGunCry(){
        int random = MathUtils.random(0, 2);
        long id = gunCry[random].play();
        gunCry[random].setVolume(id, gunVolume * globalVolume);
    }

    public static void playGunLaugh(){
        int random = MathUtils.random(0, 2);
        long id = gunLaugh[random].play();
        gunLaugh[random].setVolume(id, gunVolume * globalVolume);
    }

    public static void playGunShot(){
        long id = gunShoot.play();
        gunShoot.setVolume(id, gunVolume * globalVolume);
    }

    public static void playReload(){
        long id = reload.play();
        reload.setVolume(id, gunVolume * globalVolume);
    }


    //Duck
    public static void playDuckFall(){
        int random = MathUtils.random(0, 2);
        long id = duckFall[random].play();
        duckFall[random].setVolume(id, duckVolume * globalVolume);
    }

    // ── Extra sounds (SFX category) ──────────────────────────────────────────
    private static Sound buttonPress;
    private static Sound buttonHover;

    public static void initializeExtraSounds(GameLauncher game) {
        buttonPress = game.manager.get("Sounds/button_press.mp3");
        buttonHover = game.manager.get("Sounds/button_hover.mp3");
    }

    public static void playButtonPress() {
        long id = buttonPress.play();
        buttonPress.setVolume(id, sfxVolume * globalVolume);
    }

    public static void playButtonHover() {
        long id = buttonHover.play();
        buttonHover.setVolume(id, sfxVolume * 0.6f * globalVolume);
    }

}
