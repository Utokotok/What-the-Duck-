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
    private Label validationLabel;
    private Label namePromptLabel;

    private boolean nameSubmitted = false;
    private String playerName = "";
    private static final int MAX_NAME_LENGTH = 12;

    // Layout refs for submit animation
    private float startX, fieldX, sBtnX;
    private float sBtnW, sBtnH;
    private float totalW;

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
                        SoundManager.playGameOverDrop();
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
                        SoundManager.playGameOverHalf1();
                    }
                }),
                Actions.delay(animationDelay),
                Actions.moveTo(GameLauncher.gameWidth / 2f - pointsText.getWidth() / 2f, 390, 1f,
                        Interpolation.bounceOut)));

        // Try Again and Quit are hidden until OK is clicked
        tryAgain.setVisible(false);
        quit.setVisible(false);

        buildNameInputUI();

        tryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveScore();
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
                saveScore();
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

    /** Shared submit logic — called by both proceed button click and Enter key. */
    private void doSubmit() {
        if (nameSubmitted)
            return;
        SoundManager.playClick();
        saveScore();
        validationLabel.setText("Score saved!");
        validationLabel.getStyle().fontColor = new Color(0.4f, 1f, 0.4f, 1f);

        // Hide submit button visually and disable name field
        submitBtn.setTouchable(Touchable.disabled);
        nameField.setDisabled(true);

        float dropTarget = -200f;
        float dur = 0.35f;

        namePromptLabel.addAction(Actions.moveTo(startX, dropTarget, dur, Interpolation.pow2In));
        nameField.addAction(Actions.moveTo(fieldX, dropTarget, dur, Interpolation.pow2In));
        validationLabel.addAction(Actions.moveTo(startX, dropTarget, dur, Interpolation.pow2In));
        submitBtn.addAction(Actions.moveTo(sBtnX, dropTarget, dur, Interpolation.pow2In));

        // Play second half audio
        SoundManager.playGameOverHalf2();

        // Drop tryAgain and quit
        tryAgain.setVisible(true);
        quit.setVisible(true);

        tryAgain.setPosition(tryAgain.getX(), GameLauncher.gameHeight + 100);
        quit.setPosition(quit.getX(), GameLauncher.gameHeight + 100);

        tryAgain.addAction(Actions.sequence(
                Actions.delay(0.2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        SoundManager.playTryAgainDrop();
                    }
                }),
                Actions.moveTo(tryAgain.getX(), 250, 0.25f, Interpolation.bounceOut)));
        quit.addAction(Actions.sequence(
                Actions.delay(0.45f),
                Actions.moveTo(quit.getX(), 110, 0.25f, Interpolation.bounceOut),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isAnimationDone = true;
                    }
                })));
    }

    private void buildNameInputUI() {
        float W = GameLauncher.gameWidth;
        float H = GameLauncher.gameHeight;

        // ── Layout variables (Center) ───────────────────────────────
        float fieldW = 400f;
        float fieldH = 65f;
        sBtnW = 65f;
        sBtnH = 65f;

        // Center layout dynamically
        totalW = fieldW + 20f + sBtnW;
        startX = W / 2f - totalW / 2f;
        fieldX = startX;
        sBtnX = startX + fieldW + 20f;

        // ── Fonts & styles ───────────────────────────────────────────────────
        com.badlogic.gdx.graphics.g2d.BitmapFont promptFont = Utils.createFont("number_font.ttf", 36, 2);
        com.badlogic.gdx.graphics.g2d.BitmapFont fieldFont = Utils.createFont("number_font.ttf", 36, 1);
        com.badlogic.gdx.graphics.g2d.BitmapFont smallFont = Utils.createFont("number_font.ttf", 18, 1);

        Label.LabelStyle promptStyle = new Label.LabelStyle(promptFont, Color.WHITE);
        Label.LabelStyle validStyle = new Label.LabelStyle(smallFont, new Color(1f, 0.4f, 0.4f, 1f));

        // ── Name prompt label ────────────────────────────────────────────────
        namePromptLabel = new Label("Enter your name", promptStyle);
        namePromptLabel.setWidth(totalW);
        namePromptLabel.setAlignment(Align.center);
        namePromptLabel.setPosition(startX, H + 100f); // off-screen top, animates in down

        // ── TextField — use input.png as background ──────────────────────────
        Texture inputBgTex = game.manager.get("Buttons/input.png");
        Texture cursorTex = solidTexture(0xFFFFFFFF);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = fieldFont;
        tfStyle.fontColor = Color.WHITE;
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(inputBgTex);
        bgDrawable.setLeftWidth(25f);
        bgDrawable.setRightWidth(10f);
        tfStyle.background = bgDrawable;
        tfStyle.cursor = new TextureRegionDrawable(cursorTex);
        tfStyle.cursor.setMinWidth(4f);

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
        nameField.setPosition(fieldX, H + 100f); // off-screen top

        // ── Enter key support ────────────────────────────────────────────────
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    doSubmit();
                }
            }
        });

        // ── Validation label ─────────────────────────────────────────────────
        validationLabel = new Label("(max " + MAX_NAME_LENGTH + " chars)", validStyle);
        validationLabel.setPosition(startX, H + 100f);
        validationLabel.setWidth(totalW);
        validationLabel.setAlignment(Align.center);

        // ── Proceed button (replaces OK) — uses proceed.png ──────────────────
        Texture proceedTex = game.manager.get("Buttons/proceed.png");

        ImageButton.ImageButtonStyle submitStyle = new ImageButton.ImageButtonStyle();
        submitStyle.up = new TextureRegionDrawable(proceedTex);
        submitStyle.over = new TextureRegionDrawable(proceedTex).tint(Color.LIGHT_GRAY);
        submitStyle.down = new TextureRegionDrawable(proceedTex).tint(Color.GRAY);

        submitBtn = new ImageButton(submitStyle);
        submitBtn.setSize(sBtnW, sBtnH);
        submitBtn.setPosition(sBtnX, H + 100f); // off-screen top

        // ── Landing Y positions ──────────────────────────────────────────────
        final float promptY = 320f;
        final float fieldY = 240f;
        final float okBtnY = 240f;
        final float validY = 210f;

        submitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                doSubmit();
            }
        });

        // ── Animate name input sync with old delay ────────────────
        float dropOne = animationDelay + 2.5f;
        float dropDur = 0.25f;

        namePromptLabel.addAction(Actions.sequence(
                Actions.delay(dropOne),
                Actions.moveTo(startX, promptY, dropDur, Interpolation.bounceOut)));
        nameField.addAction(Actions.sequence(
                Actions.delay(dropOne),
                Actions.moveTo(fieldX, fieldY, dropDur, Interpolation.bounceOut),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        stage.setKeyboardFocus(nameField);
                    }
                })));
        validationLabel.addAction(Actions.sequence(
                Actions.delay(dropOne),
                Actions.moveTo(startX, validY, dropDur, Interpolation.bounceOut)));
        submitBtn.addAction(Actions.sequence(
                Actions.delay(dropOne),
                Actions.moveTo(sBtnX, okBtnY, dropDur, Interpolation.bounceOut)));

        stage.addActor(namePromptLabel);
        stage.addActor(nameField);
        stage.addActor(validationLabel);
        stage.addActor(submitBtn);
    }

    private void saveScore() {
        if (!nameSubmitted) {
            String text = nameField.getText().trim();
            if (text.isEmpty()) {
                text = "---";
            }
            playerName = text;
            nameSubmitted = true;
            String gameMode = (currGame instanceof WordHunt) ? "WordHunt" : "NumHunt";
            LeaderboardManager.saveScore(currGame.points, gameMode, playerName);
        }
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
        SoundManager.stopBackgroundMusic();
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
