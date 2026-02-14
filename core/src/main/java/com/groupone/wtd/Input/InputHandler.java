package com.groupone.wtd.Input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class InputHandler extends InputAdapter {
    final float CLICK_DEBOUNCE = 200f;
    long previousClick = 0;
    public Vector2 cursorPos = new Vector2();
    public Vector2 scroll = new Vector2();
    boolean isClicked = false;
    public float scrollY = 0;

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        long currentTime = TimeUtils.millis();
        if (currentTime - previousClick >= CLICK_DEBOUNCE) {
            previousClick = TimeUtils.millis();
            cursorPos.x = x;
            cursorPos.y = y;
            isClicked = true;
            return true;
        }
        return false;
    }

    public boolean wasClicked(){
        if(isClicked){
            isClicked = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(float x, float y){
        if(y > 0){
            scrollY = 1;
        } else if(y < 0){
            scrollY = -1;
        }
        return true;
    }

}
