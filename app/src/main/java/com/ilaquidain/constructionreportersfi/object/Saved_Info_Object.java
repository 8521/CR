package com.ilaquidain.constructionreportersfi.object;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;

public class Saved_Info_Object implements Parcelable, Serializable{

    private static final long serialVersionUID = 1L;

    private ArrayList<ArrayList<String>> ListasOpciones;
    public void setListasOpciones(ArrayList<ArrayList<String>> mlistasopciones){ListasOpciones = mlistasopciones;}
    public ArrayList<ArrayList<String>> getListasOpciones(){return ListasOpciones;}

    private ArrayList<Project_Object> SavedProjects;
    public void setSavedProjects(ArrayList<Project_Object> mreports){SavedProjects = mreports;}
    public ArrayList<Project_Object>getSavedProjects(){return SavedProjects;}

    private Report_Object temp_report;

    public Report_Object getTemp_report() {
        return temp_report;
    }

    public void setTemp_report(Report_Object temp_report) {
        this.temp_report = temp_report;
    }

    public Saved_Info_Object(){
        this.ListasOpciones = new ArrayList<>();
        this.SavedProjects = new ArrayList<>();
        this.temp_report = new Report_Object();
    }

    private Saved_Info_Object(Parcel in){
        ListasOpciones = new ArrayList<>();
        in.readList(ListasOpciones,String.class.getClassLoader());

        SavedProjects = new ArrayList<>();
        in.readList(SavedProjects,Project_Object.class.getClassLoader());

        temp_report = (Report_Object)in.readSerializable();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(ListasOpciones);
        dest.writeList(SavedProjects);
        dest.writeSerializable(temp_report);
    }

    public static final Creator CREATOR = new Creator() {
        public Saved_Info_Object createFromParcel(Parcel in) {
            return new Saved_Info_Object(in);
        }

        public Saved_Info_Object[] newArray(int size) {
            return new Saved_Info_Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
