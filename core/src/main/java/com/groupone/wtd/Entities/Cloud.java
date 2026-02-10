package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.groupone.wtd.GameLauncher;

public class Cloud {
    private final float MIN_SPEED = 50f;
    private final float MAX_SPEED = 200f;
    public boolean isRemovable = false;
    float cloudH;
    float cloudW;
    float opacity;
    Sprite sprite;
    Vector2 position;
    Vector2 velocity;

    public Cloud(GameLauncher game){
        int randomCloud = (int) MathUtils.random(1, 5);
        float scale = MathUtils.random(0.5f, 1.2f);
        opacity = MathUtils.random(0.9f, 1f);
        sprite = new Sprite((Texture) game.manager.get("Clouds/cloud_" + randomCloud + ".png"));
        cloudH = sprite.getRegionHeight() * scale;
        cloudW = sprite.getRegionWidth() * scale;
        position = new Vector2(- 2 * cloudW, MathUtils.random(270, GameLauncher.gameHeight - cloudH));
        velocity = new Vector2(MathUtils.random(MIN_SPEED, MAX_SPEED), 0);
        sprite.setSize(cloudW, cloudH);
        sprite.setAlpha(opacity);
    }

    public void updateState(){
        if(position.x > GameLauncher.gameWidth + cloudW){
            isRemovable = true;
        }
        position.add(velocity.x * Gdx.graphics.getDeltaTime(), 0);
    }

    public Sprite getSprite(){
        sprite.setPosition(position.x, position.y);
        return sprite;
    }
}
