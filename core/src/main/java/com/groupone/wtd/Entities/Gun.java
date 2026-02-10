package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class Gun {
    public float reloadCD = 0f;
    public final float RELOAD_TIME = 0.7f;
    private final float EMOTE_CD = 0.5f;
    boolean isEmoting = false;
    float gunShotStateTime = 0.7f;
    float emoteStateTime = 0.5f;
    Sprite sprite;
    Animation<TextureRegion> currentFrames;
    Animation<TextureRegion> shotFrames;
    Animation<TextureRegion> cryFrames;
    Animation<TextureRegion> laughFrames;


    public Gun(GameLauncher game){
        sprite = new Sprite();
        shotFrames = new Animation<>(0.033f, Utils.generateSheet(game.manager.get("Guns/gun_shot.png"), 3, 7));
        cryFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Guns/gun_crying.png"), 2, 1));
        laughFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Guns/gun_laughing.png"), 2, 1));
        currentFrames = shotFrames;
    }

    public void updateState(){
        reloadCD += Gdx.graphics.getDeltaTime();
        gunShotStateTime += Gdx.graphics.getDeltaTime();
    }

    public void triggerGunShot(){
        currentFrames = shotFrames;
        currentFrames.setPlayMode(Animation.PlayMode.NORMAL);
        reloadCD = 0f;
        gunShotStateTime = 0f;
    }

    public void triggerCry(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentFrames = cryFrames;
                currentFrames.setPlayMode(Animation.PlayMode.LOOP);
            }
        }, 0.7f);
    }

    public void triggerLaugh(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentFrames = laughFrames;
                currentFrames.setPlayMode(Animation.PlayMode.LOOP);
            }
        }, 0.7f);
    }

    public Sprite getSprite(Vector2 mousePos){
        TextureRegion frame = currentFrames.getKeyFrame(gunShotStateTime);
        sprite.setRegion(frame);
        sprite.setOrigin(frame.getRegionWidth() / 2f, -80);
        sprite.setSize(frame.getRegionWidth(), frame.getRegionHeight());
        sprite.setPosition((GameLauncher.gameWidth / 2f) - frame.getRegionWidth() / 2f, -10);
        sprite.setRotation(Math.clamp(MathUtils.atan2Deg(mousePos.y, mousePos.x - GameLauncher.gameWidth / 2f) - 90, -50, 50));
        return sprite;
    }
}
