package com.groupone.wtd.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.groupone.wtd.Assets.SoundManager;

public class Utils {
    public static TextureRegion[] generateSheet(Texture texture, int rows, int cols){
        int count = 0;
        TextureRegion[]  sheet = new TextureRegion[rows * cols];
        TextureRegion[][] temp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                sheet[count] = temp[i][j];
                count++;
            }
        }

        return sheet;
    }

    public static  BitmapFont createFont(String path, int size, int border) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = border;
        parameter.genMipMaps = true;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        return generator.generateFont(parameter);
    }

    public static String padNumber(int value) {
        return value > 0 ? "+" + String.format("%04d", Math.abs(value)) : "-" + String.format("%04d", Math.abs(value));
    }

    public static ImageButton createButton(Texture texture, float x, float y, float scale, boolean isCentered){
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(texture);
        style.down = new TextureRegionDrawable(texture).tint(Color.GRAY);
        style.over = new TextureRegionDrawable(texture).tint(Color.LIGHT_GRAY);

        ImageButton button = new ImageButton(style);
        button.setTransform(true);
        button.setScale(scale);
        float posX = isCentered ? x - (button.getWidth() * scale / 2f) : x;
        float posY = isCentered ? y - (button.getHeight() * scale / 2f) : y;

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                SoundManager.playClick();
            }
        });
        button.setPosition(posX, posY);

        return button;
    }

    public static void shuffleArray(Array<Integer> arr){
        for(int i = arr.size - 1; i > 0; i--){
            int j = MathUtils.random(0, i);
            int temp = arr.get(j);
            arr.set(j, arr.get(i));
            arr.set(i, temp);
        }
    }
}
