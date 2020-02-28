package com.praxello.smartevent.model.comments;

import com.praxello.smartevent.model.agendadetails.AgendaCommentsData;

import java.util.ArrayList;

public class LoadPreviousCommentResponse {

    public String Message;
    public ArrayList<LatestCommentData> Data;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<LatestCommentData> getData() {
        if(this.Data==null){
            Data=new ArrayList<>();
        }

        return Data;
    }

    public void setData(ArrayList<LatestCommentData> data) {
        Data = data;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
