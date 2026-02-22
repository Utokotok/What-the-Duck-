package com.groupone.wtd.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    int shots = 0;
    int baseScore = 200;
    float changeDirectionCD = 1.2f;
    int level = 0;
    int streak = 1;
    int maxNumber = 10;
    int minNumber = 1;
    boolean isSpawning = false;

    @Override
    protected void customLogic() {
        if(isGameOver) return;

        if(gun.checkIfOutOfAmmo() || ducks.size == 0 && (currentNumber != numberToGuess) && !isSpawning){
            isGameOver = true;
        }

        if(currentNumber == numberToGuess){
            level++;
            maxNumber = Math.min(maxNumber + 5, 20);
            minNumber = Math.min(minNumber + 2, 10);
            currentNumber = 0;
            points += (int) ((baseScore / shots) + (baseScore / shots) * (streak /  10f));
            shots = 0;
            generateRandomEquation(1, 10);
            gun.reloadGun();
            spawnDucks();
        }

        changeDirectionCD = Math.max(1.2f - (float) Math.log10(streak), 0.3f);

        for(Duck duck : ducks){
            duck.setMinMaxVelocity(streak);
            duck.setChangeDirectionCD(changeDirectionCD);
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.setColor(0,0,0, 0.5f);
        drawRoundedRect(20, 10, 180, 90, 10);
        drawRoundedRect(210, 10, 180, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 250, 10, 230, 90, 10);
        drawRoundedRect(GameLauncher.gameWidth - 380, 10, 120, 90, 10);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        customText();
    }

    @Override
    protected void customFailHit() {
        streak = 1;
    }

    private void drawRoundedRect(float x, float y, float width, float height, float radius) {
        shapeRenderer.rect(x + radius, y, width - (2 * radius), height);
        shapeRenderer.rect(x, y + radius, radius, height - (2 * radius));
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - (2 * radius));
        shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f); // BL
        shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f); // BR
        shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f); // TR
        shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f); // TL
    }

    protected void customText() {
        game.batch.begin();
        game.UIFont3.draw(game.batch, "GIVEN", 70, 88);
        game.numberFont.draw(game.batch, Utils.padNumber(numberToGuess), 35, 63);
        game.UIFont3.draw(game.batch, "CURRENT", 250, 88);
        game.numberFont.draw(game.batch, Utils.padNumber(currentNumber), 225, 63);
        game.UIFont3.draw(game.batch, "POINTS", GameLauncher.gameWidth - 175, 88);
        game.numberFont.draw(game.batch, String.format("%07d", points), GameLauncher.gameWidth - 235, 63);
        game.UIFont3.draw(game.batch, "AMMO", GameLauncher.gameWidth - 352, 88);
        game.numberFont.draw(game.batch, gun.availableGunMode[gun.gunMode - 1] + "/5", GameLauncher.gameWidth - 360, 63);
        game.batch.end();
    }

    public NumHunt(GameLauncher game) {
        super(game);
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
                isSpawning = false;
            }
            }, 2f);
    }

    @Override
    protected void customDuckHit(Duck duck) {
        streak++;
        switch (gun.gunMode){
            case 1 -> {currentNumber += duck.getNumber();}
            case 2 -> {currentNumber -= duck.getNumber();}
            case 3 -> {currentNumber *= duck.getNumber();}
            case 4 -> {currentNumber /= duck.getNumber();}
        }
    }

    public void generateRandomEquation(int startRange, int endRange){
        int[] randomNumbers;
        int number = 0;
        do{
            Array<Integer> randomOperators = new Array<>();
            randomOperators.addAll(0, 1, 2, 3);
            randomOperators.shuffle();
            randomOperators.truncate(3);
            String[] operators = {"+", "-", "*", "/"};
            randomNumbers = new int[4];

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
