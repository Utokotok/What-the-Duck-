package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class PauseMenu {
    private final float PAUSE_SCALE = 0.08f;
    private final float CONTINUE_SCALE = 0.3f;
    private final float QUIT_SCALE = 0.3f;

    Stage stage;
    GameLauncher game;
    ImageButton quitButton;
    ImageButton pauseButton;
    ImageButton continueButton;
    MainGame currGame;
    public boolean isExpanded = false;

    public PauseMenu(GameLauncher game, MainGame currGame){
        this.game = game;
        this.currGame = currGame;
        stage = new Stage(game.viewport);
        quitButton = Utils.createButton(game.manager.get("Buttons/quit.png"), GameLauncher.gameWidth / 2f, (GameLauncher.gameHeight / 2f), QUIT_SCALE, true);
        pauseButton = Utils.createButton(game.manager.get("Buttons/pause.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight / 2f, PAUSE_SCALE, true);
        continueButton = Utils.createButton(game.manager.get("Buttons/continue.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight / 2f, CONTINUE_SCALE, true);
        pauseButton.setPosition(GameLauncher.gameWidth - (pauseButton.getWidth() * PAUSE_SCALE + 10), 10);
        continueButton.setPosition(GameLauncher.gameWidth / 2f - ((continueButton.getWidth() * QUIT_SCALE) / 2f), (GameLauncher.gameHeight / 2f) - 10);
        quitButton.setPosition(GameLauncher.gameWidth / 2f - (quitButton.getWidth() * QUIT_SCALE) / 2f, (GameLauncher.gameHeight / 2f) - quitButton.getHeight() * QUIT_SCALE - 30);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE && !currGame.isGameOver) {
                    isExpanded = !isExpanded;
                    return true;
                }
                return false;
            }
        });

        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isExpanded = true;
            }
        });

        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isExpanded = false;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1) {
                    SoundManager.playContinue();
                }
            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenu(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1) {
                    SoundManager.playQuit();
                }
            }
        });
        stage.addActor(continueButton);
        stage.addActor(quitButton);
    }

    public void update(){
        pauseButton.setVisible(!isExpanded);
        continueButton.setVisible(isExpanded);
        quitButton.setVisible(isExpanded);
    }

    public Stage getStage(){
        stage.act(Gdx.graphics.getDeltaTime());
        return stage;
    }

}
