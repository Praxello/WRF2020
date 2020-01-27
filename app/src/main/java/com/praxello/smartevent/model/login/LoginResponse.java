package com.praxello.smartevent.model.login;


public class LoginResponse {
    LoginData Data;
    public String Responsecode;
    public String Message;

    public LoginData getData() {
        return Data;
    }

    public void setData(LoginData data) {
        Data = data;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
