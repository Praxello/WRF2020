package com.praxello.smartevent.model;

public class LikesResponse {

    public String Message;
    public String Likes;
    public String UserIds;
    public String isLiked;
    public String Responsecode;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getUserIds() {
        return UserIds;
    }

    public void setUserIds(String userIds) {
        UserIds = userIds;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(String responsecode) {
        Responsecode = responsecode;
    }
}
