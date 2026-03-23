package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.LeaderboardManager;
import com.groupone.wtd.Utils.Utils;

public class GameOver {
    Stage stage;
    GameLauncher game;
    MainGame currGame;
    String[][] reasons = {
            { "The ducks outlasted your ammo.", "Should've aimed first.", "You shot all! The ducks are fine.",
                    "Reloading... wait what!?", "Who taught ducks to dodge!?" },
            { "Where are the ducks?", "The ducks has left the chat.", "No ducks. Well done.", "Ducks saw you and left.",
                    "You cleared the pond." },
            { "Donald got no more time for you.", "Check bottom-left next \"time\".", "My grandpa aims faster.",
                    "Clocks don't walk, y'know!", "Think fast unc!" }
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

    // ── Name input UI ────────────────────────────────────────────────────────
    private TextField nameField;
    private ImageButton submitBtn;
    private Label submitLabel;
    private Label validationLabel;
    private Label namePromptLabel;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/GameOver/name_input_bg.png (background behind name
    // input area)
    // -------------------------------------------------------------------------
    // nameInputBgTex = new
    // Texture(Gdx.files.internal("GameOver/name_input_bg.png"));

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/GameOver/text_field_bg.png (text field background)
    // -------------------------------------------------------------------------
    // textFieldBgTex = new
    // Texture(Gdx.files.internal("GameOver/text_field_bg.png"));

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/GameOver/ok_button.png (submit / OK button)
    // -------------------------------------------------------------------------
    // okButtonTex = new Texture(Gdx.files.internal("GameOver/ok_button.png"));

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/GameOver/ok_button_hover.png (submit / OK button hover)
    // -------------------------------------------------------------------------
    // okButtonHoverTex = new
    // Texture(Gdx.files.internal("GameOver/ok_button_hover.png"));

    private boolean nameSubmitted = false;
    private String playerName = "";
    private static final int MAX_NAME_LENGTH = 12;

    public GameOver(GameLauncher game, MainGame currGame) {
        this.game = game;
        this.currGame = currGame;
        tryAgain = Utils.createButton(game.manager.get("Buttons/try_again.png"), GameLauncher.gameWidth / 2f,
                GameLauncher.gameHeight + 100, 0.28f, true);
        quit = Utils.createButton(game.manager.get("Buttons/quit.png"), GameLauncher.gameWidth / 2f,
                GameLauncher.gameHeight + 100, 0.28f, true);
        stage = new Stage(game.viewport);
        gameOverText = new Label(reason, new Label.LabelStyle(game.UIFont1, Color.WHITE));
        pointsText = new Label("Your scored " + currGame.points + " points!",
                new Label.LabelStyle(game.UIFont2, Color.WHITE));
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
                Actions.moveTo(GameLauncher.gameWidth / 2f - gameOverText.getWidth() / 2f,
                        GameLauncher.gameHeight / 2f - gameOverText.getHeight() / 2f + 150, 0.5f,
                        Interpolation.bounceOut)));
        pointsText.addAction(Actions.sequence(
                Actions.delay(animationDelay),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        SoundManager.playGameOver();
                    }
                }),
                Actions.delay(animationDelay),
                Actions.moveTo(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, 390, 1f,
                        Interpolation.bounceOut)));

        // Try Again and Quit are hidden until name is submitted
        tryAgain.setVisible(false);
        quit.setVisible(false);

        buildNameInputUI();

        tryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currGame instanceof WordHunt) {
                    game.setScreen(new WordHunt(game));
                } else {
                    game.setScreen(new NumHunt(game));
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && isAnimationDone) {
                    SoundManager.playPlayAgain();
                }
            }
        });

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
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

    private void buildNameInputUI() {
        float W = GameLauncher.gameWidth;

        // ── Fonts & styles ───────────────────────────────────────────────────
        com.badlogic.gdx.graphics.g2d.BitmapFont promptFont = Utils.createFont("number_font.ttf", 22, 2);
        com.badlogic.gdx.graphics.g2d.BitmapFont fieldFont = Utils.createFont("number_font.ttf", 20, 1);
        com.badlogic.gdx.graphics.g2d.BitmapFont smallFont = Utils.createFont("number_font.ttf", 16, 1);

        Label.LabelStyle promptStyle = new Label.LabelStyle(promptFont, Color.WHITE);
        Label.LabelStyle validStyle = new Label.LabelStyle(smallFont, new Color(1f, 0.4f, 0.4f, 1f));

        // ── Name prompt label ────────────────────────────────────────────────
        namePromptLabel = new Label("Enter your name (maximum: 12 characters)", promptStyle);
        namePromptLabel.setWidth(W);
        namePromptLabel.setAlignment(Align.center);
        namePromptLabel.setPosition(0, -100f); // off-screen, animates in

        // ── TextField ────────────────────────────────────────────────────────
        // Falls back to solidTexture — replace with actual assets above
        Texture fieldBgTex = solidTexture(0x16213eFF);
        Texture cursorTex = solidTexture(0xFFFFFFFF);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = fieldFont;
        tfStyle.fontColor = Color.WHITE;
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(fieldBgTex);
        bgDrawable.setLeftWidth(10f);
        bgDrawable.setRightWidth(10f);
        tfStyle.background = bgDrawable;
        tfStyle.cursor = new TextureRegionDrawable(cursorTex);
        tfStyle.cursor.setMinWidth(2f);

        float fieldW = 300f;
        float fieldH = 44f;
        float fieldX = W / 2f - fieldW / 2f - 60f;

        nameField = new TextField("", tfStyle);
        nameField.setMaxLength(MAX_NAME_LENGTH);
        nameField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return textField.getText().length() < MAX_NAME_LENGTH;
            }
        });
        nameField.setMessageText("Your name...");
        nameField.setSize(fieldW, fieldH);
        nameField.setPosition(fieldX, -100f); // off-screen

        // ── Validation label ─────────────────────────────────────────────────
        validationLabel = new Label("", validStyle);
        validationLabel.setPosition(fieldX, -100f);

        // ── Submit / OK button ───────────────────────────────────────────────
        // Falls back to solidTexture — replace with actual assets above
        Texture submitUpTex = solidTexture(0xe94560FF);
        Texture submitOverTex = solidTexture(0xc73652FF);

        ImageButton.ImageButtonStyle submitStyle = new ImageButton.ImageButtonStyle();
        submitStyle.up = new TextureRegionDrawable(submitUpTex);
        submitStyle.over = new TextureRegionDrawable(submitOverTex);
        submitStyle.down = new TextureRegionDrawable(submitOverTex);

        float sBtnW = 100f, sBtnH = 44f;
        float sBtnX = W / 2f + fieldW / 2f - 60f + 16f; // 16px margin after field

        submitBtn = new ImageButton(submitStyle);
        submitBtn.setSize(sBtnW, sBtnH);
        submitBtn.setPosition(sBtnX, -100f); // off-screen

        Label.LabelStyle headStyle = new Label.LabelStyle(promptFont, Color.WHITE);
        submitLabel = new Label("OK", headStyle);
        submitLabel.setPosition(
                sBtnX + (sBtnW - submitLabel.getPrefWidth()) / 2f,
                -100f);
        submitLabel.setTouchable(Touchable.disabled); // prevent blocking clicks on button

        // ── Landing Y positions ──────────────────────────────────────────────
        final float promptY = 345f;
        final float fieldY = 295f;
        final float validY = 272f;
        final float okBtnY = 295f;

        submitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String text = nameField.getText().trim();
                if (text.length() > 0) {
                    playerName = text;
                    nameSubmitted = true;
                    SoundManager.playClick();

                    // Save score with name
                    String gameMode = (currGame instanceof WordHunt) ? "WordHunt" : "NumHunt";
                    LeaderboardManager.saveScore(currGame.points, gameMode, playerName);

                    // Slide everything DOWN off-screen completely
                    float dropTarget = -200f;
                    float dropDuration = 0.35f;
                    namePromptLabel.addAction(Actions.moveTo(0, dropTarget, dropDuration, Interpolation.pow2In));
                    nameField.addAction(Actions.moveTo(fieldX, dropTarget, dropDuration, Interpolation.pow2In));
                    validationLabel.addAction(Actions.moveTo(fieldX, dropTarget, dropDuration, Interpolation.pow2In));
                    submitBtn.addAction(Actions.moveTo(sBtnX, dropTarget, dropDuration, Interpolation.pow2In));
                    submitLabel.addAction(Actions.moveTo(
                            sBtnX + (sBtnW - submitLabel.getPrefWidth()) / 2f,
                            dropTarget, dropDuration, Interpolation.pow2In));

                    // Show try again / quit after name input drops
                    tryAgain.setVisible(true);
                    quit.setVisible(true);
                    tryAgain.setPosition(tryAgain.getX(), GameLauncher.gameHeight + 100);
                    quit.setPosition(quit.getX(), GameLauncher.gameHeight + 100);

                    tryAgain.addAction(Actions.sequence(
                            Actions.delay(dropDuration + 0.1f),
                            Actions.moveTo(tryAgain.getX(), 250, 0.25f, Interpolation.bounceOut)));
                    quit.addAction(Actions.sequence(
                            Actions.delay(dropDuration + 0.35f),
                            Actions.moveTo(quit.getX(), 110, 0.25f, Interpolation.bounceOut),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    isAnimationDone = true;
                                }
                            })));
                } else {
                    validationLabel.setText("Please enter a name!");
                }
            }
        });

        // ── Animate name input in after points text animation ────────────────
        float nameInputDelay = animationDelay + animationDelay + 1.5f;

        namePromptLabel.addAction(Actions.sequence(
                Actions.delay(nameInputDelay),
                Actions.moveTo(0, promptY, 0.4f, Interpolation.pow2Out)));
        nameField.addAction(Actions.sequence(
                Actions.delay(nameInputDelay + 0.2f),
                Actions.moveTo(fieldX, fieldY, 0.4f, Interpolation.pow2Out)));
        validationLabel.addAction(Actions.sequence(
                Actions.delay(nameInputDelay + 0.2f),
                Actions.moveTo(fieldX, validY, 0.4f, Interpolation.pow2Out)));
        submitBtn.addAction(Actions.sequence(
                Actions.delay(nameInputDelay + 0.3f),
                Actions.moveTo(sBtnX, okBtnY, 0.4f, Interpolation.pow2Out)));
        submitLabel.addAction(Actions.sequence(
                Actions.delay(nameInputDelay + 0.3f),
                Actions.moveTo(
                        sBtnX + (sBtnW - submitLabel.getPrefWidth()) / 2f,
                        okBtnY + (sBtnH - submitLabel.getPrefHeight()) / 2f,
                        0.4f, Interpolation.pow2Out)));

        stage.addActor(namePromptLabel);
        stage.addActor(nameField);
        stage.addActor(validationLabel);
        stage.addActor(submitBtn);
        stage.addActor(submitLabel);
    }

    /** Creates a 1x1 Texture filled with the given RGBA8888 color int. */
    private static Texture solidTexture(int rgba8888) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(new Color(rgba8888));
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    public void setReason(int reason) {
        this.reason = reasons[reason][MathUtils.random(0, 4)];
        gameOverText.setText(this.reason);
        // Score is saved when the player submits their name (in submitBtn click
        // handler)
    }

    public void updateGameOver(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        stage.act(delta);
    }

    public Stage getStage() {
        pointsText.setText("You scored " + currGame.points + " points!");
        return stage;
    }
}
