package com.praxello.smartevent.model.comments;

import java.util.ArrayList;

public class CommentsResponse {

    public String Message;
    public ArrayList<CommentData1> CommentsData;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<CommentData1> getCommentsData() {
        return CommentsData;
    }

    public void setCommentsData(ArrayList<CommentData1> commentsData) {
        CommentsData = commentsData;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
