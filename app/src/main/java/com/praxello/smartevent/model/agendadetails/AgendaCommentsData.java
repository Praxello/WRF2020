package com.praxello.smartevent.model.agendadetails;

import android.os.Parcel;
import android.os.Parcelable;

public class AgendaCommentsData implements Parcelable {

    public String commentId;
    public String sessionId ;
    public Integer userId;
    public String comment;
    public String commentDateTime;

    protected AgendaCommentsData(Parcel in) {
        commentId = in.readString();
        sessionId = in.readString();
        userId = in.readInt();
        comment = in.readString();
        commentDateTime = in.readString();
    }

    public static final Creator<AgendaCommentsData> CREATOR = new Creator<AgendaCommentsData>() {
        @Override
        public AgendaCommentsData createFromParcel(Parcel in) {
            return new AgendaCommentsData(in);
        }

        @Override
        public AgendaCommentsData[] newArray(int size) {
            return new AgendaCommentsData[size];
        }
    };

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDateTime() {
        return commentDateTime;
    }

    public void setCommentDateTime(String commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentId);
        dest.writeString(sessionId);
        dest.writeInt(userId);
        dest.writeString(comment);
        dest.writeString(commentDateTime);
    }
}
