package com.praxello.smartevent.model.allcases;

import java.util.ArrayList;

public class AllCases {
    public String Message;
    public ArrayList<AllCasesData> Data;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<AllCasesData> getData() {
        return Data;
    }

    public void setData(ArrayList<AllCasesData> data) {
        Data = data;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
