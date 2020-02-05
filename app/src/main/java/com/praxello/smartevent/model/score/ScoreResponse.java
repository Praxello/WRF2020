package com.praxello.smartevent.model.score;

import java.util.ArrayList;

public class ScoreResponse {

    public String Message;
    public String Responsecode;
    public ArrayList<ScoresData> Scores;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }

    public ArrayList<ScoresData> getScores() {
        return Scores;
    }

    public void setScores(ArrayList<ScoresData> scores) {
        Scores = scores;
    }
}
