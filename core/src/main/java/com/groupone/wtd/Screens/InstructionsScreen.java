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

public class InstructionsScreen implements Screen {

    GameLauncher game;
    Stage stage;
    Texture bush;
    Texture background;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_background.png
    // -------------------------------------------------------------------------
    // backgroundTexture = new
    // Texture(Gdx.files.internal("instructions_background.png"));
    Texture instrBackgroundTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_panel.png
    // -------------------------------------------------------------------------
    // panelTexture = new Texture(Gdx.files.internal("instructions_panel.png"));
    Texture panelTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_back_button.png
    // -------------------------------------------------------------------------
    // backButtonTexture = new
    // Texture(Gdx.files.internal("instructions_back_button.png"));
    Texture backBtnTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_divider.png
    // -------------------------------------------------------------------------
    // dividerTexture = new Texture(Gdx.files.internal("instructions_divider.png"));
    Texture dividerTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_numhunt_icon.png
    // -------------------------------------------------------------------------
    // numHuntIconTexture = new
    // Texture(Gdx.files.internal("instructions_numhunt_icon.png"));
    Texture numHuntIconTex;

    // -------------------------------------------------------------------------
    // IMAGE PLACEHOLDER — Replace with your actual asset when ready
    // Expected file: assets/instructions_wordhunt_icon.png
    // -------------------------------------------------------------------------
    // wordHuntIconTexture = new
    // Texture(Gdx.files.internal("instructions_wordhunt_icon.png"));
    Texture wordHuntIconTex;

    Texture headerTex;
    Texture rowEvenTex;
    Texture rowOddTex;

    public InstructionsScreen(GameLauncher game) {
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
        instrBackgroundTex = solidTexture(0x00000099);
        panelTex = game.manager.get("Buttons/board.png"); // wooden board background
        backBtnTex = solidTexture(0x00000000);
        dividerTex = solidTexture(0x00000000);
        headerTex = solidTexture(0x00000000);
        rowEvenTex = solidTexture(0x00000000);
        rowOddTex = solidTexture(0x00000000);
        numHuntIconTex = solidTexture(0x0f3460FF); // placeholder 64x64 icon
        wordHuntIconTex = solidTexture(0x16213eFF); // placeholder 64x64 icon

        // ── Fonts ────────────────────────────────────────────────────────────
        BitmapFont titleFont = Utils.createFont("number_font.ttf", 48, 3);
        BitmapFont sectionFont = Utils.createFont("number_font.ttf", 32, 2);
        BitmapFont bodyFont = Utils.createFont("number_font.ttf", 24, 1);
        BitmapFont headFont = Utils.createFont("number_font.ttf", 26, 2);
        BitmapFont smallFont = Utils.createFont("number_font.ttf", 18, 1);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle sectionStyle = new Label.LabelStyle(sectionFont, new Color(1f, 0.84f, 0f, 1f));
        Label.LabelStyle bodyStyle = new Label.LabelStyle(bodyFont, Color.WHITE);
        Label.LabelStyle headStyle = new Label.LabelStyle(headFont, Color.WHITE);
        Label.LabelStyle accentStyle = new Label.LabelStyle(bodyFont, new Color(0.45f, 0.85f, 1f, 1f));
        Label.LabelStyle emojiStyle = new Label.LabelStyle(bodyFont, new Color(1f, 0.65f, 0.3f, 1f));
        Label.LabelStyle tipStyle = new Label.LabelStyle(smallFont, new Color(0.7f, 1f, 0.7f, 1f));

        // ── Dimmer overlay ───────────────────────────────────────────────────
        Image dimmer = new Image(instrBackgroundTex);
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
        Label title = new Label("HOW TO PLAY", titleStyle);
        title.setPosition(panelX + (panelW - title.getPrefWidth()) / 2f, panelY + panelH - 90f);
        stage.addActor(title);

        // ── Scrollable content ───────────────────────────────────────────────
        Table content = new Table();
        content.top().left();
        content.defaults().padLeft(60f).padRight(60f);
        float contentW = panelW - 140f;

        // ── Controls Table ───────────────────────────────────────────────────
        Label controlsTitle = new Label("Controls", sectionStyle);
        content.add(controlsTitle).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        String[][] controls = {
                { "Input", "Action" },
                { "Left Mouse Click", "Shoot a duck at the cursor's position" },
                { "1", "Switch operation to Addition (+)" },
                { "2", "Switch operation to Subtraction (-)" },
                { "3", "Switch operation to Multiplication (x)" },
                { "4", "Switch operation to Division (/)" }
        };

        Table controlsTable = new Table();
        controlsTable.top().left();

        for (int i = 0; i < controls.length; i++) {
            Label.LabelStyle rowStyle = (i == 0) ? headStyle : bodyStyle;
            Texture rowBg = (i == 0) ? headerTex : (i % 2 == 0) ? rowEvenTex : rowOddTex;

            Table row = new Table();
            row.setBackground(new TextureRegionDrawable(rowBg));

            Label inputLabel = new Label(controls[i][0], rowStyle);
            Label actionLabel = new Label(controls[i][1], rowStyle);
            inputLabel.setWrap(true);
            actionLabel.setWrap(true);

            row.add(inputLabel).width(contentW * 0.3f).pad(8f).left();
            row.add(actionLabel).width(contentW * 0.65f).pad(8f).left();

            controlsTable.add(row).width(contentW).row();
        }
        content.add(controlsTable).width(contentW).left().padBottom(16f).row();

        // ── Mode 1 — Num Hunt ────────────────────────────────────────────────
        Label numHuntTitle = new Label("Mode 1 -- Num Hunt", sectionStyle);
        content.add(numHuntTitle).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        Label numObj = new Label("Objective: Reach the exact target number on screen by shooting\n" +
                "ducks and applying math operations to their values.", accentStyle);
        numObj.setWrap(true);
        content.add(numObj).width(contentW).left().padBottom(8f).row();

        String[] numBullets = {
                "* Ducks spawn carrying numeric values and fly across the screen.",
                "* Press 1, 2, 3, or 4 to select your operation, then click a duck\n  to apply it to your running total.",
                "* You can switch operations freely at any time.",
                "* Clear the wave by matching your running total to the target\n  number exactly."
        };
        for (String bullet : numBullets) {
            Label b = new Label(bullet, bodyStyle);
            b.setWrap(true);
            content.add(b).width(contentW).left().padBottom(4f).row();
        }
        content.add(new Label("", bodyStyle)).padBottom(12f).row(); // spacer

        // ── Mode 2 — Word Hunt ───────────────────────────────────────────────
        Label wordHuntTitle = new Label("Mode 2 -- Word Hunt", sectionStyle);
        content.add(wordHuntTitle).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        Label wordObj = new Label("Objective: Spell the target word by shooting ducks in the\n" +
                "correct letter order.", accentStyle);
        wordObj.setWrap(true);
        content.add(wordObj).width(contentW).left().padBottom(8f).row();

        String[] wordBullets = {
                "* Ducks spawn carrying individual letters. The target word is\n  displayed on screen.",
                "* Shoot the ducks in the correct spelling order -- left to right.",
                "* Shooting the wrong letter wastes a bullet without any progress.",
                "* Watch out for distraction ducks -- they carry no relevant letter\n  and shooting them only wastes bullets.",
                "* Clear the wave by shooting all letters in the correct sequence."
        };
        for (String bullet : wordBullets) {
            Label b = new Label(bullet, bodyStyle);
            b.setWrap(true);
            content.add(b).width(contentW).left().padBottom(4f).row();
        }
        content.add(new Label("", bodyStyle)).padBottom(12f).row(); // spacer

        // ── Lose Conditions ──────────────────────────────────────────────────
        Label loseTitle = new Label("Lose Conditions", sectionStyle);
        content.add(loseTitle).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        String[][] loseConds = {
                { "TIME", "Time runs out -- Complete the objective before the timer hits zero." },
                { "AMMO", "Bullets run out -- You have a limited supply per wave. Spend them wisely." },
                { "DUCK", "All ducks leave the screen -- Don't let them all fly away! (Num Hunt only)" }
        };
        for (String[] cond : loseConds) {
            Table condRow = new Table();
            Label tag = new Label("[" + cond[0] + "]", emojiStyle);
            Label desc = new Label(cond[1], bodyStyle);
            desc.setWrap(true);
            condRow.add(tag).left().row();
            condRow.add(desc).width(contentW - 20f).padLeft(20f).left();
            content.add(condRow).width(contentW).left().padBottom(16f).row();
        }
        content.add(new Label("", bodyStyle)).padBottom(12f).row(); // spacer

        // ── Tips ─────────────────────────────────────────────────────────────
        Label tipsTitle = new Label("Tips", sectionStyle);
        content.add(tipsTitle).left().padTop(10f).padBottom(6f).row();

        addDivider(content, contentW);

        String[] tips = {
                "* In Num Hunt, plan your operations ahead -- overshooting the\n  target means starting over for that wave!",
                "* In Word Hunt, scan the screen first before shooting so you can\n  locate the correct letter duck quickly.",
                "* Avoid wasting bullets on distraction ducks in Word Hunt --\n  every shot counts."
        };
        for (String tip : tips) {
            Label t = new Label(tip, tipStyle);
            t.setWrap(true);
            content.add(t).width(contentW).left().padBottom(6f).row();
        }
        content.add(new Label("", bodyStyle)).padBottom(20f).row(); // bottom spacer

        // ── ScrollPane ───────────────────────────────────────────────────────
        float scrollH = panelH - 110f;
        ScrollPane scroll = new ScrollPane(content);
        scroll.setPosition(panelX + 20f, panelY + 20f);
        scroll.setSize(panelW - 40f, scrollH);
        scroll.setScrollingDisabled(true, false);
        stage.addActor(scroll);
        stage.setScrollFocus(scroll);

        // ── Close (X) button — top-right of panel ─────────────────────────────
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
