package com.praxello.smartevent.model.comments;

import com.praxello.smartevent.model.agendadetails.AgendaCommentsData;

import java.util.ArrayList;

public class CommentsResponse {

    public String Message;
    public ArrayList<LatestCommentData> CommentsData;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<LatestCommentData> getCommentsData() {
        return CommentsData;
    }

    public void setCommentsData(ArrayList<LatestCommentData> commentsData) {
        CommentsData = commentsData;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
