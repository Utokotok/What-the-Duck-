package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.groupone.wtd.Utils.Utils;

public class GameOver {
    Stage stage;
    GameLauncher game;
    MainGame currGame;
    boolean isGameOver = false;
    float stateTime = 0f;
    Label gameOverText;
    Label pointsText;
    ImageButton tryAgain;
    ImageButton quit;
    boolean isAnimationDone = false;


    public GameOver(GameLauncher game, MainGame currGame){
        this.game = game;
        this.currGame = currGame;
        tryAgain = Utils.createButton(game.manager.get("Buttons/try_again.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight + 100, 0.28f, true);
        quit = Utils.createButton(game.manager.get("Buttons/quit.png"), GameLauncher.gameWidth / 2f, GameLauncher.gameHeight + 100, 0.28f, true);
        stage = new Stage(game.viewport);
        gameOverText = new Label("GAME OVER", new Label.LabelStyle(game.UIFont1, Color.WHITE));
        gameOverText.setPosition(GameLauncher.gameWidth / 2f - gameOverText.getWidth() / 2f, GameLauncher.gameHeight);
        gameOverText.addAction(Actions.moveTo(GameLauncher.gameWidth / 2f - gameOverText.getWidth() / 2f, GameLauncher.gameHeight / 2f - gameOverText.getHeight() / 2f + 150, 0.5f, Interpolation.bounceOut));
        pointsText = new Label("Your scored " + currGame.points + " points!", new Label.LabelStyle(game.UIFont2, Color.WHITE));
        pointsText.setWidth(GameLauncher.gameWidth);
        pointsText.setAlignment(Align.center);
        pointsText.setPosition(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, GameLauncher.gameHeight + 100);
        pointsText.addAction(Actions.sequence(
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    SoundManager.playGameOver();
                }
            }),
            Actions.delay(0.25f),
            Actions.moveTo(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, 390, 1f, Interpolation.bounceOut)
        ));
        tryAgain.addAction(Actions.sequence(
            Actions.delay(1.5f),
            Actions.moveTo(tryAgain.getX(),  250, 0.25f, Interpolation.bounceOut)
        ));
        quit.addAction(Actions.sequence(
            Actions.delay(1.75f),
            Actions.moveTo(quit.getX(),  110, 0.25f, Interpolation.bounceOut),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    isAnimationDone = true;
                }
            })
        ));
        tryAgain.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new NumHunt(game));
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playPlayAgain();
                }
            }
        });


        quit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenu(game));
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playQuit();
                }
            }
        });

        stage.addActor(tryAgain);
        stage.addActor(quit);
        stage.addActor(gameOverText);
        stage.addActor(pointsText);
    }

    public void updateGameOver(float delta){
        stateTime += Gdx.graphics.getDeltaTime();
        stage.act(delta);
    }

    public Stage getStage(){
        pointsText.setText("Your scored " + currGame.points + " points!");
        return stage;
    }



}
