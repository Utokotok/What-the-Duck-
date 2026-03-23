package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.LeaderboardManager;
import com.groupone.wtd.Utils.Utils;

import java.util.List;

public class Leaderboard implements Screen {

    GameLauncher game;
    Stage stage;
    Texture bush;
    Texture background;
    Texture panelTex;
    Texture rowEvenTex;
    Texture rowOddTex;
    Texture headerTex;
    Vector2 gameCenter = new Vector2(0, 0);

    public Leaderboard(GameLauncher game) {
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

        // ── Textures ──────────────────────────────────────────────────────────
        panelTex = game.manager.get("Buttons/board.png"); // wooden board background
        rowEvenTex = solidTexture(0x16213eFF); // slightly lighter row
        rowOddTex = solidTexture(0x0f3460FF); // accent row
        headerTex = solidTexture(0xe94560FF); // vivid header bar

        // ── Fonts ─────────────────────────────────────────────────────────────
        BitmapFont titleFont = Utils.createFont("number_font.ttf", 52, 3);
        BitmapFont headFont = Utils.createFont("string_font.ttf", 22, 2);
        BitmapFont entryFont = Utils.createFont("number_font.ttf", 24, 2);
        BitmapFont rankFont = Utils.createFont("number_font.ttf", 28, 2);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle headStyle = new Label.LabelStyle(headFont, Color.WHITE);
        Label.LabelStyle entryStyle = new Label.LabelStyle(entryFont, Color.WHITE);
        Label.LabelStyle goldStyle = new Label.LabelStyle(rankFont, new Color(1f, 0.84f, 0f, 1f));
        Label.LabelStyle silverStyle = new Label.LabelStyle(rankFont, new Color(0.75f, 0.75f, 0.75f, 1f));
        Label.LabelStyle bronzeStyle = new Label.LabelStyle(rankFont, new Color(0.8f, 0.5f, 0.2f, 1f));

        // ── Semi-transparent full-screen dimmer ───────────────────────────────
        Texture dimTex = solidTexture(0x00000099);
        Image dimmer = new Image(dimTex);
        dimmer.setSize(W, H);
        stage.addActor(dimmer);

        // ── Panel (widened) ───────────────────────────────────────────────────
        float panelW = 1100f;
        float panelH = 600f;
        float panelX = (W - panelW) / 2f;
        float panelY = (H - panelH) / 2f;

        Image panel = new Image(panelTex);
        panel.setSize(panelW, panelH);
        panel.setPosition(panelX, panelY);
        stage.addActor(panel);

        // ── Title ─────────────────────────────────────────────────────────────
        Label title = new Label("LEADERBOARD", titleStyle);
        title.setPosition(panelX + (panelW - title.getPrefWidth()) / 2f, panelY + panelH - 90f);
        stage.addActor(title);

        // ── Header row ────────────────────────────────────────────────────────
        float headerH = 40f;
        float headerY = panelY + panelH - 140f;
        Image headerBg = new Image(headerTex);
        headerBg.setSize(panelW - 40f, headerH);
        headerBg.setPosition(panelX + 20f, headerY);
        stage.addActor(headerBg);

        // Column positions — properly spaced across the wider panel
        float[] colX = {
                panelX + 30f, // #
                panelX + 80f, // NAME
                panelX + 400f, // SCORE
                panelX + 570f, // MODE
                panelX + 730f, // DATE
                panelX + 930f // TIME
        };
        String[] headers = { "#", "NAME", "SCORE", "MODE", "DATE", "TIME" };
        for (int i = 0; i < headers.length; i++) {
            Label h = new Label(headers[i], headStyle);
            h.setPosition(colX[i], headerY + 10f);
            stage.addActor(h);
        }

        // ── Scrollable rows ───────────────────────────────────────────────────
        List<LeaderboardManager.Entry> entries = LeaderboardManager.getLeaderboard();

        Table table = new Table();
        table.top().left();

        float rowH = 50f;
        float rowsAreaH = panelH - 170f;

        // Column widths — total should roughly equal panelW - 60 (~1040)
        float rankW = 50f;
        float nameW = 300f;
        float scoreW = 170f;
        float modeW = 160f;
        float dateW = 190f;
        float timeW = 80f;

        for (int i = 0; i < entries.size(); i++) {
            LeaderboardManager.Entry e = entries.get(i);
            int rank = i + 1;

            Label.LabelStyle rankStyle = (rank == 1) ? goldStyle
                    : (rank == 2) ? silverStyle
                            : (rank == 3) ? bronzeStyle
                                    : entryStyle;

            Texture rowBg = (i % 2 == 0) ? rowEvenTex : rowOddTex;

            // Labels — each in its own cell
            Label rankLbl = new Label(String.valueOf(rank), rankStyle);
            Label nameLbl = new Label(e.name != null && !e.name.isEmpty() ? e.name : "---", entryStyle);
            Label scoreLbl = new Label(String.format("%07d", e.score), entryStyle);
            Label modeLbl = new Label(e.gameMode, entryStyle);
            Label dateLbl = new Label(e.date, entryStyle);
            Label timeLbl = new Label(e.time, entryStyle);

            rankLbl.setAlignment(Align.left);
            nameLbl.setAlignment(Align.left);
            scoreLbl.setAlignment(Align.left);
            modeLbl.setAlignment(Align.left);
            dateLbl.setAlignment(Align.left);
            timeLbl.setAlignment(Align.left);

            table.add(rankLbl).width(rankW).padLeft(10f).padBottom(6f);
            table.add(nameLbl).width(nameW).padBottom(6f);
            table.add(scoreLbl).width(scoreW).padBottom(6f);
            table.add(modeLbl).width(modeW).padBottom(6f);
            table.add(dateLbl).width(dateW).padBottom(6f);
            table.add(timeLbl).width(timeW).padBottom(6f);
            table.row();
        }

        if (entries.isEmpty()) {
            Label empty = new Label("No scores yet — go play!", entryStyle);
            empty.setAlignment(Align.center);
            table.add(empty).colspan(6).padTop(40f);
        }

        ScrollPane scroll = new ScrollPane(table);
        scroll.setPosition(panelX + 20f, panelY + 20f);
        scroll.setSize(panelW - 40f, rowsAreaH - 10f);
        scroll.setScrollingDisabled(true, false);
        stage.addActor(scroll);
        stage.setScrollFocus(scroll);

        // ── Back button (raised) ──────────────────────────────────────────────
        Texture backTex = solidTexture(0xe94560FF);
        Texture backOverTex = solidTexture(0xc73652FF);
        float btnW = 180f, btnH = 50f;
        float btnX = panelX + (panelW - btnW) / 2f;
        float btnY = panelY - 10f;

        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = new TextureRegionDrawable(backTex);
        backStyle.over = new TextureRegionDrawable(backOverTex);
        backStyle.down = new TextureRegionDrawable(backOverTex);

        ImageButton backBtn = new ImageButton(backStyle);
        backBtn.setSize(btnW, btnH);
        backBtn.setPosition(btnX, btnY);

        Label backLabel = new Label("BACK", headStyle);
        backLabel.setPosition(btnX + (btnW - backLabel.getPrefWidth()) / 2f,
                btnY + (btnH - backLabel.getPrefHeight()) / 2f);
        backLabel.setTouchable(Touchable.disabled); // prevent label from blocking button clicks

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundManager.playClick();
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(backBtn);
        stage.addActor(backLabel);
    }

    /** Creates a 1×1 Texture filled with the given RGBA8888 color int. */
    private static Texture solidTexture(int rgba8888) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(new Color(rgba8888));
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    @Override
    public void show() {
    }

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
        stage.dispose();
    }
}
