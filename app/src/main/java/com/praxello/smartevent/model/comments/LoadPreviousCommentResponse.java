package com.praxello.smartevent.model.comments;

import com.praxello.smartevent.model.agendadetails.CommentsData1;

import java.util.ArrayList;

public class LoadPreviousCommentResponse {

    public String Message;
    public ArrayList<CommentsData1> Data;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<CommentsData1> getData() {
        return Data;
    }

    public void setData(ArrayList<CommentsData1> data) {
        Data = data;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
