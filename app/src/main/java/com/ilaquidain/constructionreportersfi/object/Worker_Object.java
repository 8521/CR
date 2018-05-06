package com.ilaquidain.constructionreportersfi.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public class Worker_Object implements Parcelable, Serializable {

    private static final long serialVersionUID = 11L;

    private String IdNumber;
    private String FirstName;
    private String LastName;
    private String Trade;
    private String Level;
    private String Company;
    private Double RegHours;
    private Double OThours;
    private Double DThours;
    private String Activity;

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public void setOThours(Double OThours) {
        this.OThours = OThours;
    }

    public void setDThours(Double DThours) {
        this.DThours = DThours;
    }

    public String getLastName() {
        return LastName;
    }

    public String getLevel() {
        return Level;
    }

    public Double getOThours() {
        return OThours;
    }

    public Double getDThours() {
        return DThours;
    }

    public void setIdNumber(String s0){IdNumber = s0;}
    public String getIdNumber(){return IdNumber;}

    public void setFirstName(String s1){
        FirstName = s1;}
    public String getFirstName(){return FirstName;}

    public void setTrade(String s2){Trade = s2;}
    public String getTrade(){return Trade;}

    public void setCompany(String s3){Company = s3;}
    public String getCompany(){return Company;}

    public void setRegHours(Double s4){
        RegHours = s4;}
    public Double getRegHours(){return RegHours;}

    public void setActivity(String s5){Activity = s5;}
    public String getActivity(){return Activity;}

    public Worker_Object deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Worker_Object) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public Worker_Object(){
        IdNumber = "";
        FirstName = "";
        LastName = "";
        Level = "";
        OThours =0.00;
        DThours = 0.00;
        Trade = "";
        Company = "";
        RegHours = 0.00;
        Activity = "";
    }

    public Worker_Object(Parcel in){
        IdNumber = in.readString();
        FirstName = in.readString();
        Trade = in.readString();
        Company = in.readString();
        RegHours = in.readDouble();
        Activity = in.readString();
        LastName = in.readString();
        Level = in.readString();
        OThours = in.readDouble();
        DThours = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IdNumber);
        dest.writeString(FirstName);
        dest.writeString(Trade);
        dest.writeString(Company);
        dest.writeDouble(RegHours);
        dest.writeString(Activity);
        dest.writeString(Level);
        dest.writeString(LastName);
        dest.writeDouble(DThours);
        dest.writeDouble(OThours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel source) {
            return new Worker_Object(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Worker_Object[size];
        }
    };
}
