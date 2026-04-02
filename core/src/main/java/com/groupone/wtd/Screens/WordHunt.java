package com.groupone.wtd.Screens;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.Entities.Word;
import com.groupone.wtd.GameLauncher;

public class WordHunt extends MainGame {
    int shots = 0;
    int baseScore = 200;
    int level = 0;
    char hitChar;

    char[] duckLetters;
    Queue<Character> targetWordHitArr;
    ArrayList<Integer> hitCharIndex;

    Word wordTargetGenerator = new Word();
    String wordTargetStr;

    BitmapFont charFont;
    FreeTypeFontGenerator charGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter charParameter;
    BitmapFont stringFont;
    FreeTypeFontGenerator stringGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter stringParameter;
    private final GlyphLayout hudLayout = new GlyphLayout();

    @Override
    protected void customLogic() {
        if (targetWordHitArr.isEmpty()) {
            ducks.size = 0;
            level++;
            timeRemaining += Math.max(10f, 20 - level * 2f);
            points += (int) Math.max(0, baseScore - (Math.max(0, shots - 5) * 20)) + (Math.max(0, baseScore - (Math.max(0, shots - 5) * 20)) * (streak /  10f));
            shots = 0;
            generateRandomWord();
            spawnDucks();
        }

        if (gun.charCheckIfOutOfAmmo() && !targetWordHitArr.isEmpty()) {
            isGameOver = true;
            gameOver.setReason(0);
        } else if (timeRemaining <= 0f) {
            isGameOver = true;
            gameOver.setReason(2);
        }
    }

    @Override
    protected void customInput() {
    }

    @Override
    protected void drawCustomMidGround() {
        for (Duck duck : ducks) {
            charFont.draw(game.batch,
                    String.valueOf(duck.getLetter()),
                    duck.getHitBox().getX() + duck.getHitBox().getWidth() / 2f - 10,
                    duck.getHitBox().getY() + duck.getHitBox().getHeight());

        }
    }

    @Override
    protected void drawCustomForeground() {
    }

    @Override
    protected void customClick() {
        gun.charConsumeAmmo();
        shots++;
    }

    @Override
    protected boolean isClickable() {
        return gun.charCheckAvailableAmmo();
    }

    @Override
    protected void customShape() {
        drawRoundedRect(20, 10, 180, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 250, 10, 230, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 380, 10, 120, 90, 10);
        float streakBoxWidth = 170f;
        float streakBoxHeight = 90f;
        float pointsBoxX = GameLauncher.gameWidth - 250;
        float pointsBoxWidth = 230f;
        float streakBoxX = pointsBoxX + pointsBoxWidth - streakBoxWidth; // align right edge with points box
        drawRoundedRect(streakBoxX, 110, streakBoxWidth, streakBoxHeight, 10);
    }

    protected void customText() {
        GlyphLayout letterWidth = new GlyphLayout();

        float targetBoxX = 20f;
        float targetBoxWidth = 180f;
        float boxY = 10f;
        float boxHeight = 90f;
        float pointsBoxX = GameLauncher.gameWidth - 250;
        float pointsBoxWidth = 230f;
        float ammoBoxX = GameLauncher.gameWidth - 380;
        float ammoBoxWidth = 120f;

        hudLayout.setText(stringFont, "TARGET");
        float targetLabelX = targetBoxX + (targetBoxWidth - hudLayout.width) / 2f;
        float targetLabelY = boxY + boxHeight - 12f;
        stringFont.draw(game.batch, "TARGET", targetLabelX, targetLabelY);

        hudLayout.setText(charFont, wordTargetStr);
        float xPosition = targetBoxX + (targetBoxWidth - hudLayout.width) / 2f;
        float targetValueY = boxY + 52f; // keep original vertical placement for target letters

        for (int i = 0; i < wordTargetStr.length(); i++) {
            char c = wordTargetStr.charAt(i);

            if (hitCharIndex.contains(i)) {
                charFont.setColor(1, 1, 1, 1f);
            } else {
                charFont.setColor(1, 1, 1, 0.5f);
            }

            String letter = String.valueOf(c);
            letterWidth.setText(charFont, letter);
            charFont.draw(game.batch, letter, xPosition, targetValueY);

            xPosition += letterWidth.width;
        }
        // Reset full opacity before drawing other HUD values
        charFont.setColor(1, 1, 1, 1f);
        float streakBoxWidth = 170f;
        float streakBoxHeight = 90f;
        float streakBoxX = pointsBoxX + pointsBoxWidth - streakBoxWidth;
        float streakBoxY = 110f;

        hudLayout.setText(stringFont, "STREAK");
        float streakLabelX = streakBoxX + (streakBoxWidth - hudLayout.width) / 2f;
        float streakLabelY = streakBoxY + streakBoxHeight - 12f;
        stringFont.draw(game.batch, "STREAK", streakLabelX, streakLabelY);

        String streakDisplay = streak + "x";
        hudLayout.setText(charFont, streakDisplay);
        float streakValueX = streakBoxX + (streakBoxWidth - hudLayout.width) / 2f;
        float streakValueY = streakBoxY + 52f; // keep original vertical placement
        charFont.draw(game.batch, streakDisplay, streakValueX, streakValueY);

        hudLayout.setText(stringFont, "POINTS");
        float pointsLabelX = pointsBoxX + (pointsBoxWidth - hudLayout.width) / 2f;
        float pointsLabelY = boxY + boxHeight - 12f;
        stringFont.draw(game.batch, "POINTS", pointsLabelX, pointsLabelY);

        charFont.setColor(1, 1, 1, 1f);
        String pointsValue = String.format("%07d", points);
        hudLayout.setText(charFont, pointsValue);
        float pointsValueX = pointsBoxX + (pointsBoxWidth - hudLayout.width) / 2f;
        float pointsValueY = boxY + 52f; // keep original vertical placement
        charFont.draw(game.batch, pointsValue, pointsValueX, pointsValueY);

        hudLayout.setText(stringFont, "AMMO");
        float ammoLabelX = ammoBoxX + (ammoBoxWidth - hudLayout.width) / 2f;
        float ammoLabelY = boxY + boxHeight - 12f;
        stringFont.draw(game.batch, "AMMO", ammoLabelX, ammoLabelY);

        String ammoValue = gun.charCurrentAmmo + "/5";
        hudLayout.setText(charFont, ammoValue);
        float ammoValueX = ammoBoxX + (ammoBoxWidth - hudLayout.width) / 2f;
        float ammoValueY = boxY + 52f; // keep original vertical placement for ammo
        charFont.draw(game.batch, ammoValue, ammoValueX, ammoValueY);
    }

    @Override
    protected void customFailHit() {
        streak = 0;
        updateMusicByStreak();
    }

    public WordHunt(GameLauncher game) {
        super(game);
        SoundManager.setGameMusic();
        SoundManager.playBackgroundMusic();
        charGenerator = new FreeTypeFontGenerator(Gdx.files.internal("number_font.ttf"));
        charParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        charParameter.size = 45;
        charParameter.color = Color.WHITE;
        charParameter.borderColor = Color.BLACK;
        charParameter.borderWidth = 2;
        charFont = charGenerator.generateFont(charParameter);

        stringGenerator = new FreeTypeFontGenerator(Gdx.files.internal("string_font.ttf"));
        stringParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        stringParameter.size = 25;
        stringParameter.color = Color.WHITE;
        stringParameter.borderColor = Color.BLACK;
        stringParameter.borderWidth = 2;
        stringFont = stringGenerator.generateFont(stringParameter);

        generateRandomWord();
        spawnDucks();
    }

    @Override
    protected void spawnDucks() {
        ducks.clear();

        for (int i = 0; i < duckLetters.length; i++) {
            ducks.add(new Duck(game, 300, 500, changeDirectionCD, 0.5f, duckLetters[i]));
            System.out.println(ducks);
        }
        changeDirectionCD = Math.max(1.2f - (float) (0.08 * streak), 0.3f);
        for(Duck duck : ducks){
            duck.setMinMaxVelocity(streak);
            duck.setChangeDirectionCD(changeDirectionCD);
        }
    }

    @Override
    protected boolean customDuckHit(Duck duck) {
        if (targetWordHitArr.isEmpty() || duck.getLetter() != targetWordHitArr.peek()) {
            return false;
        }

        char duckLetter = Character.toUpperCase(duck.getLetter());
        streak++;
        if (targetWordHitArr.contains(duckLetter)) {

            int charIndex = wordTargetStr.indexOf(duckLetter);

            while (charIndex != -1) {
                if (!hitCharIndex.contains(charIndex)) {
                    hitCharIndex.add(charIndex);
                    break;
                }
                charIndex = wordTargetStr.indexOf(duckLetter, charIndex + 1);
            }

            System.out.println("CURRENT INDEX" + hitCharIndex);
            targetWordHitArr.poll();
            System.out.println("Updated Array: " + targetWordHitArr);
            hitChar = duckLetter;
            gun.charReloadGun();
        }
        updateMusicByStreak();
        return true;
    }

    private void updateMusicByStreak() {
        if (streak < 3)
            SoundManager.setGameMusicLevel(1);
        else if (streak < 5)
            SoundManager.setGameMusicLevel(2);
        else
            SoundManager.setGameMusicLevel(3);
    }

    public void generateRandomWord() {
        wordTargetStr = wordTargetGenerator.getRandomWord().toUpperCase();

        int wordLength = wordTargetStr.length();
        System.out.println(wordLength);

        hitCharIndex = new ArrayList<>();
        targetWordHitArr = new LinkedList<>();
        duckLetters = new char[wordLength + 2];

        for (char word : wordTargetStr.toCharArray()) {
            targetWordHitArr.add(word);
        }

        List<Character> targetWordList = new ArrayList<>(targetWordHitArr);
        for (int i = 0; i < duckLetters.length; i++) {
            if (i < wordLength) {
                duckLetters[i] = targetWordList.get(i);
            } else {
                char randomChar;
                do {
                    randomChar = (char) ('A' + MathUtils.random(0, 25));
                } while (contains(duckLetters, randomChar, i));
                duckLetters[i] = randomChar;
            }
        }
    }

    public boolean contains(char[] array, char letter, int limit) {
        for (int j = 0; j < limit; j++) {
            if (array[j] == letter)
                return true;
        }
        return false;
    }
}
