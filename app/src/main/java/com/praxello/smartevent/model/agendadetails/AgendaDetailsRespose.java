package com.praxello.smartevent.model.agendadetails;

import java.util.ArrayList;

public class AgendaDetailsRespose {

    public String Message;
    public ArrayList<AgendaData> Data;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<AgendaData> getData() {
        return Data;
    }

    public void setData(ArrayList<AgendaData> data) {
        Data = data;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
