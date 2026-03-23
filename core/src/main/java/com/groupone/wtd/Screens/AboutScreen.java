package com.groupone.wtd.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

public class AboutScreen implements Screen {

    GameLauncher game;
    Stage stage;
    Texture bush;
    Texture background;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/about_background.png
    // -------------------------------------------------------------------------
    // backgroundTexture = new Texture(Gdx.files.internal("about_background.png"));
    Texture aboutBackgroundTex; // currently using solidTexture() fallback

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/about_panel.png
    // -------------------------------------------------------------------------
    // panelTexture = new Texture(Gdx.files.internal("about_panel.png"));
    Texture panelTex; // currently using solidTexture() fallback

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/about_back_button.png
    // -------------------------------------------------------------------------
    // backButtonTexture = new Texture(Gdx.files.internal("about_back_button.png"));
    Texture backBtnTex; // currently using solidTexture() fallback

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/about_divider.png
    // -------------------------------------------------------------------------
    // dividerTexture = new Texture(Gdx.files.internal("about_divider.png"));
    Texture dividerTex; // currently using solidTexture() fallback

    Texture headerTex;
    Texture rowEvenTex;
    Texture rowOddTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual team photos when ready
    // Expected files (1:1 square photos):
    // assets/Team/photo_gumabon.png
    // assets/Team/photo_lalamoro.png
    // assets/Team/photo_loplop.png
    // assets/Team/photo_macaraig.png
    // -------------------------------------------------------------------------
    // photoGumabon = new Texture(Gdx.files.internal("Team/photo_gumabon.png"));
    // photoLalamoro = new Texture(Gdx.files.internal("Team/photo_lalamoro.png"));
    // photoLoplop = new Texture(Gdx.files.internal("Team/photo_loplop.png"));
    // photoMacaraig = new Texture(Gdx.files.internal("Team/photo_macaraig.png"));
    Texture photoGumabon;
    Texture photoLalamoro;
    Texture photoLoplop;
    Texture photoMacaraig;

    public AboutScreen(GameLauncher game) {
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

        // ── Fallback textures ────────────────────────────────────────────────
        aboutBackgroundTex = solidTexture(0x00000099); // semi-transparent dim
        panelTex = game.manager.get("Buttons/board.png"); // wooden board background
        backBtnTex = solidTexture(0xe94560FF); // vivid red
        dividerTex = solidTexture(0xe94560FF); // accent line
        headerTex = solidTexture(0xe94560FF);
        rowEvenTex = solidTexture(0x16213eFF);
        rowOddTex = solidTexture(0x0f3460FF);
        photoGumabon = game.manager.get("Photos/Gumabon.png");
        photoLalamoro = game.manager.get("Photos/Lalamoro.png");
        photoLoplop = game.manager.get("Photos/Loplop.png");
        photoMacaraig = game.manager.get("Photos/Macaraig.png");

        // ── Fonts ────────────────────────────────────────────────────────────
        BitmapFont titleFont = Utils.createFont("number_font.ttf", 48, 3);
        BitmapFont sectionFont = Utils.createFont("number_font.ttf", 32, 2);
        BitmapFont bodyFont = Utils.createFont("number_font.ttf", 24, 1);
        BitmapFont headFont = Utils.createFont("number_font.ttf", 26, 2);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle sectionStyle = new Label.LabelStyle(sectionFont, new Color(1f, 0.84f, 0f, 1f));
        Label.LabelStyle bodyStyle = new Label.LabelStyle(bodyFont, Color.WHITE);
        Label.LabelStyle headStyle = new Label.LabelStyle(headFont, Color.WHITE);
        Label.LabelStyle accentStyle = new Label.LabelStyle(bodyFont, new Color(0.45f, 0.85f, 1f, 1f));

        // ── Dimmer overlay ───────────────────────────────────────────────────
        Image dimmer = new Image(aboutBackgroundTex);
        dimmer.setSize(W, H);
        stage.addActor(dimmer);

        // ── Panel ────────────────────────────────────────────────────────────
        float panelW = 1000f;
        float panelH = 600f;
        float panelX = (W - panelW) / 2f;
        float panelY = (H - panelH) / 2f;

        Image panel = new Image(panelTex);
        panel.setSize(panelW, panelH);
        panel.setPosition(panelX, panelY);
        stage.addActor(panel);

        // ── Title ────────────────────────────────────────────────────────────
        Label title = new Label("ABOUT", titleStyle);
        title.setPosition(panelX + (panelW - title.getPrefWidth()) / 2f, panelY + panelH - 90f);
        stage.addActor(title);

        // ── Scrollable content ───────────────────────────────────────────────
        Table content = new Table();
        content.top().left();
        content.defaults().padLeft(20f).padRight(20f);
        float contentW = panelW - 60f;

        // --- Section 1: What the Duck!? ---
        Label sec1Title = new Label("What the Duck!?", sectionStyle);
        content.add(sec1Title).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        Label sec1Body = new Label(
                "What the Duck!? is a Java-based interactive shooting game inspired by the classic\n" +
                        "arcade game Duck Hunt -- built not just to entertain, but to bring Data Structures\n" +
                        "and Algorithms to life.\n\n" +
                        "Rather than learning sorting, searching, and data structures purely from textbooks,\n" +
                        "this game embeds them directly into its mechanics. Every wave you play is powered\n" +
                        "by real algorithms working under the hood.",
                bodyStyle);
        sec1Body.setWrap(true);
        content.add(sec1Body).width(contentW).left().padBottom(16f).row();

        // --- Section 2: Algorithms & Data Structures ---
        Label sec2Title = new Label("Algorithms & Data Structures", sectionStyle);
        content.add(sec2Title).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        Label fisherYates = new Label("Fisher-Yates Shuffle", accentStyle);
        content.add(fisherYates).left().padTop(4f).row();
        Label fisherDesc = new Label(
                "Used in Num Hunt to randomly arrange mathematical operators at the start of each\n" +
                        "wave, ensuring no two rounds feel the same.",
                bodyStyle);
        fisherDesc.setWrap(true);
        content.add(fisherDesc).width(contentW).left().padBottom(8f).row();

        Label queue = new Label("Queue (FIFO)", accentStyle);
        content.add(queue).left().padTop(4f).row();
        Label queueDesc = new Label(
                "Powers Word Hunt by managing the correct letter sequence the player must follow,\n" +
                        "validating each shot in order.",
                bodyStyle);
        queueDesc.setWrap(true);
        content.add(queueDesc).width(contentW).left().padBottom(8f).row();

        Label insertionSort = new Label("Insertion Sort", accentStyle);
        content.add(insertionSort).left().padTop(4f).row();
        Label insertionDesc = new Label(
                "Maintains the leaderboard in real time, placing each new score into its correct\n" +
                        "ranked position as soon as a session ends.",
                bodyStyle);
        insertionDesc.setWrap(true);
        content.add(insertionDesc).width(contentW).left().padBottom(16f).row();

        // --- Section 3: The Team ---
        Label sec3Title = new Label("The Team", sectionStyle);
        content.add(sec3Title).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        // Team table
        Table teamTable = new Table();
        teamTable.top().left();

        String[][] team = {
                { "Photo", "Name", "Role" },
                { "photo_gumabon", "Joshua Philip B. Gumabon", "Lead Voice Actor, Game Designer,\nTechnical Writer" },
                { "photo_lalamoro", "Justine Mark I. Lalamoro", "Developer (Word Hunt), Game Designer,\nVoice Actor" },
                { "photo_loplop", "Elieson C. Loplop", "Main Developer, Director, Animator,\nGame Designer" },
                { "photo_macaraig", "Ivan James V. Macaraig", "Lead Sound Engineer, Lead Technical\nWriter, Developer" }
        };

        float photoSize = 64f;

        for (int i = 0; i < team.length; i++) {
            Label.LabelStyle rowStyle = (i == 0) ? headStyle : bodyStyle;
            Texture rowBg = (i == 0) ? headerTex : (i % 2 == 0) ? rowEvenTex : rowOddTex;

            Table row = new Table();
            row.setBackground(new TextureRegionDrawable(rowBg));

            if (i == 0) {
                // Header row — text only
                Label photoHeader = new Label(team[i][0], rowStyle);
                Label nameLabel = new Label(team[i][1], rowStyle);
                Label roleLabel = new Label(team[i][2], rowStyle);
                row.add(photoHeader).width(photoSize + 16f).pad(8f).left();
                row.add(nameLabel).width(contentW * 0.32f).pad(8f).left();
                row.add(roleLabel).width(contentW * 0.48f).pad(8f).left();
            } else {
                // Data row — actual team photo + name + role
                Texture photoTex;
                switch (team[i][0]) {
                    case "photo_gumabon":
                        photoTex = photoGumabon;
                        break;
                    case "photo_lalamoro":
                        photoTex = photoLalamoro;
                        break;
                    case "photo_loplop":
                        photoTex = photoLoplop;
                        break;
                    case "photo_macaraig":
                        photoTex = photoMacaraig;
                        break;
                    default:
                        photoTex = solidTexture(0x555555FF);
                        break;
                }
                Image photo = new Image(photoTex);

                Label nameLabel = new Label(team[i][1], rowStyle);
                nameLabel.setWrap(true);
                Label roleLabel = new Label(team[i][2], rowStyle);
                roleLabel.setWrap(true);

                row.add(photo).size(photoSize, photoSize).pad(8f).left();
                row.add(nameLabel).width(contentW * 0.32f).pad(8f).left();
                row.add(roleLabel).width(contentW * 0.48f).pad(8f).left();
            }

            teamTable.add(row).width(contentW).row();
        }

        content.add(teamTable).width(contentW).left().padBottom(16f).row();

        // --- Section 4: About the Project ---
        Label sec4Title = new Label("About the Project", sectionStyle);
        content.add(sec4Title).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        Label sec4Body = new Label(
                "What the Duck!? was developed in partial fulfillment of CS 201A -- Data\n" +
                        "Structures and Algorithms Analysis at the Technological Institute of the\n" +
                        "Philippines, College of Information Technology Education, under the guidance\n" +
                        "of Ms. Janice A. Capule.",
                bodyStyle);
        sec4Body.setWrap(true);
        content.add(sec4Body).width(contentW).left().padBottom(20f).row();

        // ── ScrollPane ───────────────────────────────────────────────────────
        float scrollH = panelH - 110f;
        ScrollPane scroll = new ScrollPane(content);
        scroll.setPosition(panelX + 20f, panelY + 20f);
        scroll.setSize(panelW - 40f, scrollH);
        scroll.setScrollingDisabled(true, false);
        stage.addActor(scroll);
        stage.setScrollFocus(scroll);

        // ── Back button ──────────────────────────────────────────────────────
        Texture backOverTex = solidTexture(0xc73652FF);
        float btnW = 180f, btnH = 50f;
        float btnX = panelX + (panelW - btnW) / 2f;
        float btnY = panelY - 10f;

        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.up = new TextureRegionDrawable(backBtnTex);
        backStyle.over = new TextureRegionDrawable(backOverTex);
        backStyle.down = new TextureRegionDrawable(backOverTex);

        ImageButton backBtn = new ImageButton(backStyle);
        backBtn.setSize(btnW, btnH);
        backBtn.setPosition(btnX, btnY);

        Label backLabel = new Label("BACK", headStyle);
        backLabel.setPosition(
                btnX + (btnW - backLabel.getPrefWidth()) / 2f,
                btnY + (btnH - backLabel.getPrefHeight()) / 2f);
        backLabel.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);

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

    /** Adds a horizontal accent divider line to the content table. */
    private void addDivider(Table table, float width) {
        Image divider = new Image(dividerTex);
        table.add(divider).width(width).height(3f).padBottom(8f).left().row();
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
