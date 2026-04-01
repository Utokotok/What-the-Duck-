package com.groupone.wtd.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.groupone.wtd.Assets.SoundManager;
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.GameLauncher;
import com.groupone.wtd.Utils.Utils;

import java.util.Arrays;

public class NumHunt extends MainGame {
    int currentNumber = 0;
    int numberToGuess;
    int[] duckNumbers;
    int duckSpawnSize = 6;
    int shots = 0;
    int baseScore = 200;
    float changeDirectionCD = 1.2f;
    int level = 0;
    int maxNumber = 10;
    int minNumber = 1;
    boolean isSpawning = false;
    private static final Color POSITIVE_COLOR = Color.valueOf("1dd1a1");
    private static final Color NEGATIVE_COLOR = Color.valueOf("ff6b6b");
    private final GlyphLayout hudLayout = new GlyphLayout();

    @Override
    protected void customLogic() {
        if(isGameOver) return;

        if(gun.checkIfOutOfAmmo()){
            isGameOver = true;
            gameOver.setReason(0);
        } else if(ducks.size == 0 && (currentNumber != numberToGuess) && !isSpawning){
            isGameOver = true;
            gameOver.setReason(1);
        } else if(timeRemaining <= 0f){
            isGameOver = true;
            gameOver.setReason(2);
        }

        if(currentNumber == numberToGuess){
            level++;
            timeRemaining += Math.max(10f, 20 - level * 2f);
            currentNumber = 0;
            // old scoring
            // points += (int) ((baseScore / shots) + (baseScore / shots) * (streak /  10f));
            points += (int) ((baseScore) + (baseScore) * (streak /  10f));
            shots = 0;
            duckSpawnSize = Math.max(level % 5 == 0 ? duckSpawnSize - 1 : duckSpawnSize, 4);
            generateRandomEquation(1, 10);
            gun.reloadGun();
            spawnDucks();
        }
        
    }

    @Override
    protected void customInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            gun.changeMode(1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            gun.changeMode(2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            gun.changeMode(3);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
            gun.changeMode(4);
        }
    }

    @Override
    protected void drawCustomMidGround() {
        for(Duck duck : ducks){
            game.duckNumberFont.draw(game.batch, String.valueOf(duck.getNumber()), duck.getHitBox().getX() + duck.getHitBox().getWidth() / 2f - 10, duck.getHitBox().getY() + duck.getHitBox().getHeight());
        }
    }

    @Override
    protected void drawCustomForeground() {
        gun.getModeSprite(mousePos).draw(game.batch);
    }

    @Override
    protected void customClick() {
        gun.consumeAmmo(gun.gunMode);
        shots++;
    }

    @Override
    protected boolean isClickable(){
        return gun.checkAvailableAmmo(gun.gunMode);
    }

    @Override
    protected void customShape() {
        drawRoundedRect(20, 10, 180, 90, 10);
        drawRoundedRect(210, 10, 180, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 250, 10, 230, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 380, 10, 120, 90, 10);

        float streakBoxWidth = 170f;
        float streakBoxHeight = 90f;
        float pointsBoxX = GameLauncher.gameWidth - 250;
        float pointsBoxWidth = 230f;
        float streakBoxX = pointsBoxX + pointsBoxWidth - streakBoxWidth; // right edges align
        drawRoundedRect(streakBoxX, 110, streakBoxWidth, streakBoxHeight, 10);
    }

    @Override
    protected void customFailHit() {
        streak = 0;
        updateMusicByStreak();
    }

    @Override
    protected void customText() {
        float givenBoxX = 20f;
        float currentBoxX = 210f;
        float boxY = 10f;
        float boxHeight = 90f;
        float givenBoxWidth = 180f;
        float currentBoxWidth = 180f;
        float pointsBoxX = GameLauncher.gameWidth - 250;
        float pointsBoxWidth = 230f;
        float ammoBoxX = GameLauncher.gameWidth - 380;
        float ammoBoxWidth = 120f;

        hudLayout.setText(game.UIFont3, "GIVEN");
        float givenLabelX = givenBoxX + (givenBoxWidth - hudLayout.width) / 2f;
        float givenLabelY = boxY + boxHeight - 12f;
        game.UIFont3.draw(game.batch, "GIVEN", givenLabelX, givenLabelY);

        String givenValue = numberToGuess > 0 ? "+" + numberToGuess : String.valueOf(numberToGuess);
        Color givenColor = numberToGuess > 0 ? POSITIVE_COLOR : numberToGuess < 0 ? NEGATIVE_COLOR : Color.WHITE;
        hudLayout.setText(game.numberFont, givenValue);
        float givenValueX = givenBoxX + (givenBoxWidth - hudLayout.width) / 2f;
        float givenValueY = boxY + 52f; // keep original vertical placement to avoid overlapping the title
        game.numberFont.setColor(givenColor);
        game.numberFont.draw(game.batch, givenValue, givenValueX, givenValueY);
        game.numberFont.setColor(Color.WHITE);

        hudLayout.setText(game.UIFont3, "CURRENT");
        float currentLabelX = currentBoxX + (currentBoxWidth - hudLayout.width) / 2f;
        float currentLabelY = boxY + boxHeight - 12f;
        game.UIFont3.draw(game.batch, "CURRENT", currentLabelX, currentLabelY);

        String currentValue = currentNumber > 0 ? "+" + currentNumber : String.valueOf(currentNumber);
        Color currentColor = currentNumber > 0 ? POSITIVE_COLOR : currentNumber < 0 ? NEGATIVE_COLOR : Color.WHITE;
        hudLayout.setText(game.numberFont, currentValue);
        float currentValueX = currentBoxX + (currentBoxWidth - hudLayout.width) / 2f;
        float currentValueY = boxY + 52f; // keep original vertical placement to avoid overlapping the title
        game.numberFont.setColor(currentColor);
        game.numberFont.draw(game.batch, currentValue, currentValueX, currentValueY);
        game.numberFont.setColor(Color.WHITE);

        float streakBoxWidth = 170f;
        float streakBoxHeight = 90f;
        float streakBoxX = pointsBoxX + pointsBoxWidth - streakBoxWidth;
        float streakBoxY = 110f;

        hudLayout.setText(game.UIFont3, "STREAK");
        float streakLabelX = streakBoxX + (streakBoxWidth - hudLayout.width) / 2f;
        float streakLabelY = streakBoxY + streakBoxHeight - 12f;
        game.UIFont3.draw(game.batch, "STREAK", streakLabelX, streakLabelY);

        hudLayout.setText(game.numberFont, String.valueOf(streak));
        float streakValueX = streakBoxX + (streakBoxWidth - hudLayout.width) / 2f;
        float streakValueY = streakBoxY + 52f; // keep original vertical placement
        game.numberFont.draw(game.batch, String.valueOf(streak), streakValueX, streakValueY);

        hudLayout.setText(game.UIFont3, "POINTS");
        float pointsLabelX = pointsBoxX + (pointsBoxWidth - hudLayout.width) / 2f;
        float pointsLabelY = boxY + boxHeight - 12f;
        game.UIFont3.draw(game.batch, "POINTS", pointsLabelX, pointsLabelY);

        String pointsValue = String.format("%07d", points);
        hudLayout.setText(game.numberFont, pointsValue);
        float pointsValueX = pointsBoxX + (pointsBoxWidth - hudLayout.width) / 2f;
        float pointsValueY = boxY + 52f; // keep original vertical placement
        game.numberFont.draw(game.batch, pointsValue, pointsValueX, pointsValueY);

        hudLayout.setText(game.UIFont3, "AMMO");
        float ammoLabelX = ammoBoxX + (ammoBoxWidth - hudLayout.width) / 2f;
        float ammoLabelY = boxY + boxHeight - 12f;
        game.UIFont3.draw(game.batch, "AMMO", ammoLabelX, ammoLabelY);

        String ammoValue = gun.availableGunMode[gun.gunMode - 1] + "/5";
        hudLayout.setText(game.numberFont, ammoValue);
        float ammoValueX = ammoBoxX + (ammoBoxWidth - hudLayout.width) / 2f;
        float ammoValueY = boxY + 52f; // keep original vertical placement for ammo
        game.numberFont.draw(game.batch, ammoValue, ammoValueX, ammoValueY);
    }

    public NumHunt(GameLauncher game) {
        super(game);
        SoundManager.setGameMusic();
        SoundManager.playBackgroundMusic();
        generateRandomEquation(minNumber, maxNumber);
        spawnDucks();
    }

    @Override
    protected void spawnDucks() {
        ducks.clear();
        isSpawning = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for(int i = 0; i < duckNumbers.length; i++){
                    ducks.add(new Duck(game, 300, 500, changeDirectionCD, 0.5f, duckNumbers[i]));
                }
                changeDirectionCD = Math.max(1.2f - (float) (0.08 * streak), 0.3f);

                for(Duck duck : ducks){
                    duck.setMinMaxVelocity(streak);
                    duck.setChangeDirectionCD(changeDirectionCD);
                }
                
                isSpawning = false;
            }
            }, 2f);
    }

    @Override
    protected boolean customDuckHit(Duck duck) {
        streak++;
        switch (gun.gunMode){
            case 1 -> {currentNumber += duck.getNumber();}
            case 2 -> {currentNumber -= duck.getNumber();}
            case 3 -> {currentNumber *= duck.getNumber();}
            case 4 -> {currentNumber /= duck.getNumber();}
        }
        updateMusicByStreak();
        return true;
    }

    private void updateMusicByStreak() {
        if (streak < 3) SoundManager.setGameMusicLevel(1);
        else if (streak < 5) SoundManager.setGameMusicLevel(2);
        else SoundManager.setGameMusicLevel(3);
    }

    public void generateRandomEquation(int startRange, int endRange){
        int[] randomNumbers;
        int number = 0;
        do{
            Array<Integer> randomOperators = new Array<>();
            randomOperators.addAll(0, 1, 2, 3);
            Utils.shuffleArray(randomOperators);
            randomOperators.truncate(3);
            String[] operators = {"+", "-", "*", "/"};
            randomNumbers = new int[duckSpawnSize];

            for(int i = 0; i < randomNumbers.length; i++){
                randomNumbers[i] = MathUtils.random(startRange, endRange);
            }

            int counter = 0;

            counter++;
            for(int i = 0; i < randomOperators.size; i++){
                System.out.println(number);
                if(randomOperators.get(i) == 3){
                    if(number % randomNumbers[counter] == 0){
                        number = calculate(number, randomNumbers[counter], operators[randomOperators.get(i)]);
                    } else{
                        int guessNum = randomNumbers[counter];
                        while(number % guessNum != 0 && guessNum > 1){
                            guessNum--;
                        }
                        randomNumbers[counter] = guessNum;
                        number = calculate(number, randomNumbers[counter], operators[randomOperators.get(i)]);
                    }
                } else{
                    number = calculate(number, randomNumbers[counter], operators[randomOperators.get(i)]);
                }
                counter++;
            }
        } while(number == 0);
        System.out.println(number);
        numberToGuess = number;
        duckNumbers = randomNumbers;
        System.out.println(Arrays.toString(duckNumbers));
    }

    private int calculate(int a, int b, String operator) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0;
        };
    }

}
