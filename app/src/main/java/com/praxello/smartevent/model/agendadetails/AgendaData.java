package com.praxello.smartevent.model.agendadetails;

import java.util.ArrayList;

public class AgendaData {

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
    public String Comments;

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

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
