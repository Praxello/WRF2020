package com.praxello.smartevent.model.agendadetails;

import android.os.Parcel;
import android.os.Parcelable;

public class SpeakersName implements Parcelable {

    public String sessionid;
    public Integer userid;
    public String usertype;
    public String firstname;
    public String lastname;
    public String email;
    public String mobile;
    public String city;
    public String state;
    public String country;

    protected SpeakersName(Parcel in) {
        sessionid = in.readString();
        userid = in.readInt();
        usertype = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        mobile = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
    }

    public static final Creator<SpeakersName> CREATOR = new Creator<SpeakersName>() {
        @Override
        public SpeakersName createFromParcel(Parcel in) {
            return new SpeakersName(in);
        }

        @Override
        public SpeakersName[] newArray(int size) {
            return new SpeakersName[size];
        }
    };

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sessionid);
        dest.writeInt(userid);
        dest.writeString(usertype);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
    }
}
