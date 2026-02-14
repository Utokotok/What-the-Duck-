package com.groupone.wtd.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.GameLauncher;

import java.util.Arrays;

public class NumHunt extends MainGame {
    float currentNumber = 0;
    float numberToGuess;
    int points;
    int[] duckNumbers;
    BitmapFont font;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    @Override
    protected void customLogic() {
        if(currentNumber == numberToGuess || ducks.size == 0){
            currentNumber = 0;
            generateRandomEquation(1, 10);
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
            font.draw(game.batch, String.valueOf(duck.getNumber()), duck.getHitBox().getX() + duck.getHitBox().getWidth() / 2f - 10, duck.getHitBox().getY() + duck.getHitBox().getHeight());
        }
    }

    @Override
    protected void drawCustomForeground() {
        gun.getModeSprite(mousePos).draw(game.batch);
        font.draw(game.batch, String.valueOf(currentNumber), 20, 100);
        font.draw(game.batch, String.valueOf(numberToGuess), 20, 50);
    }

    public NumHunt(GameLauncher game) {
        super(game);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("pixel_font.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);
        generateRandomEquation(1, 10);
        spawnDucks();
    }

    @Override
    protected void spawnDucks() {
        ducks.clear();
        for(int i = 0; i < duckNumbers.length; i++){
            ducks.add(new Duck(game, 300, 500, 0.5f, 0.5f, duckNumbers[i]));
        }
    }

    @Override
    protected void customDuckHit(Duck duck) {
        switch (gun.gunMode){
            case 1 -> {currentNumber += duck.getNumber();}
            case 2 -> {currentNumber -= duck.getNumber();}
            case 3 -> {currentNumber *= duck.getNumber();}
            case 4 -> {currentNumber /= duck.getNumber();}
        }
    }

    public void generateRandomEquation(int startRange, int endRange){
        float number = 0;
        int[] randomNumbers;
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

            for(int i = 0; i < randomOperators.size; i++){
                number = calculate(number, randomNumbers[counter], operators[randomOperators.get(i)]);
                System.out.println(number);
                counter++;
            }
        } while(number == 0);

        numberToGuess = number;
        duckNumbers = randomNumbers;
        System.out.println(Arrays.toString(duckNumbers));
    }

    private float calculate(float a, float b, String operator) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0;
        };
    }

}
