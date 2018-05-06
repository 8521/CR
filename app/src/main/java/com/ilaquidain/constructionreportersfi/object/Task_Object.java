package com.ilaquidain.constructionreportersfi.object;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;


public class Task_Object implements Parcelable, Serializable {

    private static final long serialVersionUID = 9L;

    private String TaskName;
    private String Act_dwgspec;
    private String Act_No;
    private Integer Act_Compliance;
    private String TaskDescription;

    public Integer getAct_Compliance() {
        return Act_Compliance;
    }

    public void setAct_Compliance(Integer act_Compiance) {
        Act_Compliance = act_Compiance;
    }

    public void setAct_dwgspec(String act_dwgspec) {
        Act_dwgspec = act_dwgspec;
    }

    public void setAct_No(String act_No) {
        Act_No = act_No;
    }

    public String getAct_dwgspec() {
        return Act_dwgspec;
    }

    public String getAct_No() {
        return Act_No;
    }

    public void setTaskName(String s0){
        TaskName = s0;}
    public String getTaskName(){return TaskName;}

    public void setTaskDescription(String s1){
        TaskDescription = s1;}
    public String getTaskDescription(){return TaskDescription;}

    public Task_Object(){
        TaskName = " ";
        TaskDescription = " ";
        Act_dwgspec = " ";
        Act_Compliance =0;
        Act_No = " ";
    }

    public Task_Object(Parcel in){
        TaskName = in.readString();
        TaskDescription = in.readString();
        Act_dwgspec = in.readString();
        Act_No = in.readString();
        Act_Compliance = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TaskName);
        dest.writeString(TaskDescription);
        dest.writeString(Act_dwgspec);
        dest.writeString(Act_No);
        dest.writeInt(Act_Compliance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new Task_Object(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Task_Object[size];
        }
    };
}
