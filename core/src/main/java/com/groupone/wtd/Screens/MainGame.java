package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.Entities.Cloud;
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.Entities.Egg;
import com.groupone.wtd.Entities.Gun;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Input.InputHandler;
import com.groupone.wtd.Utils.Utils;

abstract class MainGame implements Screen {

    final int DUCK_SPAWN_SIZE = 5;
    private float cloudSpawnerCD = 1f;
    private float cloudSpawnerState = 0f;
    private Array<Cloud> clouds;
    private Rectangle shootableArea;
    boolean isGameOver = false;
    int points = 0;
    PauseMenu pauseMenu;
    Gun gun;
    Egg egg;
    Array<Duck> ducks;
    GameLauncher game;
    Texture bush;
    Texture background;
    InputMultiplexer inputMultiplexer;
    InputHandler handler;
    GameOver gameOver;
    Animation<TextureRegion> gunShotFrames;
    ShapeRenderer shapeRenderer;
    Vector2 gameCenter = new Vector2(0,0);
    Vector3 mouseOrigPos = new Vector3();
    Vector2 mousePos = new Vector2();
    Stage flashStage;
    protected abstract void customLogic();
    protected abstract void customInput();
    protected abstract void drawCustomMidGround();
    protected abstract void drawCustomForeground();
    protected abstract void customClick();
    protected abstract boolean isClickable();
    protected abstract void customShape();
    protected abstract void customFailHit();

    public MainGame(GameLauncher game){
        this.game = game;
        this.gun = new Gun(game);
        gameOver = new GameOver(game, this);
        shootableArea = new Rectangle(0, GameLauncher.GROUND_HEIGHT, 1280, 720);
        clouds = new Array<>();
        egg = new Egg(game);
        flashStage = new Stage(game.viewport);
        shapeRenderer = new ShapeRenderer();
        handler = new InputHandler();
        pauseMenu = new PauseMenu(game, this);
        ducks = new Array<>(DUCK_SPAWN_SIZE);
        inputMultiplexer = new InputMultiplexer(pauseMenu.getStage(), gameOver.getStage(), handler);
        Gdx.input.setInputProcessor(inputMultiplexer);
        gunShotFrames = new Animation<>(0.05f, Utils.generateSheet(game.manager.get("Guns/gun_shot.png"), 3, 7));
        bush = game.manager.get("Background/bush.png");
        background = game.manager.get("Background/background.png");
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
        pauseMenu.update();
        if(pauseMenu.isExpanded) return;
        if(isGameOver) gameOver.updateGameOver(Gdx.graphics.getDeltaTime());
        //randomly spawn clouds
//        spawnClouds();
        //update cloud, duck, gun
//        updateClouds();
        flashStage.act(Gdx.graphics.getDeltaTime());
        updateDucks();
        customLogic();
        gun.updateState();
        egg.updateState();
    }

    private void input(){
        //detect clicks
        if(pauseMenu.isExpanded) return;
        handleClick();
        updateMousePos();
        customInput();
    }

    private void drawForeground(){
        //bush
        game.batch.draw(bush, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
        //gun
        gun.getSprite(mousePos).draw(game.batch);
        //cloud
//        drawClouds();
        //egg
        drawCustomForeground();
        egg.getSprite().draw(game.batch);
    }

    private void drawMidGround(){
        //ducks
        drawDucks();
        drawCustomMidGround();
    }

    private void drawBackground(){
        //background
        game.batch.draw(background, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
    }

    private void draw(){
        //setup initialization
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        //begin drawing
        game.batch.begin();
        drawBackground();
        drawMidGround();
        drawForeground();
        game.batch.end();
        customShape();

        if(pauseMenu.isExpanded ||  isGameOver){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            if(pauseMenu.isExpanded || isGameOver){
                expandPause();
            }

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if(isGameOver){
            gameOver.getStage().draw();
        }
        flashStage.draw();
        pauseMenu.getStage().draw();
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
    protected abstract void spawnDucks();

    private void updateDucks(){
        for(int i = ducks.size - 1; i >= 0; i--){
            Duck duck = ducks.get(i);
            duck.updateState();
            if(duck.isRemovable){
                ducks.removeIndex(i);
            }
        }
    }

    protected abstract void customDuckHit(Duck duck);

    private void detectDuckClick(){
        boolean isHit = false;
        for(Duck duck : ducks){
            if(duck.getHitBox().contains(mousePos)){
                SoundManager.playApplause();
                customDuckHit(duck);
                gun.triggerCry();
                duck.handleShot();
                isHit = true;
            }
        }

        if(!isHit){
            SoundManager.playDisappoint();
            gun.triggerLaugh();
            customFailHit();
        }
    }

    private void drawDucks(){
        for(Duck duck : ducks){
            duck.getSprite().draw(game.batch);
        }
    }

    //function for clicks
    private void handleClick(){
        if(handler.wasClicked() && gun.reloadCD >= gun.RELOAD_TIME && shootableArea.contains(mousePos)){
            if(isGameOver) return;
            if(!isClickable()){
                SoundManager.playReload();
                return;
            }

            customClick();
            detectDuckClick();
            gun.triggerGunShot();
            flashStage.addActor(Assets.getFlash());
            egg.triggerEgg(mousePos.x, mousePos.y);
        }
    }

    private void updateMousePos(){
        if(Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0){
            mouseOrigPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(mouseOrigPos);
            mousePos.set(mouseOrigPos.x, mouseOrigPos.y);
        }
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
