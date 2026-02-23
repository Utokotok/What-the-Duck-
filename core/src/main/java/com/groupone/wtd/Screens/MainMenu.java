package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.Assets.AnimationManager;
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Input.InputHandler;
import com.groupone.wtd.Utils.Utils;

public class MainMenu implements Screen {

    boolean isAssetsLoading = true;
    boolean isAnimationDone = false;
    GameLauncher game;
    ImageButton numHuntButton;
    ImageButton wordHuntButton;
    ImageButton quitButton;
    ImageButton aboutButton;
    Sound buttonPressSound;
    Sound buttonHoverSound;
    Stage stage;
    InputMultiplexer inputMultiplexer;
    Animation<TextureRegion> logoFrames;
    Image logoIntro;

    Vector2 gameCenter = new Vector2(0,0);
    Texture bush;
    Texture background;
    float elapsedTime = 0f;
    float animationDelay = 2f;
    float logoH;
    float logoW;

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

    private void playBackgroundMusic(){
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                SoundManager.setMainMenu();
                SoundManager.playBackgroundMusic();
                SoundManager.playBlox();
            }
        }, GameLauncher.isMainMenuAnimation ? 0f : animationDelay + 0.8f);
    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        TextureRegion logoFrame = logoFrames.getKeyFrame(elapsedTime);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        if(GameLauncher.isMainMenuAnimation){
            game.batch.draw(background, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
            game.batch.draw(bush, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
            game.batch.draw(logoFrame, 500, gameCenter.y - logoH / 2f, logoW, logoH);
        }
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void input(){

    }

    private void loadAssets(){
        if(game.manager.update()){
            isAssetsLoading = false;
            bush = game.manager.get("Background/bush.png");
            background = game.manager.get("Background/background.png");
            logoFrames = new Animation<>(0.2f, Utils.generateSheet(game.manager.get("Logo/wtd_logo.png"), 6, 1));
            logoFrames.setPlayMode(Animation.PlayMode.LOOP);
            logoW = logoFrames.getKeyFrame(0).getRegionWidth() * 1.8f;
            logoH = logoFrames.getKeyFrame(0).getRegionHeight() * 1.8f;
            logoIntro = new Image(logoFrames.getKeyFrame(0));
            logoIntro.setSize(logoW, logoH);
            logoIntro.setPosition(gameCenter.x - logoW / 2f - 20f, 2000);
            initializeButtons();
            AnimationManager.initializeDucks();
            SoundManager.initializeSound(game);
            playBackgroundMusic();
        }
    }

    private void drawFlash(){
        stage.addActor(Assets.getFlash());
    }

    private void initializeButtons(){
        wordHuntButton = Utils.createButton(game.manager.get("Buttons/word_hunt.png"), 150, 1500, 0.3f, false);
        numHuntButton = Utils.createButton(game.manager.get("Buttons/num_hunt.png"), 150, 1500, 0.3f, false);
        aboutButton = Utils.createButton(game.manager.get("Buttons/about.png"), 150, 1500, 0.3f, false);
        quitButton = Utils.createButton(game.manager.get("Buttons/quit.png"), 150, 1500, 0.3f, false);

        wordHuntButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playWordHunt();
                }
            }
        });

        numHuntButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playNumHunt();
                }
            }
        });

        aboutButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playAbout();
                }
            }
        });

        quitButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playQuit();
                }
            }
        });

        if(!GameLauncher.isMainMenuAnimation){
            logoIntro.addAction(
                Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            SoundManager.playLogo();
                        }
                    }),
                    Actions.moveTo(gameCenter.x - logoW / 2f - 20f, gameCenter.y - logoH / 2f, 0.8f, Interpolation.bounceOut),
                    Actions.delay(animationDelay),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            GameLauncher.isMainMenuAnimation = true;
                            drawFlash();
                        }
                    }),
                    Actions.removeActor()
                )
            );

            wordHuntButton.addAction(
                Actions.sequence(
                    Actions.delay(animationDelay + 0.75f),
                    Actions.moveTo(150, 525, 0.2f, Interpolation.pow2In)
                )
            );
            numHuntButton.addAction(
                Actions.sequence(
                    Actions.delay(animationDelay + 1f),
                    Actions.moveTo(150, 375, 0.2f, Interpolation.pow2In)
                )
            );

            aboutButton.addAction(
                Actions.sequence(
                    Actions.delay(animationDelay + 1.25f),
                    Actions.moveTo(150, 225, 0.2f, Interpolation.pow2In)
                )
            );
            quitButton.addAction(
                Actions.sequence(
                    Actions.delay(animationDelay + 1.50f),
                    Actions.moveTo(150, 75, 0.2f, Interpolation.pow2In),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationDone = true;
                        }
                    })
                )
            );
        } else{
            wordHuntButton.addAction(
                Actions.sequence(
                    Actions.moveTo(150, 525, 0.2f, Interpolation.pow2In)
                )
            );
            numHuntButton.addAction(
                Actions.sequence(
                    Actions.delay(0.25f),
                    Actions.moveTo(150, 375, 0.2f, Interpolation.pow2In)
                )
            );

            aboutButton.addAction(
                Actions.sequence(
                    Actions.delay(0.50f),
                    Actions.moveTo(150, 225, 0.2f, Interpolation.pow2In)
                )
            );
            quitButton.addAction(
                Actions.sequence(
                    Actions.delay(0.75f),
                    Actions.moveTo(150, 75, 0.2f, Interpolation.pow2In),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationDone = true;
                        }
                    })
                )
            );

        }

        numHuntButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new NumHunt(game));
                SoundManager.stopBackgroundMusic();
            }
        });

        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        stage.addActor(logoIntro);
        stage.addActor(aboutButton);
        stage.addActor(wordHuntButton);
        stage.addActor(numHuntButton);
        stage.addActor(quitButton);
    }
}
