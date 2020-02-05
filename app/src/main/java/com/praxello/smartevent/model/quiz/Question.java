package com.praxello.smartevent.model.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Parcelable {
    @SerializedName("questionId")
    @Expose
    public String questionId;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("categoryId")
    @Expose
    public String categoryId;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("option1")
    @Expose
    public String option1;
    @SerializedName("option2")
    @Expose
    public String option2;
    @SerializedName("option3")
    @Expose
    public String option3;
    @SerializedName("option4")
    @Expose
    public String option4;
    @SerializedName("correctoption")
    @Expose
    public String correctoption;
    @SerializedName("ansdes")
    @Expose
    public String ansdes;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectoption() {
        return correctoption;
    }

    public void setCorrectoption(String correctoption) {
        this.correctoption = correctoption;
    }

    public String getAnsdes() {
        return ansdes;
    }

    public void setAnsdes(String ansdes) {
        this.ansdes = ansdes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionId);
        dest.writeString(this.userId);
        dest.writeString(this.categoryId);
        dest.writeString(this.question);
        dest.writeString(this.option1);
        dest.writeString(this.option2);
        dest.writeString(this.option3);
        dest.writeString(this.option4);
        dest.writeString(this.correctoption);
        dest.writeString(this.ansdes);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.questionId = in.readString();
        this.userId = in.readString();
        this.categoryId = in.readString();
        this.question = in.readString();
        this.option1 = in.readString();
        this.option2 = in.readString();
        this.option3 = in.readString();
        this.option4 = in.readString();
        this.correctoption = in.readString();
        this.ansdes = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}