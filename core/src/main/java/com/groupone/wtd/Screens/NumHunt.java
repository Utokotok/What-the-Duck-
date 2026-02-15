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
import com.groupone.wtd.Entities.Duck;
import com.groupone.wtd.GameLauncher;

import java.util.Arrays;

public class NumHunt extends MainGame {
    float currentNumber = 0;
    float numberToGuess;
    int[] duckNumbers;
    int shots = 0;
    int baseScore = 200;
    BitmapFont numberFont;
    FreeTypeFontGenerator numberGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter numberParameter;
    BitmapFont duckNumberFont;
    FreeTypeFontGenerator duckNumberGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter duckNumberParameter;
    BitmapFont stringFont;
    FreeTypeFontGenerator stringGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter stringParameter;


    @Override
    protected void customLogic() {
        if(currentNumber == numberToGuess){
            currentNumber = 0;
            points +=  baseScore / shots;
            shots = 0;
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
            duckNumberFont.draw(game.batch, String.valueOf(duck.getNumber()), duck.getHitBox().getX() + duck.getHitBox().getWidth() / 2f - 10, duck.getHitBox().getY() + duck.getHitBox().getHeight());
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
        if(gun.checkIfOutOfAmmo() && (currentNumber != numberToGuess)){
            isGameOver = true;
        }
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

    private void drawRoundedRect(float x, float y, float width, float height, float radius) {
        shapeRenderer.rect(x + radius, y, width - (2 * radius), height);
        shapeRenderer.rect(x, y + radius, radius, height - (2 * radius));
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - (2 * radius));
        shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f); // BL
        shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f); // BR
        shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f); // TR
        shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f); // TL
    }

    private String padNumber(float value) {
        int whole = (int) Math.abs(value);
        int decimal = (int) ((Math.abs(value) * 100) % 100);
        return value > 0 ? "+" + String.format("%02d.%02d", whole, Math.abs(decimal)) : "-" + String.format("%02d.%02d", whole, Math.abs(decimal));
    }

    protected void customText() {
        game.batch.begin();
        stringFont.draw(game.batch, "GIVEN", 70, 88);
        numberFont.draw(game.batch, padNumber(numberToGuess), 35, 63);
        stringFont.draw(game.batch, "CURRENT", 250, 88);
        numberFont.draw(game.batch, padNumber(currentNumber), 225, 63);
        stringFont.draw(game.batch, "POINTS", GameLauncher.gameWidth - 175, 88);
        numberFont.draw(game.batch, String.format("%07d", points), GameLauncher.gameWidth - 235, 63);
        stringFont.draw(game.batch, "AMMO", GameLauncher.gameWidth - 352, 88);
        numberFont.draw(game.batch, gun.availableGunMode[gun.gunMode - 1] + "/5", GameLauncher.gameWidth - 360, 63);
//        numberFont.draw(game.batch, String.valueOf(numberToGuess), 20, 50);
        game.batch.end();
    }

    public NumHunt(GameLauncher game) {
        super(game);
        numberGenerator = new FreeTypeFontGenerator(Gdx.files.internal("number_font.ttf"));
        numberParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        numberParameter.size = 45;
        numberParameter.color = Color.WHITE;
        numberParameter.borderColor = Color.BLACK;
        numberParameter.borderWidth = 2;
        numberFont = numberGenerator.generateFont(numberParameter);

        stringGenerator = new FreeTypeFontGenerator(Gdx.files.internal("string_font.ttf"));
        stringParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        stringParameter.size = 25;
        stringParameter.color = Color.WHITE;
        stringParameter.borderColor = Color.BLACK;
        stringParameter.borderWidth = 2;
        stringFont = stringGenerator.generateFont(stringParameter);

        duckNumberGenerator = new FreeTypeFontGenerator(Gdx.files.internal("number_font.ttf"));
        duckNumberParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        duckNumberParameter.size = 70;
        duckNumberParameter.color = Color.WHITE;
        duckNumberParameter.borderColor = Color.BLACK;
        duckNumberParameter.borderWidth = 2;
        duckNumberFont = duckNumberGenerator.generateFont(duckNumberParameter);

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
        currentNumber = Math.round(currentNumber * 100f) / 100f;
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
                number = Math.round(calculate(number, randomNumbers[counter], operators[randomOperators.get(i)]) * 100f) / 100f;
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
