package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.Entities.Cloud;
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.Entities.Egg;
import com.groupone.wtd.Entities.Gun;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Input.InputHandler;
import com.groupone.wtd.Utils.Utils;

public class NumHunt implements Screen {
    final float DUCK_SCALE = 0.8f;
    final int DUCK_SPAWN_SIZE = 10;
    private float cloudSpawnerCD = 1f;
    final float IMPACT_FRAME_CD = 0.05f;
    float impactFrameState = 1f;
    private float cloudSpawnerState = 0f;
    private Array<Cloud> clouds;
    private Rectangle shootableArea;

    PauseMenu pauseMenu;
    Gun gun;
    Egg egg;
    Array<Duck> ducks;
    GameLauncher game;
    Texture bush;
    Texture background;
    InputMultiplexer inputMultiplexer;
    InputHandler handler;
    Animation<TextureRegion> gunShotFrames;
    ShapeRenderer shapeRenderer;
    Vector2 gameCenter = new Vector2(0,0);
    Vector3 mouseOrigPos = new Vector3();
    Vector2 mousePos = new Vector2();

    public NumHunt(GameLauncher game){
        this.game = game;
        this.gun = new Gun(game);
        shootableArea = new Rectangle(0, GameLauncher.GROUND_HEIGHT, 1280, 720);
        clouds = new Array<>();
        egg = new Egg(game);
        shapeRenderer = new ShapeRenderer();
        handler = new InputHandler();
        pauseMenu = new PauseMenu(game);
        ducks = new Array<>(DUCK_SPAWN_SIZE);
        inputMultiplexer = new InputMultiplexer(pauseMenu.getStage(0), handler);
        Gdx.input.setInputProcessor(inputMultiplexer);
        gunShotFrames = new Animation<>(0.05f, Utils.generateSheet(game.manager.get("Guns/gun_shot.png"), 3, 7));
        bush = game.manager.get("Background/bush.png");
        background = game.manager.get("Background/background.png");
        spawnDucks();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        logic();
        input();
        draw();
    }

    private void logic(){
        if(pauseMenu.isExpanded) return;
        //randomly spawn clouds
        spawnClouds();
        //update cloud, duck, gun
        updateClouds();
        updateDucks();
        gun.updateState();
        egg.updateState();
        //handle impact frames
        updateImpactFrame();
    }

    private void input(){
        //detect clicks
        if(pauseMenu.isExpanded) return;
        handleClick();
        updateMousePos();
    }

    private void draw(){
        //setup initialization
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        //begin drawing
        game.batch.begin();
        //background
        game.batch.draw(background, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
        //ducks
        drawDucks();
        //bush
        game.batch.draw(bush, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
        //cloud
        drawClouds();
        //gun
        gun.getSprite(mousePos).draw(game.batch);
        //egg
        egg.getSprite().draw(game.batch);
        game.batch.end();

        if(pauseMenu.isExpanded || impactFrameState <= IMPACT_FRAME_CD){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            if(pauseMenu.isExpanded){
                expandPause();
            }

            if(impactFrameState <= IMPACT_FRAME_CD){
                drawImpactFrame();
            }

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        pauseMenu.getStage(Gdx.graphics.getDeltaTime()).draw();
    }

    //functions for clouds
    private void spawnClouds(){
        cloudSpawnerState += Gdx.graphics.getDeltaTime();
        if(cloudSpawnerState >= cloudSpawnerCD){
            clouds.add(new Cloud(game));
            cloudSpawnerCD = MathUtils.random(2, 5);
            cloudSpawnerState = 0f;
        }
    }

    private void updateClouds(){
        for(Cloud cloud : clouds){
            cloud.updateState();
        }
        for(int i = clouds.size - 1; i >= 0; i--){
            if(clouds.get(i).isRemovable){
                clouds.removeIndex(i);
            }
        }
    }

    private void drawClouds(){
        for(Cloud cloud : clouds){
            cloud.getSprite().draw(game.batch);
        }
    }

    //functions for ducks
    private void spawnDucks(){
        for(int i = 0; i < DUCK_SPAWN_SIZE; i++){
            ducks.add(new Duck(game));
        }
    }

    private void updateDucks(){
        for(int i = 0; i < ducks.size; i++){
            Duck duck = ducks.get(i);
            duck.updateState();
            if(duck.isRemovable){
                ducks.removeIndex(i);
            }
        }
    }

    private void detectDuckClick(){
        boolean isHit = false;
        for(Duck duck : ducks){
            if(duck.getHitBox().contains(mousePos)){
                gun.triggerCry();
                duck.handleShot();
                isHit = true;
            }
        }

        if(!isHit) gun.triggerLaugh();
    }

    private void drawDucks(){
        for(Duck duck : ducks){
            duck.getSprite().draw(game.batch);
        }
    }

    //function for clicks
    private void handleClick(){
        if(handler.wasClicked() && gun.reloadCD >= gun.RELOAD_TIME && shootableArea.contains(mousePos)){
            detectDuckClick();
            gun.triggerGunShot();
            egg.triggerEgg(mousePos.x, mousePos.y);
            impactFrameState = 0f;
        }
    }

    private void updateMousePos(){
        if(Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0){
            mouseOrigPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(mouseOrigPos);
            mousePos.set(mouseOrigPos.x, mouseOrigPos.y);
        }
    }

    //function for impact frames
    private void updateImpactFrame(){
        impactFrameState += Gdx.graphics.getDeltaTime();
    }

    private void drawImpactFrame(){
        shapeRenderer.setColor(1,1,1, 0.9f);
        shapeRenderer.rect(0,0,GameLauncher.gameWidth, GameLauncher.gameHeight);
    }

    private void expandPause(){
        shapeRenderer.setColor(0,0,0, 0.8f);
        shapeRenderer.rect(0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        gameCenter.x = game.viewport.getWorldWidth() / 2f;
        gameCenter.y = game.viewport.getWorldHeight() / 2f;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
