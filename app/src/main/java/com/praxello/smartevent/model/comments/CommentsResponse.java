package com.praxello.smartevent.model.comments;

import com.praxello.smartevent.model.agendadetails.CommentsData1;

import java.util.ArrayList;

public class CommentsResponse {

    public String Message;
    public ArrayList<CommentsData1> CommentsData;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<CommentsData1> getCommentsData() {
        return CommentsData;
    }

    public void setCommentsData(ArrayList<CommentsData1> commentsData) {
        CommentsData = commentsData;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}