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
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.groupone.wtd.Utils.Utils;

public class GameOver {
    Stage stage;
    GameLauncher game;
    MainGame currGame;
    String[][] reasons = {
        {"The ducks outlasted your ammo.", "Should've aimed first.", "You shot all! The ducks are fine.", "Reloading... wait what!?", "Who taught ducks to dodge!?"},
        {"Where are the ducks?", "The ducks has left the chat.", "No ducks. Well done.", "Ducks saw you and left.", "You cleared the pond."},
        {"Donald got no more time for you.", "Check bottom-left next \"time\".", "My grandpa aims faster.", "Clocks don't walk, y'know!", "Think fast unc!"}
    };
    String reason = "How'd that happen!?";
    boolean isGameOver = false;
    float stateTime = 0f;
    float animationDelay = 1f;
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
        gameOverText = new Label(reason, new Label.LabelStyle(game.UIFont1, Color.WHITE));
        pointsText = new Label("Your scored " + currGame.points + " points!", new Label.LabelStyle(game.UIFont2, Color.WHITE));
        pointsText.setWidth(GameLauncher.gameWidth);
        pointsText.setAlignment(Align.center);
        gameOverText.setWidth(GameLauncher.gameWidth);
        gameOverText.setAlignment(Align.center);
        gameOverText.setPosition(GameLauncher.gameWidth / 2f - gameOverText.getWidth() / 2f, GameLauncher.gameHeight);
        pointsText.setPosition(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, GameLauncher.gameHeight + 100);
        stage.addActor(Assets.getFlash());
        gameOverText.addAction(Actions.sequence(
            Actions.delay(animationDelay),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    stage.addActor(Assets.getFlash());
                }
            }),
            Actions.moveTo(GameLauncher.gameWidth / 2f - gameOverText.getWidth() / 2f, GameLauncher.gameHeight / 2f - gameOverText.getHeight() / 2f + 150, 0.5f, Interpolation.bounceOut)
        ));
        pointsText.addAction(Actions.sequence(
            Actions.delay(animationDelay),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    SoundManager.playGameOver();
                }
            }),
            Actions.delay(animationDelay),
            Actions.moveTo(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, 390, 1f, Interpolation.bounceOut)
        ));
        tryAgain.addAction(Actions.sequence(
            Actions.delay(animationDelay + 2.5f),
            Actions.moveTo(tryAgain.getX(),  250, 0.25f, Interpolation.bounceOut)
        ));
        quit.addAction(Actions.sequence(
            Actions.delay(animationDelay + 2.75f),
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

    public void setReason(int reason){
        this.reason = reasons[reason][MathUtils.random(0, 4)];
        gameOverText.setText(this.reason);
    }

    public void updateGameOver(float delta){
        stateTime += Gdx.graphics.getDeltaTime();
        stage.act(delta);
    }

    public Stage getStage(){
        pointsText.setText("You scored " + currGame.points + " points!");
        return stage;
    }



}
