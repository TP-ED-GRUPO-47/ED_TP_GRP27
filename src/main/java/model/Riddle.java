package model;

import structures.linear.ArrayUnorderedList;
import java.util.Iterator;

public class Riddle {
    private String question;
    private ArrayUnorderedList<String> options;
    private int correctAnswerIndex; // 0 a N

    public Riddle(String question, ArrayUnorderedList<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() { return question; }

    public ArrayUnorderedList<String> getOptions() { return options; }

    public boolean checkAnswer(int index) {
        return index == correctAnswerIndex;
    }
}