package com.groupone.wtd.Entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class Egg {
    Animation<TextureRegion> eggFrames;
    Sprite sprite;
    float eggStateTime = 0f;
    float eggFadeState = 1f;
    float eggW = 125f;
    float eggH = 125f;
    float x;
    float y;

    public Egg(GameLauncher game){
        eggFrames = new Animation<>(0.07f, Utils.generateSheet(game.manager.get("Guns/egg_shot.png"), 13, 1));
        sprite = new Sprite();
    }

    public void updateState(){
        eggStateTime += Gdx.graphics.getDeltaTime();
        if(eggStateTime >= 0.65f){
            eggFadeState -= Gdx.graphics.getDeltaTime();
            eggFadeState = Math.max(eggFadeState, 0);
        }
    }

    public void triggerEgg(float x, float y){
        System.out.println("dadwa");
        eggStateTime = 0f;
        eggFadeState = 1f;
        this.x = x;
        this.y = y;
    }

    public Sprite getSprite(){
        TextureRegion eggFrame = eggFrames.getKeyFrame(eggStateTime);
        sprite.setRegion(eggFrame);
        sprite.setPosition(x - eggW / 2f, y - eggH / 2f);
        sprite.setSize(eggW, eggH);
        sprite.setAlpha(eggFadeState);
        return sprite;
    }
}
