package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

/**
 * Settings popup with 6 volume sliders:
 *   Master (bigger), Voice, Gun, Duck, Background, SFX
 */
public class SettingsScreen implements Screen {

    GameLauncher game;
    Stage stage;
    Texture bush;
    Texture background;

    // Slider data for each category
    private static final int SLIDER_COUNT = 6;
    private final Image[] boards = new Image[SLIDER_COUNT];
    private final Image[] circles = new Image[SLIDER_COUNT];
    private final Label[] pctLabels = new Label[SLIDER_COUNT];
    private final float[] circleMinX = new float[SLIDER_COUNT];
    private final float[] circleMaxX = new float[SLIDER_COUNT];

    public SettingsScreen(GameLauncher game) {
        this.game = game;
        stage = new Stage(game.viewport);
        InputMultiplexer mux = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(mux);

        bush = game.manager.get("Background/bush.png");
        background = game.manager.get("Background/background.png");

        buildUI();
    }

    private void buildUI() {
        float W = GameLauncher.gameWidth;
        float H = GameLauncher.gameHeight;

        // ── Dimmer ───────────────────────────────────────────────────────────
        Texture dimTex = solidTexture(0x00000099);
        Image dimmer = new Image(dimTex);
        dimmer.setSize(W, H);
        stage.addActor(dimmer);

        // ── Panel (board.png) ────────────────────────────────────────────────
        Texture panelTex = game.manager.get("Buttons/board.png");
        float panelW = 900f;
        float panelH = 580f;
        float panelX = (W - panelW) / 2f;
        float panelY = (H - panelH) / 2f;

        Image panel = new Image(panelTex);
        panel.setSize(panelW, panelH);
        panel.setPosition(panelX, panelY);
        stage.addActor(panel);

        // ── Title ────────────────────────────────────────────────────────────
        BitmapFont titleFont = Utils.createFont("number_font.ttf", 48, 3);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label title = new Label("SETTINGS", titleStyle);
        title.setPosition(panelX + (panelW - title.getPrefWidth()) / 2f, panelY + panelH - 85f);
        stage.addActor(title);

        // ── Close (X) button — top-right of panel ────────────────────────────
        Texture closeTex = game.manager.get("Buttons/close.png");
        float closeBtnSize = 50f;
        ImageButton.ImageButtonStyle closeStyle = new ImageButton.ImageButtonStyle();
        closeStyle.up = new TextureRegionDrawable(closeTex);
        closeStyle.over = new TextureRegionDrawable(closeTex).tint(Color.LIGHT_GRAY);
        closeStyle.down = new TextureRegionDrawable(closeTex).tint(Color.GRAY);
        ImageButton closeBtn = new ImageButton(closeStyle);
        closeBtn.setSize(closeBtnSize, closeBtnSize);
        closeBtn.setPosition(panelX + panelW - closeBtnSize - 45f, panelY + panelH - closeBtnSize - 30f);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playClick();
                game.setScreen(new MainMenu(game));
            }
        });
        stage.addActor(closeBtn);

        // ── Fonts for sliders ────────────────────────────────────────────────
        BitmapFont labelFont = Utils.createFont("number_font.ttf", 26, 2);
        BitmapFont pctFont = Utils.createFont("number_font.ttf", 22, 1);
        BitmapFont masterLabelFont = Utils.createFont("number_font.ttf", 26, 2);
        BitmapFont masterPctFont = Utils.createFont("number_font.ttf", 22, 1);

        Label.LabelStyle labelStyle = new Label.LabelStyle(labelFont, Color.WHITE);
        Label.LabelStyle pctStyle = new Label.LabelStyle(pctFont, Color.WHITE);
        Label.LabelStyle masterLabelStyle = new Label.LabelStyle(masterLabelFont, new Color(1f, 0.84f, 0f, 1f));
        Label.LabelStyle masterPctStyle = new Label.LabelStyle(masterPctFont, new Color(1f, 0.84f, 0f, 1f));

        // ── Textures for sliders ─────────────────────────────────────────────
        Texture boardTex = game.manager.get("Buttons/volume board.png");
        Texture circleTex = game.manager.get("Buttons/volume circle.png");

        // ── Slider configuration ─────────────────────────────────────────────
        String[] names = { "Master Volume", "Voice", "Gun", "Duck", "Background", "SFX" };
        float[] initValues = {
            SoundManager.getGlobalVolume(),
            SoundManager.getVoiceVolume(),
            SoundManager.getGunVolume(),
            SoundManager.getDuckVolume(),
            SoundManager.getBackgroundVolume(),
            SoundManager.getSfxVolume()
        };

        // All sliders same size, maximizing panel space
        float boardW = 500f, boardH = 40f, circleSize = 36f;
        float rowSpacing = 74f; 
        
        // 6 layout rows
        float totalHeight = (SLIDER_COUNT - 1) * rowSpacing + boardH;
        float startY = panelY + (panelH - totalHeight) / 2f - 30f; // shifted down completely
        
        float labelX = panelX + 60f;
        float boardStartX = panelX + 290f;
        float pctLabelOffsetX = 15f;

        for (int i = 0; i < SLIDER_COUNT; i++) {
            // Rows are drawn top to bottom
            float rowY = startY + (SLIDER_COUNT - 1 - i) * rowSpacing;

            Label.LabelStyle currentLabelStyle = (i == 0) ? masterLabelStyle : labelStyle;
            Label.LabelStyle currentPctStyle = (i == 0) ? masterPctStyle : pctStyle;

            Label lbl = new Label(names[i], currentLabelStyle);
            lbl.setPosition(labelX, rowY + (boardH - lbl.getPrefHeight()) / 2f);
            stage.addActor(lbl);

            boards[i] = new Image(boardTex);
            boards[i].setSize(boardW, boardH);
            boards[i].setPosition(boardStartX, rowY);
            stage.addActor(boards[i]);

            float cmI = boardStartX + 5f;
            float cxI = boardStartX + boardW - circleSize - 5f;
            circleMinX[i] = cmI;
            circleMaxX[i] = cxI;

            float circleXI = cmI + initValues[i] * (cxI - cmI);
            float circleYI = rowY + (boardH - circleSize) / 2f;

            circles[i] = new Image(circleTex);
            circles[i].setSize(circleSize, circleSize);
            circles[i].setPosition(circleXI, circleYI);
            circles[i].setTouchable(Touchable.enabled);
            stage.addActor(circles[i]);

            int pct = Math.round(initValues[i] * 100f);
            pctLabels[i] = new Label(pct + "%", currentPctStyle);
            pctLabels[i].setPosition(boardStartX + boardW + pctLabelOffsetX,
                    rowY + (boardH - pctLabels[i].getPrefHeight()) / 2f);
            stage.addActor(pctLabels[i]);

            addDragListener(i);
        }
    }

    /** Add a horizontal drag listener for slider at the given index. */
    private void addDragListener(final int index) {
        circles[index].addListener(new InputListener() {
            private float offsetX;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                offsetX = x;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float newX = circles[index].getX() + x - offsetX;
                newX = MathUtils.clamp(newX, circleMinX[index], circleMaxX[index]);
                circles[index].setPosition(newX, circles[index].getY());

                float range = circleMaxX[index] - circleMinX[index];
                float vol = (range > 0) ? (newX - circleMinX[index]) / range : 1f;
                applyVolume(index, vol);

                int pct = Math.round(vol * 100f);
                pctLabels[index].setText(pct + "%");
            }
        });
    }

    /** Apply the dragged volume to the corresponding SoundManager category. */
    private void applyVolume(int index, float vol) {
        switch (index) {
            case 0: SoundManager.setGlobalVolume(vol); break;
            case 1: SoundManager.setVoiceVolume(vol); break;
            case 2: SoundManager.setGunVolume(vol); break;
            case 3: SoundManager.setDuckVolume(vol); break;
            case 4: SoundManager.setBackgroundVolume(vol); break;
            case 5: SoundManager.setSfxVolume(vol); break;
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

    @Override public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
        game.batch.draw(bush, 0, 0, GameLauncher.gameWidth, GameLauncher.gameHeight);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
