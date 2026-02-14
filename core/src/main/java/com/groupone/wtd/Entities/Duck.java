package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.Assets.AnimationManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class Duck {
    private float changeDirectionCD = 0.5f;
    private float changeDirectionChance = 0.5f;
    private float minSpeed = 200f;
    private float maxSpeed = 600f;

    private float changeDirectionState = 0f;
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
    private final float SCALE = 0.5f;
    private Rectangle hitBox;
    private float stateTime = 0f;
    private GameLauncher game;
    private int number;
    private char letter;

    public Duck(GameLauncher game, float minSpeed, float maxSpeed, float changeDirectionCD, float changeDirectionChance){
        this.game = game;
        this.hitBox = new Rectangle();
        this.sprite = new Sprite();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.changeDirectionCD = changeDirectionCD;
        this.changeDirectionChance = changeDirectionChance;
        position = new Vector2(0, 0);
        velocity = new Vector2();
        initializeAnimation();
        initializePosition();
        handleRandomDirection();
    }

    public Duck(GameLauncher game, float minSpeed, float maxSpeed, float changeDirectionCD, float changeDirectionChance, int number){
        this.game = game;
        this.hitBox = new Rectangle();
        this.number = number;
        this.sprite = new Sprite();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.changeDirectionCD = changeDirectionCD;
        this.changeDirectionChance = changeDirectionChance;
        position = new Vector2(0, 0);
        velocity = new Vector2();
        initializeAnimation();
        initializePosition();
        handleRandomDirection();
    }

    public Duck(GameLauncher game, float minSpeed, float maxSpeed, float changeDirectionCD, float changeDirectionChance, char letter){
        this.game = game;
        this.hitBox = new Rectangle();
        this.letter = letter;
        this.sprite = new Sprite();
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.changeDirectionCD = changeDirectionCD;
        this.changeDirectionChance = changeDirectionChance;
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

    public int getNumber(){
        return number;
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
        Array<Animation<TextureRegion>> duckAnimation = null;
        switch (MathUtils.random(1, 3)){
            case 1 -> {duckAnimation = AnimationManager.wDuck;}
            case 2-> {duckAnimation = AnimationManager.gDuck;}
            case 3 -> {duckAnimation = AnimationManager.yDuck;}
        }

        fallFrames = duckAnimation.get(0);
        downFrames = duckAnimation.get(1);
        upFrames = duckAnimation.get(2);
        leftFrames = duckAnimation.get(3);
        shockFrames = duckAnimation.get(4);
        currentFrame = downFrames;
        updateAnimation();
        updateSprite();
    }

    private void initializePosition(){
        boolean spawnLeft = MathUtils.randomBoolean();
        position = new Vector2(spawnLeft ? 0 : GameLauncher.gameWidth - duckW, GameLauncher.GROUND_HEIGHT);
    }

    private void handleRandomDirection(){
        if(changeDirectionState >= changeDirectionCD){
            double random = Math.random();
            if(random <= changeDirectionChance) randomDirection();
        } else{
            changeDirectionState += Gdx.graphics.getDeltaTime();
        }
    }
    private void randomDirection(){
        float randX = MathUtils.random(-maxSpeed, maxSpeed);
        float randY = MathUtils.random(-maxSpeed, maxSpeed);
        randX = Math.max(Math.abs(randX), minSpeed) * Math.signum(randX);
        randY = Math.max(Math.abs(randY), minSpeed) * Math.signum(randY);
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
