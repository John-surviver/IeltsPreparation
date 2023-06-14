package com.devghost.ieltspreparation;

public class Question {
    private final String question;
    private final String[] answerOptions;
    private final String correctAnswer;
    private String selectedAnswer;
    private final String Link;

    public Question(String question, String[] answerOptions, String correctAnswer, String picLink) {
        this.question = question;
        this.answerOptions = answerOptions;
        this.correctAnswer = correctAnswer;
        this.Link= picLink;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getPicLink() {
        return Link;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
