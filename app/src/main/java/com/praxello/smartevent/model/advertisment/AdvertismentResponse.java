package com.praxello.smartevent.model.advertisment;

import java.util.ArrayList;

public class AdvertismentResponse {

    public ArrayList<AdvertismentData> Data;
    public String Message;
    public String Responsecode;

    public ArrayList<AdvertismentData> getData() {
        return Data;
    }

    public void setData(ArrayList<AdvertismentData> data) {
        Data = data;
    }

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
}
