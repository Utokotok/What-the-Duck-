package com.groupone.wtd.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Random;

public class Word {
    private ArrayList<String> targetWord;
    private Random random;

    public Word() {
        targetWord = new ArrayList<>();
        random = new Random();
        checkword();
    }

    public void checkword() {
        FileHandle file = Gdx.files.internal("words.txt");
        String[] lines = file.readString().split("\\r?\\n");

        for (String line: lines) {
            if (!line.trim().isEmpty()) {
                targetWord.add(line.trim());
            }
        }
    }

    public String getRandomWord() {
        if (targetWord.isEmpty()) return "Empty";
        return targetWord.get(random.nextInt(targetWord.size()));
    }
}
