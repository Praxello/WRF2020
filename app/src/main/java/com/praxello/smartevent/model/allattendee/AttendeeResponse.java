package com.praxello.smartevent.model.allattendee;


import java.util.ArrayList;

public class AttendeeResponse {

    public String Message;
    public String Responsecode;
    public ArrayList<AttendeeData> Data ;

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

    public ArrayList<AttendeeData> getData() {
        if(this.Data==null){
            Data=new ArrayList<>();
        }
        return Data;
    }

    public void setData(ArrayList<AttendeeData> data) {
        Data = data;
    }
}
