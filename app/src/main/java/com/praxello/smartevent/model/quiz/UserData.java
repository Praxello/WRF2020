package com.praxello.smartevent.model.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {
   /* @SerializedName("Data")
    @Expose
    public Data data;*/
    @SerializedName("Responsecode")
    @Expose
    public long responsecode;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("conferenceid")
    @Expose
    public String conferenceid;
    @SerializedName("TransactionId")
    @Expose
    public String TransactionId;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getConferenceid() {
        return conferenceid;
    }

    public void setConferenceid(String conferenceid) {
        this.conferenceid = conferenceid;
    }

    /*public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
*/
    public long getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(long responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeParcelable(this.data, flags);
        dest.writeLong(this.responsecode);
        dest.writeString(this.message);
        dest.writeString(this.conferenceid);
        dest.writeString(this.TransactionId);
    }

    protected UserData(Parcel in) {
       // this.data = in.readParcelable(Data.class.getClassLoader());
        this.responsecode = in.readLong();
        this.message = in.readString();
        this.conferenceid = in.readString();
        this.TransactionId = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
