package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Input.InputHandler;
import com.groupone.wtd.Utils.Utils;

public class MainMenu implements Screen {

    boolean isAssetsLoading = true;
    GameLauncher game;
    ImageButton numHuntButton;
    ImageButton wordHuntButton;
    ImageButton quitButton;
    Sound buttonPressSound;
    Sound buttonHoverSound;
    Stage stage;
    InputMultiplexer inputMultiplexer;
    Animation<TextureRegion> logoFrames;
    Vector2 gameCenter = new Vector2(0,0);
    Texture bush;
    Texture background;
    float elapsedTime = 0f;

    public MainMenu(GameLauncher game){
        this.game = game;
        stage = new Stage(game.viewport);
        inputMultiplexer = new InputMultiplexer(stage, new InputHandler());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(isAssetsLoading){
            loadAssets();
        } else{
            draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
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

    private void logic(){

    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        TextureRegion logoFrame = logoFrames.getKeyFrame(elapsedTime);
        float logoH = logoFrame.getRegionHeight() * 1.4f;
        float logoW = logoFrame.getRegionWidth() * 1.4f;
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, game.gameWidth, game.gameHeight);
        game.batch.draw(bush, 0, 0, game.gameWidth, game.gameHeight);
        game.batch.draw(logoFrame, (gameCenter.x - 20) - logoW / 2f, 375, logoW, logoH);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void input(){

    }

    private void loadAssets(){
        if(game.manager.update()){
            isAssetsLoading = false;
            buttonHoverSound = game.manager.get("Sounds/button_hover.mp3");
            buttonPressSound = game.manager.get("Sounds/button_press.mp3");
            bush = game.manager.get("Background/bush.png");
            background = game.manager.get("Background/background.png");
            logoFrames = new Animation<>(0.2f, Utils.generateSheet(game.manager.get("Logo/wtd_logo.png"), 6, 1));
            logoFrames.setPlayMode(Animation.PlayMode.LOOP);
            initializeButtons();
        }
    }

    private void initializeButtons(){
        wordHuntButton = Utils.createButton(game.manager.get("Buttons/word_hunt.png"), gameCenter.x, gameCenter.y - 70, 0.2f);
        numHuntButton = Utils.createButton(game.manager.get("Buttons/num_hunt.png"), gameCenter.x, gameCenter.y - 180, 0.2f);
        quitButton = Utils.createButton(game.manager.get("Buttons/quit.png"), gameCenter.x, gameCenter.y - 290, 0.2f);

        numHuntButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new NumHunt(game));
            }
        });

        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        stage.addActor(wordHuntButton);
        stage.addActor(numHuntButton);
        stage.addActor(quitButton);
    }
}
