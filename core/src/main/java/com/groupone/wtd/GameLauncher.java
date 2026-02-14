package com.groupone.wtd;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.groupone.wtd.Assets.AnimationManager;
import com.groupone.wtd.Assets.Assets;
import com.groupone.wtd.Screens.MainMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameLauncher extends Game {

    public SpriteBatch batch;
    public AssetManager manager = Assets.getManager();
    public FitViewport viewport;
    public static int gameHeight = 720;
    public static int gameWidth = 1280;
    public static final float GROUND_HEIGHT = 140f;

    @Override
    public void create() {
        AnimationManager.setGame(this);
        Assets.loadMenuAssets();
        batch = new SpriteBatch();
        viewport = new FitViewport(gameWidth, gameHeight);
        setScreen(new MainMenu(this));
    }

}
