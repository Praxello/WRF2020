package com.praxello.smartevent.model.allconference;

import java.util.ArrayList;

public class AllConferenceResponse {
    public String Message;
    public String Responsecode;
    public ArrayList<AllConferenceData> Data;

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

    public ArrayList<AllConferenceData> getData() {
        return Data;
    }

    public void setData(ArrayList<AllConferenceData> data) {
        Data = data;
    }
}
