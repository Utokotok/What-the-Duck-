package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class Duck {
    private final float CHANGE_DIRECTION_CD = 1f;
    private float changeDirectionState = 0f;
    private final float MIN_SPEED = 400f;
    private final float MAX_SPEED = 600f;
    private Vector2 position;
    private Vector2 velocity;

    private Animation<TextureRegion> fallFrames;
    private Animation<TextureRegion> downFrames;
    private Animation<TextureRegion> upFrames;
    private Animation<TextureRegion> leftFrames;
    private Animation<TextureRegion> shockFrames;
    private Animation<TextureRegion> currentFrame;

    private boolean isShot = false;
    public boolean isRemovable = false;
    private Sprite sprite;
    private float duckH;
    private float duckW;
    private final float SCALE = 0.6f;
    private Rectangle hitBox;
    private float stateTime = 0f;
    private GameLauncher game;

    public Duck(GameLauncher game){
        this.game = game;
        this.hitBox = new Rectangle();
        this.sprite = new Sprite();
        position = new Vector2(0, 0);
        velocity = new Vector2();
        initializeAnimation();
        initializePosition();
        handleRandomDirection();
    }

    public void updateState(){
        stateTime += Gdx.graphics.getDeltaTime();
        updatePosition();
        updateRemovable();
        if(!isShot){
            handleRandomDirection();
            checkBorderCollision();
        }
    }

    public void handleShot(){
        isShot = true;
        velocity.x = 0;
        velocity.y = 0;
        currentFrame = shockFrames;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentFrame = fallFrames;
                velocity.y = -800f;
            }
        }, 0.5f);
    }

    private void updateRemovable(){
        if(position.y - 70 <= GameLauncher.GROUND_HEIGHT && isShot){
            isRemovable = true;
        }
    }
    private void initializeAnimation(){
        char randomColor = 'w';
        switch (MathUtils.random(1, 3)){
            case 1 -> randomColor = 'y';
            case 2-> randomColor = 'g';
            case 3 -> randomColor = 'w';
        }

        fallFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/" + randomColor + "_duck_fall.png"), 4, 1));
        downFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/" + randomColor +"_duck_down.png"), 2, 1));
        upFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/" + randomColor + "_duck_up.png"), 4, 1));
        leftFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/" + randomColor + "_duck_left.png"), 4, 1));
        shockFrames = new Animation<>(0.1f, Utils.generateSheet(game.manager.get("Ducks/" + randomColor +"_duck_shock.png"), 2, 1));
        currentFrame = downFrames;
        updateAnimation();
        updateSprite();
    }

    private void initializePosition(){
        boolean spawnLeft = MathUtils.randomBoolean();
        float randomY = MathUtils.random(GameLauncher.GROUND_HEIGHT, GameLauncher.gameHeight);
        position = new Vector2(spawnLeft ? 0 : GameLauncher.gameWidth - duckW, randomY);
    }

    private void handleRandomDirection(){
        if(changeDirectionState >= CHANGE_DIRECTION_CD){
            double random = Math.random();
            if(random <= 0.5) randomDirection();
        } else{
            changeDirectionState += Gdx.graphics.getDeltaTime();
        }
    }
    private void randomDirection(){
        float randX = MathUtils.random(-MAX_SPEED, MAX_SPEED);
        float randY = MathUtils.random(-MAX_SPEED, MAX_SPEED);
        randX = Math.max(Math.abs(randX), MIN_SPEED) * Math.signum(randX);
        randY = Math.max(Math.abs(randY), MIN_SPEED) * Math.signum(randY);
        velocity.y = randY;
        velocity.x = randX;
        changeDirectionState = 0;
    }

    private void updatePosition(){
        position.add(velocity.x * Gdx.graphics.getDeltaTime(), velocity.y * Gdx.graphics.getDeltaTime());
        hitBox.set(position.x, position.y, duckW, duckH);
    }

    private void updateAnimation(){
        currentFrame.setPlayMode(Animation.PlayMode.LOOP);
        if(isShot) return;
        if(MathUtils.atan2Deg(velocity.y, velocity.x) <= -65 && velocity.y < 0){
            currentFrame = downFrames;
        } else if(MathUtils.atan2Deg(velocity.y, velocity.x) >= 60 && velocity.y > 0) {
            currentFrame = upFrames;
        } else{
            currentFrame = leftFrames;
        }
        sprite.setFlip(!(velocity.x < 0), false);
    }

    private void updateSprite(){
        sprite.setRegion(currentFrame.getKeyFrame(stateTime));
        duckH = sprite.getRegionHeight() * SCALE;
        duckW = sprite.getRegionWidth() * SCALE;
        sprite.setSize(duckW, duckH);
        sprite.setPosition(position.x, position.y);
    }

    private void checkBorderCollision(){
        if(position.x <= 0){
            position.x = 0;
            randomDirection();
            velocity.x = Math.abs(velocity.x);
        }

        if(position.x + duckW >= GameLauncher.gameWidth){
            position.x = GameLauncher.gameWidth - duckW;
            randomDirection();
            velocity.x = -1 * Math.abs(velocity.x);
        }

        if(position.y + duckH >= GameLauncher.gameHeight){
            position.y = GameLauncher.gameHeight - duckH;
            randomDirection();
            velocity.y = -1 * Math.abs(velocity.y);
        }

        if(position.y <= GameLauncher.GROUND_HEIGHT){
            position.y = GameLauncher.GROUND_HEIGHT;
            randomDirection();
            velocity.y = Math.abs(velocity.y);
        }
    }

    public Rectangle getHitBox(){
        return hitBox;
    }

    public Sprite getSprite(){
        updateSprite();
        updateAnimation();
        return sprite;
    }

}
