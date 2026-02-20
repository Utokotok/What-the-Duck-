package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class Gun {
    public float reloadCD = 0f;
    public final float RELOAD_TIME = 0.7f;
    float gunShotStateTime = 0.7f;
    Sprite gunSprite;
    Sprite modeSprite;

    public int gunMode = 1;
    public int[] availableGunMode = {2,2,2,2};
    private Animation<TextureRegion> gunModeFrames;
    private float gunModeState = 0f;

    Animation<TextureRegion> divisionFrames;
    Animation<TextureRegion> productFrames;
    Animation<TextureRegion> minusFrames;
    Animation<TextureRegion> sumFrames;
    Animation<TextureRegion> changeFrames;
    Animation<TextureRegion> currentMode;

    Animation<TextureRegion> currentGun;
    Animation<TextureRegion> shotFrames;
    Animation<TextureRegion> cryFrames;
    Animation<TextureRegion> laughFrames;


    public Gun(GameLauncher game){
        gunSprite = new Sprite();
        modeSprite = new Sprite();
        shotFrames = new Animation<>(0.033f, Utils.generateSheet(game.manager.get("Guns/gun_shot.png"), 3, 7));
        cryFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Guns/gun_crying.png"), 2, 1));
        laughFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Guns/gun_laughing.png"), 2, 1));
        divisionFrames = new Animation<>(0.5f, Utils.generateSheet(game.manager.get("Guns/division.png"), 2, 1));
        sumFrames = new Animation<>(0.5f, Utils.generateSheet(game.manager.get("Guns/sum.png"), 2, 1));
        minusFrames = new Animation<>(0.5f, Utils.generateSheet(game.manager.get("Guns/minus.png"), 2, 1));
        productFrames = new Animation<>(0.5f, Utils.generateSheet(game.manager.get("Guns/product.png"), 2, 1));
        changeFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Guns/change_mode.png"), 4, 1));
        currentMode = changeFrames;

        divisionFrames.setPlayMode(Animation.PlayMode.LOOP);
        sumFrames.setPlayMode(Animation.PlayMode.LOOP);
        minusFrames.setPlayMode(Animation.PlayMode.LOOP);
        productFrames.setPlayMode(Animation.PlayMode.LOOP);

        currentGun = shotFrames;
    }

    public void reloadGun(){
        for(int i = 0; i < 4; i++){
            availableGunMode[i] = MathUtils.clamp(availableGunMode[i] + 2, 0, 5);
        }
    }

    public boolean checkIfOutOfAmmo(){
        boolean isOut = true;
        for(int i = 0; i < 4; i++){
            if(availableGunMode[i] != 0){
                isOut = false;
                break;
            }
        }

        return isOut;
    }

    public boolean checkAvailableAmmo(int mode){
        return availableGunMode[mode - 1] != 0;
    }

    public void consumeAmmo(int mode){
        availableGunMode[mode - 1] -= 1;
    }

    public void updateState(){
        reloadCD += Gdx.graphics.getDeltaTime();
        gunShotStateTime += Gdx.graphics.getDeltaTime();
        gunModeState += Gdx.graphics.getDeltaTime();
    }

    public void triggerGunShot(){
        SoundManager.playGunShot();
        currentGun = shotFrames;
        currentGun.setPlayMode(Animation.PlayMode.NORMAL);
        reloadCD = 0f;
        gunShotStateTime = 0f;
    }

    public void triggerCry(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                SoundManager.playGunCry();
                currentGun = cryFrames;
                currentGun.setPlayMode(Animation.PlayMode.LOOP);
            }
        }, 0.7f);
    }

    public void triggerLaugh(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                SoundManager.playGunLaugh();
                currentGun = laughFrames;
                currentGun.setPlayMode(Animation.PlayMode.LOOP);
            }
        }, 0.7f);
    }

    public Sprite getSprite(Vector2 mousePos){
        TextureRegion frame = currentGun.getKeyFrame(gunShotStateTime);
        gunSprite.setRegion(frame);
        gunSprite.setOrigin(frame.getRegionWidth() / 2f, -80);
        gunSprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());
        gunSprite.setPosition((GameLauncher.gameWidth / 2f) - frame.getRegionWidth() / 2f, -10);
        gunSprite.setRotation(Math.clamp(MathUtils.atan2Deg(mousePos.y, mousePos.x - GameLauncher.gameWidth / 2f) - 90, -50, 50));
        return gunSprite;
    }

    public void changeMode(int mode){
        SoundManager.playSwitch();
        gunModeState = 0f;
        gunMode = mode;
    }

    public Sprite getModeSprite(Vector2 mousePos){
        if(gunModeState < 0.4){
            currentMode = changeFrames;
        } else{
            switch(gunMode){
                case 1 -> {currentMode = sumFrames;}
                case 2 -> {currentMode = minusFrames;}
                case 3 -> {currentMode = productFrames;}
                case 4 -> {currentMode = divisionFrames;}
            }
        }
        TextureRegion frame = currentMode.getKeyFrame(gunModeState);
        modeSprite.setRegion(frame);
        modeSprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());
        modeSprite.setPosition(60 + GameLauncher.gameWidth / 2f, 250);
        modeSprite.setOrigin((frame.getRegionWidth() / 2f) - 60, -340f);
        modeSprite.setRotation(Math.clamp(MathUtils.atan2Deg(mousePos.y, mousePos.x - GameLauncher.gameWidth / 2f) - 90, -50, 50));

        return modeSprite;
    }
}
