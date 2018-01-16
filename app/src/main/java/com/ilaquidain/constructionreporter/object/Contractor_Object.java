package com.ilaquidain.constructionreporter.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Contractor_Object implements Parcelable,Serializable {

    private String contractor_id;
    private String contractor_name;
    private String contractor_address;
    private String contractor_ref_no;
    private String contractor_phone;

    public void setContractor_id(String s1){
        contractor_id =s1;}
    public String getContractor_id(){return contractor_id;}

    public void setContractor_name(String s2){
        contractor_name = s2;}
    public String getContractor_name(){return contractor_name;}

    public void setContractor_address(String s3){
        contractor_address =s3;}
    public String getContractor_address(){return contractor_address;}

    public void setContractor_ref_no(String s4){
        contractor_ref_no = s4;}
    public String getContractor_ref_no(){return contractor_ref_no;}

    public void setContractor_phone(String s6){
        contractor_phone = s6;}
    public String getContractor_phone(){return contractor_phone;}

    public Contractor_Object(){
        this.contractor_id = UUID.randomUUID().toString();
        this.contractor_name = null;
        this.contractor_address = null;
        this.contractor_ref_no = null;
        this.contractor_phone =null;
    }

    private Contractor_Object(Parcel in){
        contractor_id = in.readString();
        contractor_name = in.readString();
        contractor_address = in.readString();
        contractor_ref_no = in.readString();
        contractor_phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contractor_id);
        dest.writeString(contractor_name);
        dest.writeString(contractor_address);
        dest.writeString(contractor_ref_no);
        dest.writeString(contractor_phone);
    }

    public static final Creator CREATOR = new Creator() {
        public Contractor_Object createFromParcel(Parcel in) {
            return new Contractor_Object(in);
        }

        public Contractor_Object[] newArray(int size) {
            return new Contractor_Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
