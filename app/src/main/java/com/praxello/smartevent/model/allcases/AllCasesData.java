package com.praxello.smartevent.model.allcases;

import android.os.Parcel;
import android.os.Parcelable;

public class AllCasesData implements Parcelable {

    public String caseId;
    public String caseTitle;
    public String caseDetails;
    public String pdflink;
    public String owner;
    public String isActive;
    public String photoUrl;
    public String Submission;

    protected AllCasesData(Parcel in) {
        caseId = in.readString();
        caseTitle = in.readString();
        caseDetails = in.readString();
        pdflink = in.readString();
        owner = in.readString();
        isActive = in.readString();
        photoUrl = in.readString();
        Submission = in.readString();
    }

    public static final Creator<AllCasesData> CREATOR = new Creator<AllCasesData>() {
        @Override
        public AllCasesData createFromParcel(Parcel in) {
            return new AllCasesData(in);
        }

        @Override
        public AllCasesData[] newArray(int size) {
            return new AllCasesData[size];
        }
    };

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public String getPdflink() {
        return pdflink;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSubmission() {
        return Submission;
    }

    public void setSubmission(String submission) {
        Submission = submission;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caseId);
        dest.writeString(caseTitle);
        dest.writeString(caseDetails);
        dest.writeString(pdflink);
        dest.writeString(owner);
        dest.writeString(isActive);
        dest.writeString(photoUrl);
        dest.writeString(Submission);
    }
}
