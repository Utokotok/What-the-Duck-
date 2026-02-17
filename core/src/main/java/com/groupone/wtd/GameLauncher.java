package com.groupone.wtd;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.groupone.wtd.Assets.AnimationManager;
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Screens.MainMenu;
import com.groupone.wtd.Utils.Utils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameLauncher extends Game {

    public SpriteBatch batch;
    public AssetManager manager = Assets.getManager();
    public FitViewport viewport;
    public static int gameHeight = 720;
    public static int gameWidth = 1280;
    public static final float GROUND_HEIGHT = 140f;
    public static boolean isMainMenuAnimation = false;
    public BitmapFont numberFont;
    public BitmapFont duckNumberFont;
    public BitmapFont UIFont3;
    public BitmapFont UIFont2;
    public BitmapFont UIFont1;

    @Override
    public void create() {
        AnimationManager.setGame(this);
        numberFont = Utils.createFont("number_font.ttf", 45, 2);
        UIFont3 = Utils.createFont("string_font.ttf", 25, 2);
        UIFont2 = Utils.createFont("number_font.ttf", 40, 3);
        UIFont1 = Utils.createFont("string_font.ttf", 150, 5);
        duckNumberFont = Utils.createFont("number_font.ttf", 70, 2);


        Assets.loadMenuAssets();
        batch = new SpriteBatch();
        viewport = new FitViewport(gameWidth, gameHeight);
        setScreen(new MainMenu(this));
    }

}
