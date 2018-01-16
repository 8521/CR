package com.ilaquidain.constructionreporter.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Project_Object implements Parcelable,Serializable, Cloneable {

    private String ProjectId;
    private String ProjectName;
    private String ProjectRefNo;
    private String ProjectAddress;
    private String BitmapFileAddress;
    private ArrayList<Report_Object> ProjectReports;
    private ArrayList<Contractor_Object> ProjectContractors;
    private ArrayList<ArrayList<Worker_Object>> ListAvailableManpower;
    private ArrayList<ArrayList<Worker_Object>> ListAvailableEquipment;
    private ArrayList<Task_Object> ListAvailableTasks;
    private ArrayList<String> WorkerGroups;
    private ArrayList<String> EquipmentGroups;

    public void setProjectId(String s1){ProjectId =s1;}
    public String getProjectId(){return ProjectId;}

    public void setProjectName(String s2){ProjectName = s2;}
    public String getProjectName(){return ProjectName;}

    public void setProjectRefNo(String s3){ProjectRefNo =s3;}
    public String getProjectRefNo(){return ProjectRefNo;}

    public void setProjectAddress(String s4){ProjectAddress = s4;}
    public String getProjectAddress(){return ProjectAddress;}

    public void setBitmapFileAddress(String s6){BitmapFileAddress = s6;}
    public String getBitmapFileAddress(){return BitmapFileAddress;}

    public void setProjectReports(ArrayList<Report_Object> s5){ProjectReports = s5;}
    public ArrayList<Report_Object> getProjectReports(){return ProjectReports;}

    public void setProjectContractors(ArrayList<Contractor_Object> s6){ProjectContractors = s6;}
    public ArrayList<Contractor_Object> getProjectContractors(){return ProjectContractors;}

    public void setListAvailableManpower(ArrayList<ArrayList<Worker_Object>> mListAvailableManpower){ListAvailableManpower = mListAvailableManpower;}
    public ArrayList<ArrayList<Worker_Object>> getListAvailableManpower(){return ListAvailableManpower;}

    public void setListAvailableEquipment(ArrayList<ArrayList<Worker_Object>> mListAvailableEquipment){ListAvailableEquipment = mListAvailableEquipment;}
    public ArrayList<ArrayList<Worker_Object>> getListAvailableEquipment(){return ListAvailableEquipment;}

    public void setWorkerGroups(ArrayList<String> mWorkerGroups){WorkerGroups = mWorkerGroups;}
    public ArrayList<String> getWorkerGroups(){return WorkerGroups;}

    public void setEquipmentGroups(ArrayList<String> mEquipmentGroups){EquipmentGroups = mEquipmentGroups;}
    public ArrayList<String> getEquipmentGroups(){return EquipmentGroups;}

    public void setListAvailableTasks(ArrayList<Task_Object> s7){ListAvailableTasks = s7;}
    public ArrayList<Task_Object> getListAvailableTasks(){return ListAvailableTasks;}

    public Project_Object deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Project_Object) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public Project_Object(){
        this.ProjectId = UUID.randomUUID().toString();
        this.ProjectName = " ";
        this.ProjectRefNo = " ";
        this.ProjectAddress = " ";
        this.BitmapFileAddress =null;
        this.ProjectReports = new ArrayList<>();
        this.ProjectContractors = new ArrayList<>();
        this.ListAvailableManpower = new ArrayList<>();
        this.ListAvailableEquipment = new ArrayList<>();
        this.ListAvailableTasks = new ArrayList<>();
        this.WorkerGroups = new ArrayList<>();
        this.EquipmentGroups = new ArrayList<>();
    }

    private Project_Object(Parcel in){
        ProjectId = in.readString();
        ProjectName = in.readString();
        ProjectRefNo = in.readString();
        ProjectAddress = in.readString();
        BitmapFileAddress = in.readString();

        ProjectReports = new ArrayList<>();
        in.readList(ProjectReports,Report_Object.class.getClassLoader());
        ProjectContractors = new ArrayList<>();
        in.readList(ProjectContractors,Contractor_Object.class.getClassLoader());
        ListAvailableManpower = new ArrayList<>();
        in.readList(ListAvailableManpower,Worker_Object.class.getClassLoader());
        ListAvailableEquipment = new ArrayList<>();
        in.readList(ListAvailableEquipment,Worker_Object.class.getClassLoader());
        ListAvailableTasks = new ArrayList<>();
        in.readList(ListAvailableTasks,Task_Object.class.getClassLoader());
        WorkerGroups = new ArrayList<>();
        in.readList(WorkerGroups,null);
        EquipmentGroups = new ArrayList<>();
        in.readList(EquipmentGroups,null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ProjectId);
        dest.writeString(ProjectName);
        dest.writeString(ProjectRefNo);
        dest.writeString(ProjectAddress);
        dest.writeString(BitmapFileAddress);
        dest.writeList(ProjectReports);
        dest.writeList(ProjectContractors);
        dest.writeList(ListAvailableManpower);
        dest.writeList(ListAvailableEquipment);
        dest.writeList(ListAvailableTasks);
        dest.writeList(WorkerGroups);
        dest.writeList(EquipmentGroups);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Project_Object createFromParcel(Parcel in) {
            return new Project_Object(in);
        }

        public Project_Object[] newArray(int size) {
            return new Project_Object[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
