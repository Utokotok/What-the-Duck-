package com.groupone.wtd.Screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    boolean isExpanded = false;

    public PauseMenu(GameLauncher game){
        this.game = game;
        stage = new Stage(game.viewport);
        quitButton = Utils.createButton(game.manager.get("Buttons/quit.png"), GameLauncher.gameWidth / 2f, (GameLauncher.gameHeight / 2f), QUIT_SCALE);
        pauseButton = Utils.createButton(game.manager.get("Buttons/pause.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight / 2f, PAUSE_SCALE);
        continueButton = Utils.createButton(game.manager.get("Buttons/continue.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight / 2f, CONTINUE_SCALE);
        pauseButton.setPosition(GameLauncher.gameWidth - (pauseButton.getWidth() * PAUSE_SCALE + 10), 10);
        continueButton.setPosition(GameLauncher.gameWidth / 2f - ((continueButton.getWidth() * QUIT_SCALE) / 2f), (GameLauncher.gameHeight / 2f) - 10);
        quitButton.setPosition(GameLauncher.gameWidth / 2f - (quitButton.getWidth() * QUIT_SCALE) / 2f, (GameLauncher.gameHeight / 2f) - quitButton.getHeight() * QUIT_SCALE - 30);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
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
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenu(game));
            }
        });
        stage.addActor(pauseButton);
        stage.addActor(continueButton);
        stage.addActor(quitButton);
    }

    public Stage getStage(float delta){
        pauseButton.setVisible(!isExpanded);
        continueButton.setVisible(isExpanded);
        quitButton.setVisible(isExpanded);
        stage.act(delta);
        return stage;
    }

}
