package com.praxello.smartevent.model.speaker;

import java.util.ArrayList;

public class SpeakerResponse {
    public String Message;
    public String Responsecode;
    public ArrayList<SpeakerData> Data;

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

    public ArrayList<SpeakerData> getData() {
        return Data;
    }

    public void setData(ArrayList<SpeakerData> data) {
        Data = data;
    }
}
