package com.praxello.smartevent.model.agendadetails;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AgendaData implements Parcelable {

    public String sessionId;
    public String title;
    public String subject;
    public String details;
    public String slotId;
    public String sessionDate;
    public String sequenceNo;
    public String isActive;
    public ArrayList<SpeakersName> Speakers ;
    public String isBookmarked;
    public String isLiked;
    public String Likes;
    public ArrayList<AgendaCommentsData> Comments;
    public String slotTitle;
    public String sessionLocation;
    public String sessionType;


    protected AgendaData(Parcel in) {
        sessionId = in.readString();
        title = in.readString();
        subject = in.readString();
        details = in.readString();
        slotId = in.readString();
        sessionDate = in.readString();
        sequenceNo = in.readString();
        isActive = in.readString();
        Speakers = in.createTypedArrayList(SpeakersName.CREATOR);
        isBookmarked = in.readString();
        isLiked = in.readString();
        Likes = in.readString();
        Comments = in.createTypedArrayList(AgendaCommentsData.CREATOR);
        slotTitle = in.readString();
        sessionLocation = in.readString();
        sessionType = in.readString();
    }

    public static final Creator<AgendaData> CREATOR = new Creator<AgendaData>() {
        @Override
        public AgendaData createFromParcel(Parcel in) {
            return new AgendaData(in);
        }

        @Override
        public AgendaData[] newArray(int size) {
            return new AgendaData[size];
        }
    };

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public ArrayList<SpeakersName> getSpeakers() {
        return Speakers;
    }

    public void setSpeakers(ArrayList<SpeakersName> speakers) {
        Speakers = speakers;
    }

    public String getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(String isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public ArrayList<AgendaCommentsData> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<AgendaCommentsData> comments) {
        Comments = comments;
    }

    public String getSlotTitle() {
        return slotTitle;
    }

    public void setSlotTitle(String slotTitle) {
        this.slotTitle = slotTitle;
    }

    public String getSessionLocation() {
        return sessionLocation;
    }

    public void setSessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sessionId);
        dest.writeString(title);
        dest.writeString(subject);
        dest.writeString(details);
        dest.writeString(slotId);
        dest.writeString(sessionDate);
        dest.writeString(sequenceNo);
        dest.writeString(isActive);
        dest.writeTypedList(Speakers);
        dest.writeString(isBookmarked);
        dest.writeString(isLiked);
        dest.writeString(Likes);
        dest.writeTypedList(Comments);
        dest.writeString(slotTitle);
        dest.writeString(sessionLocation);
        dest.writeString(sessionType);
    }
}
