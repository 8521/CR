package com.ilaquidain.constructionreportersfi.activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.fragments.f0003_MenuDocuments2;
import com.ilaquidain.constructionreportersfi.object.Image_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.qcreports.f00101_QCnewreport;
import com.ilaquidain.constructionreportersfi.qcreports.f0010_MenuQCReports;
import com.ilaquidain.constructionreportersfi.qareports.f00201_QAnewreport;
import com.ilaquidain.constructionreportersfi.fragments.f0002_MenuDocuments;
import com.ilaquidain.constructionreportersfi.fragments.f0001_MenuProjects;
import com.ilaquidain.constructionreportersfi.qareports.f0020_MenuQAReports;
import com.ilaquidain.constructionreportersfi.fragments.f0999_Settings;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import com.ilaquidain.constructionreportersfi.object.Task_Object;
import com.ilaquidain.constructionreportersfi.object.Worker_Object;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Saved_Info_Object saved_info;
    public void setSaved_info(Saved_Info_Object msaved_info){saved_info = msaved_info;}
    public Saved_Info_Object getSaved_info(){return saved_info;}

    private final static String TAG = "MainActivityTAG";
    private Document doc;
    private PdfTemplate t;
    private PdfWriter writer;
    private Paragraph par;
    private PdfPTable table;
    private PdfPCell per;
    private final BaseColor lightgray = new BaseColor(230,230,230);
    private String tempstring;
    private SharedPreferences msharedpreferences;
    private SharedPreferences.Editor editor;
    private Report_Object mcurrentreport;
    private ArrayList<Image_Object> arrayofphotosforreport;
    private Project_Object mcurrentproject;
    private Integer mreportnumber, reporttypenumber;
    private BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    private ArrayList<Image> imagenes;
    private final static String OriginatorName = "OriginatorName";
    private final static String OriginatorPosition = "OriginatorPosition";
    private final static String OriginatorCompany = "OrignatorCompany";
    private final static String IncludeConstructionDate = "IncludeConstructionDate";
    private final static String IncludeManpower = "IncludeManpower";
    private final static String IncludeEquipment = "IncludeEquipment";
    private final static String IncludePhotos = "IncludePhotos";
    private final static String IncludePhotoDate = "IncludePhotoDate";
    private final static String PhotosQuality = "PhotosQuality";
    private Font Cal11BU,Cal11B, Cal10, Cal10B_Gray, Cal6, Cal7, Cal8, Cal8B, Cal9, Cal10orange;
    private Bitmap bitmaphoto;
    private File pdfFolderFile,pdfSecFolderFile,pdfThirdFolderFile;
    private SharedPreferences mpref;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onPause() {
        super.onPause();
        SaveInfoToMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        msharedpreferences = this.getPreferences(Context.MODE_PRIVATE);
        this.GetInfoFromMemory();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }


        //CheckIfAlreadyInstalled();

        FragmentManager fm = getFragmentManager();
        Fragment fgmt = new f0001_MenuProjects();
        fm.beginTransaction()
                .add(R.id.MainFrame,fgmt)
                .addToBackStack(getResources().getString(R.string.level00))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getFragmentManager();
            f0999_Settings fgmt = new f0999_Settings();
            fm.beginTransaction()
                    .replace(R.id.MainFrame,fgmt)
                    .addToBackStack(getResources().getString(R.string.level09))
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==0){
            if(grantResults.length>0 &&
                    grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                Toast toast = Toast.makeText(this,"PDF Could Not be Saved in Device",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        int index = this.getFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        final FragmentManager fm = this.getFragmentManager();
        Fragment fgmt;
        switch (tag){
            case "level00"://Menu with projects
            case "level01"://Menu showing possible documents to create
                fgmt = new f0001_MenuProjects();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level00))
                        .addToBackStack(getResources().getString(R.string.level00))
                        .commit();
                break;
            case "level02":
                fgmt = new f0002_MenuDocuments();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level01))
                        .addToBackStack(getResources().getString(R.string.level01))
                        .commit();
                break;
            case "level10"://QC Reports
            case "level20"://QA Reports
            case "level30"://Inspection Material
            case "level40"://Steel erection
            case "level50"://Safety observation
            case "level60"://Timesheet
                fgmt = new f0003_MenuDocuments2();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level02))
                        .addToBackStack(getResources().getString(R.string.level02))
                        .commit();
                break;
            case "level101": //new report
            case "level102": //edit report
            case "level103": //view report
                fgmt = new f0010_MenuQCReports();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level10))
                        .addToBackStack(getResources().getString(R.string.level10))
                        .commit();
                break;
            case "level1011": //report info
            case "level1012": //report activities
            case "level1013": //report equipman
            case "level1014": //report photos
                fgmt = new f00101_QCnewreport();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level101))
                        .addToBackStack(getResources().getString(R.string.level101))
                        .commit();
                break;
            case "level201": //new report
            case "level202": //edit report
            case "level203": //view report
                fgmt = new f0020_MenuQAReports();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level20))
                        .addToBackStack(getResources().getString(R.string.level20))
                        .commit();
                break;
            case "level2011": //report info
            case "level2012": //report activities
            case "level2013": //report photos
                fgmt = new f00201_QAnewreport();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.level201))
                        .addToBackStack(getResources().getString(R.string.level201))
                        .commit();
                break;

        }
    }

    /*private void CheckIfAlreadyInstalled() {
        Boolean firstime = msharedpreferences.getBoolean("FirstTime",true);
        editor = msharedpreferences.edit();
        if(firstime){
            CreateSampleReport();
            editor.putBoolean("FirstTime",false);
            editor.apply();
        }
    }*/
    /*private void CreateSampleReport() {
        Project_Object sampleproject = new Project_Object();
        sampleproject.setContractor_name("Sample Project");
        sampleproject.setContractor_id(UUID.randomUUID().toString());
        sampleproject.setContractor_address("Bridge No.: 0001");
        sampleproject.setContractor_ref_no("500 ABC Street, Los Angeles, California");

        editor.putString(OriginatorName,"John Smith");
        editor.putString(OriginatorPosition,"Field Engineer");
        editor.putString(OriginatorCompany,"United Contractors");
        editor.apply();

        qa_report_obj samplereport = new qa_report_obj();

        sampleproject = AddPhotos(sampleproject);
        samplereport = AddPhotosReport(samplereport);

        ArrayList<Worker_Object> reportworkers = new ArrayList<>();
        for(int i=0;i<10;i++){
            Worker_Object worker = new Worker_Object();
            worker.setFirstName("Worker "+String.valueOf(i));
            worker.setCompany("United Contractors");
            worker.setActivity("South Tower Construction");
            worker.setRegHours("8");
            reportworkers.add(worker);
        }
        samplereport.setSelectedWorkers(reportworkers);

        ArrayList<Task_Object> tasks = new ArrayList<>();
        Task_Object task = new Task_Object();
        task.setTaskName("Foundation Construction");
        task.setTaskDescription(getResources().getString(R.string.sampletaskdescription));
        tasks.add(task);
        samplereport.setSelectedTasks(tasks);

        samplereport.getQcreportinfo().set(4,"United Contractors");
        samplereport.getQcreportinfo().set(5,"Fog");
        samplereport.getQcreportinfo().set(6,"QC Daily Report");
        samplereport.getQcreportinfo().set(7,"Structural");
        samplereport.getQcreportinfo().set(8,"07/24/2017");
        samplereport.getQcreportinfo().set(10,"05:00");
        samplereport.getQcreportinfo().set(11,"14:00");

        ArrayList<Project_Object> savedprojects = new ArrayList<>();
        sampleproject.getqa_reports().add(samplereport);
        savedprojects.add(sampleproject);
        this.getSaved_info().setSavedProjects(savedprojects);

        this.GeneratePDFReport(sampleproject,sampleproject.getqa_reports().indexOf(samplereport));
    }
    private qa_report_obj AddPhotosReport(qa_report_obj samplereport) {
        ArrayList<Image_Object> defaultactivityimages = new ArrayList<>();
        Bitmap sampleimage1 = BitmapFactory.decodeResource(getResources(),R.drawable.samp1);
        String StorePathLogo4 = "samp1.jpg";
        try{
            File f = new File(this.getApplicationContext().getFilesDir(),StorePathLogo4);
            FileOutputStream fos = new FileOutputStream(f);
            sampleimage1.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Image_Object image1 = new Image_Object();
            image1.setPathDevice(f.getPath());
            image1.setPhotoDate("Monday 24, July 2017");
            image1.setPhotoActivity("Sample Project");
            image1.setPhotoDescription("View Looking North");
            defaultactivityimages.add(image1);
            fos.close();
        }catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        Bitmap sampleimage2 = BitmapFactory.decodeResource(getResources(),R.drawable.samp2);
        String StorePathLogo5 = "samp2.jpg";
        try{
            File f = new File(this.getApplicationContext().getFilesDir(),StorePathLogo5);
            FileOutputStream fos = new FileOutputStream(f);
            sampleimage2.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Image_Object image1 = new Image_Object();
            image1.setPathDevice(f.getPath());
            image1.setPhotoDate("Monday 24, July 2017");
            image1.setPhotoActivity("Sample Project");
            image1.setPhotoDescription("Aerial View");
            defaultactivityimages.add(image1);
            fos.close();
        }catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        Bitmap sampleimage3 = BitmapFactory.decodeResource(getResources(),R.drawable.samp3);
        String StorePathLogo6 = "samp3.jpg";
        try{
            File f = new File(this.getApplicationContext().getFilesDir(),StorePathLogo6);
            FileOutputStream fos = new FileOutputStream(f);
            sampleimage3.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Image_Object image1 = new Image_Object();
            image1.setPathDevice(f.getPath());
            image1.setPhotoDate("Monday 24, July 2017");
            image1.setPhotoActivity("Sample Project");
            image1.setPhotoDescription("View Looking South");
            defaultactivityimages.add(image1);
            fos.close();
        }catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        samplereport.setQcreport_selectedphotos(defaultactivityimages);
        return samplereport;
    }
    private Project_Object AddPhotos(Project_Object sampleproject) {
        Bitmap companylogo = BitmapFactory.decodeResource(getResources(),R.drawable.ggc);
        String StoredPathLogo = "logo.jpg";
        try {
            File f = new File(this.getApplicationContext().getFilesDir(),StoredPathLogo);
            FileOutputStream fos = new FileOutputStream(f);
            companylogo.compress(Bitmap.CompressFormat.JPEG,90,fos);
            fos.close();
        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        Bitmap signature = BitmapFactory.decodeResource(getResources(),R.drawable.ggs);
        String StoredPathLogo2 = "signature.jpg";
        try {
            File f = new File(this.getApplicationContext().getFilesDir(),StoredPathLogo2);
            FileOutputStream fos = new FileOutputStream(f);
            signature.compress(Bitmap.CompressFormat.JPEG,90,fos);
            fos.close();
        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
        Bitmap projectlogo = BitmapFactory.decodeResource(getResources(),R.drawable.ggi);
        String StoredPathLogo3 = sampleproject.getContractor_id()+".jpg";
        try {
            File f = new File(this.getApplicationContext().getFilesDir(),StoredPathLogo3);
            FileOutputStream fos = new FileOutputStream(f);
            projectlogo.compress(Bitmap.CompressFormat.JPEG,90,fos);
            fos.close();
        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }

        return sampleproject;
    }*/

    public void SaveInfoToMemory() {
        Saved_Info_Object tempinfo = this.getSaved_info();
        if(tempinfo==null){tempinfo = new Saved_Info_Object();}
        String FileNameTrades = "InfoR2.data";
        try{
            File f = new File(getApplicationContext().getFilesDir(),FileNameTrades);
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(tempinfo);
            out.close();
        }catch (Exception e){e.printStackTrace();}
    }
    public void GetInfoFromMemory() {
        Saved_Info_Object tempinfo2 = new Saved_Info_Object();
        String S1 = "InfoR2.data";
        try{
            FileInputStream fileIn2 = new FileInputStream(
                    getApplicationContext().getFilesDir()+ File.separator+S1);
            ObjectInput in2 = new ObjectInputStream(fileIn2);
            tempinfo2 = (Saved_Info_Object) in2.readObject();
            in2.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("TAGERROR",e.toString());}

        if(tempinfo2==null){
            tempinfo2 = new Saved_Info_Object();
            setSaved_info(tempinfo2);
        }else {
            setSaved_info(tempinfo2);
        }
        AddDefaultInfo();
    }

        /*public void SaveInfoToMemory2() {
        Saved_Info_Object_2 tempinfo = getSaved_info();
        if(tempinfo==null){tempinfo = new Saved_Info_Object();}
        String FileNameTrades = "Info.data";
        try{
            File f = new File(getApplicationContext().getFilesDir(),FileNameTrades);
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(tempinfo);
            out.close();
        }catch (Exception e){e.printStackTrace();}
    }
    public void GetInfoFromMemory2() {
        Saved_Info_Object tempinfo2 = new Saved_Info_Object();
        String S1 = "Info.data";
        try{
            FileInputStream fileIn2 = new FileInputStream(getFilesDir()+ File.separator+S1);
            ObjectInput in2 = new ObjectInputStream(fileIn2);
            tempinfo2 = (Saved_Info_Object) in2.readObject();
            in2.close();
        }catch (Exception e){e.printStackTrace();}

        if(tempinfo2==null){
            tempinfo2 = new Saved_Info_Object();
            setSaved_info(tempinfo2);
        }else {
            setSaved_info(tempinfo2);
        }
        AddDefaultInfo();
    }*/

    private void AddDefaultInfo() {

        //Lista 0 -- Default Options (Not used here - only for report object)
        //Lista 1 -- Project Names
        //Lista 2 -- Project Ids
        //Lista 3 -- Project Addresses
        //Lista 4 -- Contractor
        //Lista 5 -- Weather
        //Lista 6 -- Report Type
        //Lista 7 -- Report Area
        //Lista 8 -- Date (Not used here - only for report object)
        //Lista 9 -- Tasks
        //Lista 10 -- Start Time
        //Lista 11 -- End Time

        //Report Info
        //Option 0 -- Default Options
        //Option 1 -- Project Name
        //Option 2 -- Project Id
        //Option 3 -- Project Address
        //Option 4 -- List of Contractor
        //Option 5 -- Weather
        //Option 6 -- Report Type
        //Option 7 -- Report Discipline
        //Option 8 -- Report Date
        //Option 9 -- Activities
        //Option 10 -- Start Time
        //Option 11 -- End Time
        //Option 12 -- Location
        //Option 13 -- Contractor
        //Option 14 -- Subcontractor
        //Option 15 -- ContractNo
        //Option 16 -- Section
        //Option 17 -- Weather_Sky
        //Option 18 -- Weather_Wind
        //Option 19 -- Weather_Precipitation
        //Option 20 -- Temp_Low
        //Option 21 -- Temp_High
        //Option 22 -- StuctureNo
        //Option 23 -- Station
        //Option 24 -- Report ID

        Integer listsofoptions = this.getResources().getInteger(R.integer.listsofoptions);
        Saved_Info_Object savedinfo_2 = getSaved_info();
        ArrayList<ArrayList<String>> templist1 = savedinfo_2.getListasOpciones();
        if(templist1==null){templist1 = new ArrayList<>();}
        if(templist1.size()<listsofoptions){
            for(int j=templist1.size();j<listsofoptions;j++){
                ArrayList<String> templist2 = new ArrayList<>();
                switch (j){
                    case 0: // Default Options
                        for(int i=0;i<this.getResources().getInteger(R.integer.listsofoptions);i++){
                        templist2.add("N/A");
                        }
                        templist2.set(1,"Project Name");
                        templist2.set(2,"Project ID");
                        templist2.set(3,"Project Address");
                        templist2.set(4,"Contractor");
                        templist2.set(5,"Clear");
                        templist2.set(6,"Report Type");
                        templist2.set(7,"Report Discipline");
                        templist2.set(10,"06:00");
                        templist2.set(11,"16:00");
                    case 1: // Project Name
                        break;
                    case 2: // Project ID
                        break;
                    case 3: // Project List
                        break;
                    case 4: // Project Contractors
                        break;
                    case 5: // Weather
                        break;
                    case 6: // Type of Report
                        break;
                    case 7: // Discipline
                        templist2.add("Highways");
                        templist2.add("Environmental");
                        templist2.add("Structural");
                        templist2.add("Mechanical");
                        templist2.add("Electrical");
                        break;
                    case 8: // Date
                        break;
                    case 9: // Tasks
                        break;
                    case 10: // Start time
                        break;
                    case 11: // End time
                        break;
                    case 12: // Location
                        break;
                    case 13: // Contractor
                        break;
                    case 14: // Subcontractor
                        break;
                    case 15: // ContractNo.
                        break;
                    case 16: // Section
                        break;
                    case 17: // Weather Sky
                        templist2.add("Clear");
                        templist2.add("Overcast");
                        templist2.add("Rain");
                        break;
                    case 18: // Weather Wind
                        break;
                    case 19: // Weather Precipitation
                        templist2.add("No rain");
                        templist2.add("Light rain");
                        templist2.add("Heavy rain");
                        break;
                    case 20: // Temp Low
                        break;
                    case 21: // Temp High
                        break;
                    case 22: // Structure No.
                        break;
                    case 23: // Station
                        break;
                }
                templist1.add(templist2);
            }
            savedinfo_2.setListasOpciones(templist1);
            this.setSaved_info(savedinfo_2);
        }

        /*if(savedinfo_2.getWorkerGroups().size()==0){
            ArrayList<String> templist3 = new ArrayList<>();
            templist3.add("Carpenters");
            templist3.add("Ironworkers");
            templist3.add("Laborers");
            templist3.add("Finishers");
            templist3.add("Operators");
            savedinfo_2.setWorkerGroups(templist3);
            if(savedinfo_2.getListAvailableManpower().size()==0){
                ArrayList<ArrayList<Worker_Object>> availablemanpower = new ArrayList<>();
                for(int i=0;i<templist3.size();i++){
                    ArrayList<Worker_Object> workerlist = new ArrayList<>();
                    Worker_Object worker = new Worker_Object();
                    worker.setFirstName("Foreman");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    worker = new Worker_Object();
                    worker.setFirstName("Journeyman");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    worker = new Worker_Object();
                    worker.setFirstName("Apprentice");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    availablemanpower.add(workerlist);
                }
                savedinfo_2.setListAvailableManpower(availablemanpower);
            }

        }
        if(savedinfo_2.getEquipmentGroups().size()==0){
            ArrayList<String> templist3 = new ArrayList<>();
            templist3.add("Cranes");
            templist3.add("Forklifts");
            templist3.add("Boomlifts");
            savedinfo_2.setEquipmentGroups(templist3);
            if(savedinfo_2.getListAvailableEquipment().size()==0){
                ArrayList<ArrayList<Worker_Object>> availableequipment = new ArrayList<>();
                for(int i=0;i<templist3.size();i++){
                    ArrayList<Worker_Object> workerlist = new ArrayList<>();
                    Worker_Object worker = new Worker_Object();
                    worker.setFirstName(templist3.get(i).substring(0,templist3.get(i).length()-1)+" 1");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    worker = new Worker_Object();
                    worker.setFirstName(templist3.get(i).substring(0,templist3.get(i).length()-1)+" 2");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    worker = new Worker_Object();
                    worker.setFirstName(templist3.get(i).substring(0,templist3.get(i).length()-1)+" 3");
                    worker.setActivity("N/A");
                    worker.setCompany("N/A");
                    worker.setTrade("N/A");
                    worker.setIdNumber(UUID.randomUUID().toString());
                    worker.setRegHours("8");
                    workerlist.add(worker);
                    availableequipment.add(workerlist);
                }
                savedinfo_2.setListAvailableEquipment(availableequipment);
            }

            this.setSaved_info(savedinfo_2);
        }*/
    }
    public void GeneratePDFReport(Project_Object currentproject,int reporttype, int reportposition){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

        mcurrentproject = currentproject;
        mreportnumber = reportposition;
        mcurrentreport = mcurrentproject.getProjectReports().get(reporttype).get(reportposition);

        //Create the folder to save the files if it does not exist
        GenerateFolders(reporttype);

        mpref = this.getPreferences(MODE_PRIVATE);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);
        if(reporttypenumber==-1){
            Toast.makeText(this,"Error Generating PDF",Toast.LENGTH_SHORT).show();
            return;
        }

        //Create the file and the report afterwards;
        String date = mcurrentreport.getReportinfo().get(8);
        date = RearrangeDate(date);
        String s1 = this.getResources().getStringArray(R.array.reporttypes)[reporttype];
        String reportfilename = pdfThirdFolderFile+File.separator+date+"_"+s1+".pdf";
        File Report = new File(reportfilename);
        Integer noreport = 2;
        while(Report.exists()){
            reportfilename = pdfThirdFolderFile+File.separator+date+"_"+s1+"_"+String.valueOf(noreport)+".pdf";
            Report = new File(reportfilename);
            noreport = noreport+1;
        }
        try {
            doc = new Document(PageSize.LETTER,36,36,
                    108,120);
            OutputStream output = new FileOutputStream(Report);
            writer = PdfWriter.getInstance(doc, output);
            HeaderTable event1 = new HeaderTable();
            writer.setPageEvent(event1);
            DefineFonts();
            doc.open();

            switch (reporttype){
                case 0:
                    CreateQCReport();
                    break;
                case 1:
                    CreateQAReport();
                    break;
            }

            if(msharedpreferences.getBoolean(IncludePhotos,true)&&
                    mcurrentreport.getSelectedphotos().size()!=0){
                arrayofphotosforreport = mcurrentreport.getSelectedphotos();
                CreatePhotosPage();
            }
            doc.close();

            final String tempstringfilename = reportfilename;
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView().findViewById(R.id.relativelayout_newreport)
                            ,"Report Generated Successfully",Snackbar.LENGTH_LONG)
                    .setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(Intent.ACTION_VIEW);
                            intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY);
                            File f = new File(tempstringfilename);
                            Uri uri2 = FileProvider.getUriForFile(getApplicationContext(),
                                    "com.ilaquidain.constructionreportersfi.provider",f);
                            intent2.setDataAndType(uri2,getApplicationContext().getContentResolver().getType(uri2));
                            startActivity(intent2);
                        }
                    });
            snackbar.show();

            //Open the PDF after is created - not used
            /*Intent ViewPDFIntent = new Intent(Intent.ACTION_VIEW);
            ViewPDFIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_ACTIVITY_NO_HISTORY);
            File f = new File(reportfilename);
            Uri uri = FileProvider.getUriForFile(this,
                    "com.ilaquidain.constructionreportersfi.provider",f);
            ViewPDFIntent.setDataAndType(uri,this.getContentResolver().getType(uri));
            startActivity(ViewPDFIntent);*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void GenerateFolders(int reportype){
        String S_MainFolder,S_SecondFolder,S_ThirdFolder;
        S_MainFolder = this.getResources().getString(R.string.S_MainFolder);
        S_SecondFolder = mcurrentproject.getProjectName();
        S_ThirdFolder = this.getResources().getStringArray(R.array.reporttypes)[reportype];

        if(Build.VERSION.SDK_INT>=19){
            String MainFolder = S_MainFolder;
            pdfFolderFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),MainFolder);
            if(!pdfFolderFile.exists()){
                Boolean created = pdfFolderFile.mkdirs();
                Log.i("TAG","Main Folder Created"+pdfFolderFile.getAbsolutePath()+String.valueOf(created));
            }
            String SecondaryFolder = S_SecondFolder;
            pdfSecFolderFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    +File.separator+ MainFolder,SecondaryFolder);
            if(!pdfSecFolderFile.exists()){
                pdfSecFolderFile.mkdirs();
                Log.i("TAG","Secondary Folder Created"+pdfSecFolderFile.getAbsolutePath());
            }
            String ThirdFolder = S_ThirdFolder;
            pdfThirdFolderFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    +File.separator+ MainFolder+File.separator+SecondaryFolder,ThirdFolder);
            if(!pdfThirdFolderFile.exists()){
                pdfThirdFolderFile.mkdirs();
                Log.i("TAG","Third Folder Created"+pdfThirdFolderFile.getAbsolutePath());
            }
        }else{
            pdfFolderFile = new File(Environment.getExternalStorageDirectory()+File.separator+"Documents");
            if(!pdfFolderFile.exists()){
                pdfFolderFile.mkdir();
            }
            pdfSecFolderFile = new File(pdfFolderFile.getAbsolutePath()+"/"+
                    mcurrentproject.getProjectName());
            if(!pdfSecFolderFile.exists()){
                pdfSecFolderFile.mkdir();
            }
            pdfThirdFolderFile =new File(pdfSecFolderFile.getAbsolutePath()+"/"+
                    "QC Reports");
            if(!pdfThirdFolderFile.exists()){
                pdfThirdFolderFile.mkdir();
            }
        }
    }


    //Footer table - not used
    /*private class FooterTable extends PdfPageEventHelper {
        *//*private final PdfPTable footer;
        private FooterTable(PdfPTable footer) {
            this.footer = footer;
        }*//*
        Image total;
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30,16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        public void onEndPage(PdfWriter writer, Document document) {
            //footer.writeSelectedRows(0, -1, 36, 108, writer.getDirectContent());
            table = new PdfPTable(80);
            table.setTotalWidth(540);

            Bitmap signaturebitmap, companybitmap;

            Drawable drawable = getResources().getDrawable(R.drawable.squarewhiteshape);
            Bitmap blankbitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(blankbitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);

            try{
                String StoredPath = "signature.jpg";
                File f = new File(getApplicationContext().getFilesDir(),StoredPath);
                signaturebitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            }catch (IOException e){
                e.printStackTrace();
                signaturebitmap = blankbitmap;
            }


            try {
                //Fila 1 Columa 1 - Imagen de firma
                ByteArrayOutputStream btout = new ByteArrayOutputStream();
                signaturebitmap.compress(Bitmap.CompressFormat.JPEG, 100, btout);
                Image image = Image.getInstance(btout.toByteArray());
                per = new PdfPCell(image, true);
                per.setColspan(10);
                per.setUseAscender(true);
                per.setUseAscender(true);
                per.setBorderWidth(0);
                per.setHorizontalAlignment(Element.ALIGN_LEFT);
                per.setPadding(1);
                table.addCell(per);
                //Fila 1 Columna 2 - Espacio en blanco
                per = new PdfPCell(new Paragraph(""));
                per.setColspan(70);
                per.setUseAscender(true);
                per.setBorderWidth(0);
                table.addCell(per);
                //Fila 2 Columna 1 - Nombre de Creador
                tempstring = msharedpreferences.getString(OriginatorName, "");
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setBorderWidth(0);
                per.setColspan(40);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(per);
                //Fila 2 Columna 2 - Espacio en blanco
                tempstring = "";
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setBorderWidth(0);
                per.setColspan(40);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                table.addCell(per);
                //Fila 3 Columna 1 - Posicion Creador
                tempstring = msharedpreferences.getString(OriginatorPosition, "");
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setBorderWidth(0);
                per.setColspan(20);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(per);
                //Fila 3 Columna 2 - Numero de Pagina
                per = new PdfPCell(new Phrase(String.format(Locale.US,
                        "Page %d of",writer.getPageNumber()),Cal10));
                per.setBorderWidth(0);
                per.setColspan(23);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                table.addCell(per);
                //Fila 3 Columna 3 - Total de paginas
                per = new PdfPCell(total);
                per.setBorderWidth(0);
                per.setColspan(17);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(per);
                //Fila 3 Columna 4 - Originator Company
                tempstring = msharedpreferences.getString(OriginatorCompany, "");
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setBorderWidth(0);
                per.setColspan(20);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                table.addCell(per);

                PdfContentByte canvas2 = writer.getDirectContent();
                canvas2.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, 36, 108, canvas2);
                canvas2.endMarkedContentSequence();

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), Cal10),
                    2, 7, 0);
        }
    }*/

    private class HeaderTable extends PdfPageEventHelper {
        Image total;
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30,16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        public void onEndPage(PdfWriter writer, Document document) {
            //footer.writeSelectedRows(0, -1, 36, 108, writer.getDirectContent());
            table = new PdfPTable(80);
            table.setTotalWidth(540);

            Bitmap image1, image2;
            image1 = BitmapFactory.decodeResource(getResources(),R.drawable.gdb_logo);
            image2 = BitmapFactory.decodeResource(getResources(),R.drawable.sfi_logo_2);

            try {
                //Fila 1 Columa 1 - Imagen de logo 1
                ByteArrayOutputStream btout = new ByteArrayOutputStream();
                image1.compress(Bitmap.CompressFormat.PNG, 100, btout);
                Image image = Image.getInstance(btout.toByteArray());
                per = new PdfPCell(image, true);
                per.setColspan(20);
                per.setFixedHeight(36);
                per.setUseAscender(true);
                per.setHorizontalAlignment(Element.ALIGN_CENTER);
                per.setPadding(5);
                table.addCell(per);
                //Fila 1 Columna 2 - Imagen de logo 2
                ByteArrayOutputStream btout2 = new ByteArrayOutputStream();
                image2.compress(Bitmap.CompressFormat.PNG, 100, btout2);
                Image image5 = Image.getInstance(btout2.toByteArray());
                per = new PdfPCell(image5, true);
                per.setColspan(20);
                per.setFixedHeight(36);
                per.setUseAscender(true);
                per.setHorizontalAlignment(Element.ALIGN_CENTER);
                per.setPadding(5);
                table.addCell(per);
                //Fila 1 Columna 3 - Texto Gerald Desmond Bridge
                switch(reporttypenumber){
                    case 0:
                        tempstring = "Gerald Desmond Bridge Replacement Project\n\n\nDAILY QUALITY CONTROL REPORT";
                        break;
                    case 1:
                        tempstring = "Gerald Desmond Bridge Replacement Project\n\nQUALITY ASSURANCE\nINSPECTION REPORT";
                        break;
                    default:
                        tempstring = "Gerald Desmond Bridge Replacement Project";
                        break;
                }

                par = new Paragraph(tempstring,Cal11B);
                per = new PdfPCell(par);
                per.setUseAscender(true);
                per.setColspan(40);
                per.setRowspan(2);
                per.setHorizontalAlignment(Element.ALIGN_CENTER);
                per.setPaddingTop(5);
                table.addCell(per);
                //Fila 2 Columna 1 - Nombre de Creador
                switch (reporttypenumber){
                    case 0:
                        tempstring = "Doc. No: CQP003FA";
                        break;
                    case 1:
                        tempstring = "Doc. No: CQP002FB";
                        break;
                    default:
                        tempstring = "Doc. No: CQPXXXXX";
                        break;
                }
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setColspan(20);
                per.setPadding(5);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(per);
                //Fila 2 Columna 2 - Espacio en blanco
                switch (reporttypenumber){
                    case 0:
                        tempstring = "Rev. 00";
                        break;
                    case 1:
                        tempstring = "Rev. 00";
                        break;
                    default:
                        tempstring = "Rev. 00";
                        break;
                }
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setColspan(20);
                per.setPadding(5);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(per);

                //Fila 3 Columna 1 - Numero de Pagina
                per = new PdfPCell(new Phrase(String.format(Locale.US,
                        "Page %d of",writer.getPageNumber()), Cal10));
                per.setBorderWidth(0);
                per.setColspan(77);
                per.setPaddingTop(5);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                table.addCell(per);
                //Fila 3 Columna 3 - Total de paginas
                per = new PdfPCell(total);
                per.setBorderWidth(0);
                per.setColspan(3);
                per.setPaddingTop(5);
                per.setUseAscender(true);
                per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(per);

                PdfContentByte canvas2 = writer.getDirectContent();
                canvas2.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1,36,
                        756, canvas2);
                canvas2.endMarkedContentSequence();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), Cal10),
                    2, 9, 0);
        }
    }
    private void CreateQAReport() {
        try {
            //Table with contract info, date, contractor, activity id, location and type of work
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{7,3});
            //Row 1 Column 1 Contract No:
            tempstring = "Contract No.: " + mcurrentproject.getProjectContractNo();
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 1 Column 2 Date:
            tempstring = "Date: " + mcurrentreport.getReportinfo().get(8);
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 2 Column 1 Contractor Subcontractor Vendor
            tempstring = "Contractor/Subcontractor/Vendor: " + mcurrentreport.getReportinfo().get(13);
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 2 Column 2 Location / Satation
            tempstring = "Location/Station: " + mcurrentreport.getReportinfo().get(12);
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 3 Column 1 Type of work scheduled
            tempstring = "Type of Work Scheduled: " + mcurrentreport.getReportinfo().get(7);
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 3 Column 2 Activity ID
            tempstring = "Activity ID: " + mcurrentreport.getReportinfo().get(9);
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);

            //Table initial with items to discuss
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,9});
            tempstring = "ITEM";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            tempstring = "SPECIFICATION SECTION: THE FOLLOWING ITEMS AT A MINIMUM ARE TO BE DISCUSSED AT THIS MEETING.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "1.  Conformance of work to establish quality standards, including workmanship.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "2.  Use of defective, damaged or incorrect equipment or material.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "3.  Configuration of the work to the contract drawings and specifications.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "4.  Conformance of the control test results to the requirements of the contract drawings and specifications.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "5.  Adequacy of construction methods and equipment and tools utilized.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "6.  Adequacy of the environmental and safety precautions taken.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            tempstring = "";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);
            tempstring = "7.  Initial inspection requirements of the Inspection Procedure for the given feature of work.";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(3);
            table.addCell(per);

            doc.add(table);

            //Single Table with ITEMS DISSCUSSED
            table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1});
            tempstring = "ITEMS DISCUSSED";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);

            /*//Row 2 - IQA Daily Report
            table=new PdfPTable(1);
            table.setWidthPercentage(100);
            String s1 = mcurrentreport.getQcreportinfo().get(6);
            par = new Paragraph(s1,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            doc.add(table);
            //Row 3 - Date and Time
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,9,6,9});
            //Row 3 column 1
            tempstring = "Date";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 3 column 2
            tempstring = mcurrentreport.getQcreportinfo().get(8);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            //Row 3 column 3
            tempstring = "Time";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 3 column 4
            tempstring = mcurrentreport.getQcreportinfo().get(10)+" to "+mcurrentreport.getQcreportinfo().get(11);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            doc.add(table);
            //Row 4 - Weather and Report No.
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,9,6,9});
            //Row 4 column 1
            tempstring = "Weather";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 4 column 2
            tempstring = mcurrentreport.getQcreportinfo().get(5);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            //Row 4 column 3
            Boolean b4 = msharedpreferences.getBoolean(IncludeConstructionDate,true);
            tempstring = "Report No.";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 4 column 4
            if(b4){
                tempstring = getConstructionDate(mcurrentreport.getQcreportinfo().get(8));
                tempstring = "46 - "+tempstring;
            }else {
                tempstring = "";
            }
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            doc.add(table);
            //Row 5 - Type of Work and Location.
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,9,6,9});
            //Row 5 column 1
            tempstring = "Type of Work";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 5 column 2
            tempstring = mcurrentreport.getQcreportinfo().get(7);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            //Row 5 column 3
            tempstring = "Location";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 5 column 4
            tempstring = mcurrentreport.getQcreportinfo().get(12);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            doc.add(table);
            //Row 6 - Contractor & Activity ID.
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,9,6,9});
            //Row 6 column 1
            tempstring = "Contractor";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 6 column 2
            tempstring = mcurrentreport.getQcreportinfo().get(13);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);
            //Row 6 column 3
            tempstring = "Activity ID";
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            //Row 6 column 4
            tempstring = mcurrentreport.getQcreportinfo().get(14);
            par = new Paragraph(tempstring,Cal10);
            per = new PdfPCell(par);
            per.setPaddingLeft(5);
            per.setPaddingTop(2);
            per.setPaddingBottom(2);
            table.addCell(per);

            doc.add(table);*/

            //Create activity table
            ArrayList<Task_Object> reporttasks = mcurrentreport.getSelectedactivities();
            for(int j=0;j<reporttasks.size();j++){
                //doc.add(new Paragraph("\n"));
                table = new PdfPTable(1);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1});
                //table.setKeepTogether(true);
                table.setSplitLate(false);
                //table.setWidthPercentage(100);
                //per = getCell_15(3,"Activity:");
                //per.setBackgroundColor(lightgray);
                //table.addCell(per);
                tempstring = mcurrentreport.getSelectedactivities().get(j).getTaskName();
                par = new Paragraph(tempstring,Cal11BU);
                per = new PdfPCell(par);
                per.setBorderWidthBottom(0);
                per.setPadding(3);
                per.setPaddingLeft(10);
                per.setPaddingBottom(20);
                table.addCell(per);
                //table.addCell(getCell_15(12,tempstring));
                tempstring = mcurrentreport.getSelectedactivities().get(j).getTaskDescription();
                par = new Paragraph(tempstring, Cal10);
                per = new PdfPCell(par);
                per.setBorderWidthTop(0);
                per.setPadding(3);
                per.setPaddingLeft(10);
                per.setPaddingBottom(20);
                table.addCell(per);

                //table.addCell(getCell_15_2(15,tempstring));
                table.completeRow();
                doc.add(table);
            }

            //Create signature table
            table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1});
            tempstring = "SFI QA Representative:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);
            tempstring = "Signature:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);
            tempstring = "Date:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);

            doc.add(table);

            Bitmap signaturebitmap;

            Drawable drawable = getResources().getDrawable(R.drawable.squarewhiteshape);
            Bitmap blankbitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(blankbitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);

            try{
                String StoredPath = "signature.jpg";
                File f = new File(getApplicationContext().getFilesDir(),StoredPath);
                signaturebitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            }catch (IOException e){
                e.printStackTrace();
                signaturebitmap = blankbitmap;
            }

            table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3,1,1,1,3});

            tempstring = msharedpreferences.getString(OriginatorName, "");
            par = new Paragraph(tempstring, Cal11B);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setUseAscender(true);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);

            par = new Paragraph("");
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setUseAscender(true);
            table.addCell(per);

            ByteArrayOutputStream btout = new ByteArrayOutputStream();
            signaturebitmap.compress(Bitmap.CompressFormat.JPEG, 100, btout);
            Image image = Image.getInstance(btout.toByteArray());
            per = new PdfPCell(image, true);
            per.setUseAscender(true);
            per.setBorderWidthRight(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            per.setPaddingBottom(3);
            table.addCell(per);

            par = new Paragraph("");
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setUseAscender(true);
            table.addCell(per);

            tempstring = mcurrentreport.getReportinfo().get(8);
            par = new Paragraph(tempstring,Cal11B);
            per = new PdfPCell(par);
            per.setUseAscender(true);
            per.setBorderWidthTop(0);
            per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);

            doc.add(table);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private void CreateQCReport(){
        try{
            //First Row of QC Report
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1,1});
            //Row 1 Column 1
            tempstring = "Date: " + mcurrentreport.getReportinfo().get(8);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 1 Column 2
            tempstring = "Time In: " + mcurrentreport.getReportinfo().get(10);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 1 Column 3
            tempstring = "Time Out: " + mcurrentreport.getReportinfo().get(11);;
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 1 Column 4
            tempstring = "Page: ";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);
            //Second Row of QC Report
            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{10,10,7,13});
            //Row 2 Column 1
            tempstring = "Contractor: " + mcurrentreport.getReportinfo().get(13);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 2 Column 2
            tempstring = "Subcontractor/Vendor: " + mcurrentreport.getReportinfo().get(14);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 2 Column 3
            tempstring = "Contract No: " + mcurrentreport.getReportinfo().get(15);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 2 Column 4
            tempstring = "Section: "+ mcurrentreport.getReportinfo().get(16);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);
            //Third Row of QC Report
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,4});
            //Row 3 Column 1
            tempstring = "Weather";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setPadding(5);
            table.addCell(per);
            //Row 3 Column 2
            tempstring = "Temperature";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);
            //Fourth Row of QC Report
            table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1,1,1});
            //Row 4 Column 1
            tempstring = "Sky: " + mcurrentreport.getReportinfo().get(17);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setPadding(5);
            table.addCell(per);
            //Row 4 Column 2
            tempstring = "Wind: " + mcurrentreport.getReportinfo().get(18);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setPadding(5);
            table.addCell(per);
            //Row 4 Column 3
            tempstring = "Precipitation: " + mcurrentreport.getReportinfo().get(19);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setPadding(5);
            table.addCell(per);
            //Row 4 Column 4
            tempstring = "High: "+ mcurrentreport.getReportinfo().get(21);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setPadding(5);
            table.addCell(per);
            //Row 4 Column 5
            tempstring = "Low: "+ mcurrentreport.getReportinfo().get(20);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);
            //Fifth Row of QC Report
            table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1,1,1});
            //Row 5 Column 1
            tempstring = "Discipline: " + mcurrentreport.getReportinfo().get(17);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 5 Column 2
            tempstring = "Structure No.: " + mcurrentreport.getReportinfo().get(18);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            //Row 5 Column 3
            tempstring = "Station/Location: " + mcurrentreport.getReportinfo().get(19);
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            table.addCell(per);
            doc.add(table);
            //Sixth Row of QC Report
            table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,6,15,15,2,2});
            //Row 6 Column 1
            tempstring = "Drawing\nand\nSpecification";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setRowspan(5);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);
            //Row 6 Column 2
            tempstring = "Activity ID";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setRowspan(5);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);
            //Row 6 Column 3&4
            tempstring = "Description of Inspection / Test Method";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setColspan(2);
            per.setBorderWidthBottom(0);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(per);
            //Row 6 Column 5&6
            tempstring = "Complies";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setColspan(2);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(per);
            //Row 7 Column 3
            tempstring = "Type and Location of work being performed";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 7 Column 4
            tempstring = "Comments";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 7 Column 5
            tempstring = "Yes";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setRowspan(4);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);
            //Row 7 Column 6
            tempstring = "No";
            par = new Paragraph(tempstring, Cal8);
            per = new PdfPCell(par);
            per.setRowspan(4);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);
            //Row 8 Column 3
            tempstring = "Critical Activity Points(s) Noted";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 8 Column 4
            tempstring = "Visitors";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 9 Column 3
            tempstring = "Drawings and Submittals Used for Inspection";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 9 Column 4
            tempstring = "Meetings";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 10 Column 3
            tempstring = "Acceptance Status with NCRs Identified";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setPadding(2);
            table.addCell(per);
            //Row 10 Column 4
            tempstring = "";
            par = new Paragraph(tempstring, Cal6);
            per = new PdfPCell(par);
            per.setBorderWidthBottom(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setPadding(2);
            table.addCell(per);
            doc.add(table);

            //Create activity table
            table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{6,6,30,2,2});
            int max = Math.max(10,mcurrentreport.getSelectedactivities().size());
            for(int i=0; i<max;i++){
                if(i<mcurrentreport.getSelectedactivities().size()) {
                    tempstring = mcurrentreport.getSelectedactivities().get(i).
                            getAct_dwgspec();
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                    tempstring = mcurrentreport.getSelectedactivities().get(i).
                            getAct_No();
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                    tempstring = mcurrentreport.getSelectedactivities().get(i).
                            getTaskDescription();
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                    if (mcurrentreport.getSelectedactivities().get(i).getAct_Compliance() == 0) {
                        tempstring = "X";
                        par = new Paragraph(tempstring, Cal8);
                        per = new PdfPCell(par);
                        per.setHorizontalAlignment(Element.ALIGN_CENTER);
                        per.setPadding(5);
                        table.addCell(per);
                        tempstring = "";
                        par = new Paragraph(tempstring, Cal8);
                        per = new PdfPCell(par);
                        per.setHorizontalAlignment(Element.ALIGN_CENTER);
                        per.setPadding(5);
                        table.addCell(per);
                    } else {
                        tempstring = "";
                        par = new Paragraph(tempstring, Cal8);
                        per = new PdfPCell(par);
                        per.setHorizontalAlignment(Element.ALIGN_CENTER);
                        per.setPadding(5);
                        table.addCell(per);
                        tempstring = "X";
                        par = new Paragraph(tempstring, Cal8);
                        per = new PdfPCell(par);
                        per.setHorizontalAlignment(Element.ALIGN_CENTER);
                        per.setPadding(5);
                        table.addCell(per);
                    }
                }else{
                    tempstring = " ";
                    par = new Paragraph(tempstring,Cal8);
                    per = new PdfPCell(par);
                    table.addCell(per);
                    table.addCell(per);
                    table.addCell(per);
                    table.addCell(per);
                    table.addCell(per);
                }
            }
            doc.add(table);
            doc.add(new Paragraph("\n"));

            //Create equipmnt table
            table = new PdfPTable(3);
            table.setWidthPercentage(60);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidths(new float[]{8,2,2});
            tempstring = "Equipment";
            par = new Paragraph(tempstring,Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(per);
            tempstring = "#Persons";
            par = new Paragraph(tempstring,Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(per);
            tempstring = "Hours";
            par = new Paragraph(tempstring,Cal8);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(per);
            max = Math.max(6,mcurrentreport.getSelectedEquipPeopleHours().size());
            for(int i=0;i<max;i++){
                if(i<mcurrentreport.getSelectedEquipPeopleHours().size()){
                    tempstring= mcurrentreport.getSelectedEquipPeopleHours().get(i)
                    .getEquipment1();
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                    tempstring= String.valueOf(mcurrentreport.getSelectedEquipPeopleHours().get(i)
                            .getNumberOfPeople1());
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                    tempstring= String.valueOf(mcurrentreport.getSelectedEquipPeopleHours().get(i)
                            .getHours1());
                    par = new Paragraph(tempstring, Cal8);
                    per = new PdfPCell(par);
                    per.setPadding(5);
                    table.addCell(per);
                }else{
                    par = new Paragraph(" ");
                    per = new PdfPCell(par);
                    table.addCell(per);
                    table.addCell(per);
                    table.addCell(per);
                }
            }
            doc.add(table);
            doc.add(new Paragraph("\n"));


            //Create activity table
            /*ArrayList<Task_Object> reporttasks = mcurrentreport.getSelectedTasks();
            for(int j=0;j<reporttasks.size();j++){
                //doc.add(new Paragraph("\n"));
                table = new PdfPTable(1);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1});
                //table.setKeepTogether(true);
                table.setSplitLate(false);
                //table.setWidthPercentage(100);
                //per = getCell_15(3,"Activity:");
                //per.setBackgroundColor(lightgray);
                //table.addCell(per);
                tempstring = mcurrentreport.getSelectedTasks().get(j).getTaskName();
                par = new Paragraph(tempstring,Cal11BU);
                per = new PdfPCell(par);
                per.setBorderWidthBottom(0);
                per.setPadding(3);
                per.setPaddingLeft(10);
                per.setPaddingBottom(20);
                table.addCell(per);
                //table.addCell(getCell_15(12,tempstring));
                tempstring = mcurrentreport.getSelectedTasks().get(j).getTaskDescription();
                par = new Paragraph(tempstring,Cal10);
                per = new PdfPCell(par);
                per.setBorderWidthTop(0);
                per.setPadding(3);
                per.setPaddingLeft(10);
                per.setPaddingBottom(20);
                table.addCell(per);

                //table.addCell(getCell_15_2(15,tempstring));
                table.completeRow();
                doc.add(table);
            }*/

            //Create signature table
            table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1});
            tempstring = "SFI QC Representative:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);
            tempstring = "Signature:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);
            tempstring = "Date:";
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setPadding(5);
            per.setBorderWidthBottom(0);
            table.addCell(per);

            doc.add(table);

            Bitmap signaturebitmap;

            Drawable drawable = getResources().getDrawable(R.drawable.squarewhiteshape);
            Bitmap blankbitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(blankbitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);

            try{
                String StoredPath = "signature.jpg";
                File f = new File(getApplicationContext().getFilesDir(),StoredPath);
                signaturebitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            }catch (IOException e){
                e.printStackTrace();
                signaturebitmap = blankbitmap;
            }

            table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3,1,1,1,3});

            tempstring = msharedpreferences.getString(OriginatorName, "");
            par = new Paragraph(tempstring, Cal11B);
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setUseAscender(true);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);

            par = new Paragraph("");
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setBorderWidthRight(0);
            per.setUseAscender(true);
            table.addCell(per);

            ByteArrayOutputStream btout = new ByteArrayOutputStream();
            signaturebitmap.compress(Bitmap.CompressFormat.JPEG, 100, btout);
            Image image = Image.getInstance(btout.toByteArray());
            per = new PdfPCell(image, true);
            per.setUseAscender(true);
            per.setBorderWidthRight(0);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            per.setPaddingBottom(3);
            table.addCell(per);

            par = new Paragraph("");
            per = new PdfPCell(par);
            per.setBorderWidthTop(0);
            per.setBorderWidthLeft(0);
            per.setUseAscender(true);
            table.addCell(per);

            tempstring = mcurrentreport.getReportinfo().get(8);
            par = new Paragraph(tempstring,Cal11B);
            per = new PdfPCell(par);
            per.setUseAscender(true);
            per.setBorderWidthTop(0);
            per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(per);

            doc.add(table);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Get Construction Date solo esta en la general no en la de SFI
    /*private String getConstructionDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        ArrayList<Integer> holidays = new ArrayList<>();
        Date reportdate = new Date();
        Date referencedate = new Date();
        try{
            reportdate = sdf.parse(s);
            referencedate = sdf.parse("08/01/17");
        }catch (ParseException e){
            e.printStackTrace();
        }
        long dif1 = reportdate.getTime()-referencedate.getTime();
        long dif2 = TimeUnit.MILLISECONDS.toDays(dif1);
        int originatconsdate = 88;
        Calendar c = Calendar.getInstance();
        c.setTime(referencedate);

        for(int i=0;i<dif2;i++){
            c.add(Calendar.DATE,1);
            Date tempdate = c.getTime();
            boolean isholiday = true;
            ArrayList holidaydates = new ArrayList<String>
                    (Arrays.asList(getResources().getStringArray(R.array.holiday)));
            //Comprobamos si es sabado o domingo
            if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY ||
                    c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                isholiday = false;
            }
            //Comprobamos si coincide con alguna fiesta
            for (int k=0;k<holidaydates.size();k++){
                if(sdf.format(tempdate).equals(holidaydates.get(k))){
                    isholiday = false;
                }
            }
            //Si es verdadero añadimos uno al numero
            if(isholiday){
                originatconsdate = originatconsdate +1;
                holidays.add(0);
            }else{
                holidays.add(1);
            }
        }
        String returnvalue;
        c.setTime(reportdate);

        //comprobamos si los dias anteriores son fiesta
        if(holidays.get(holidays.size()-1)==1 && holidays.get(holidays.size()-2)==1&&
                holidays.get(holidays.size()-3)==1&&holidays.get(holidays.size()-4)==1){
            returnvalue = String.valueOf(originatconsdate)+"D";
        }else if(holidays.get(holidays.size()-1)==1 && holidays.get(holidays.size()-2)==1&&
                holidays.get(holidays.size()-3)==1){
            returnvalue = String.valueOf(originatconsdate)+"C";
        }else if(holidays.get(holidays.size()-1)==1 && holidays.get(holidays.size()-2)==1){
            returnvalue = String.valueOf(originatconsdate)+"B";
        }else if(holidays.get(holidays.size()-1)==1){
            returnvalue = String.valueOf(originatconsdate)+"A";
        }else{
            returnvalue = String.valueOf(originatconsdate);
        }
        return returnvalue;
    }*/

    private void CreateManPowerPage(){
        doc.newPage();

        ArrayList<Worker_Object> workers = mcurrentreport.getSelectedlabor();
        ArrayList<Worker_Object> equipmet = mcurrentreport.getSelectedequip();
        if(workers.size()+equipmet.size()<40) {
            try {
                par = new Paragraph("\n");
                doc.add(par);

                table = new PdfPTable(14);
                table.setWidthPercentage(100);
                per = getCell_15(14, "Manpower");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                per = getCell_15(5, "Name");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Contractor");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Activity");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(1, "Hours");
                per.setBackgroundColor(lightgray);
                table.addCell(per);


                for (int i = 0; i < workers.size(); i++) {
                    per = getCell_15(5, workers.get(i).getFirstName());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getCompany());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getActivity());
                    table.addCell(per);
                    per = getCell_15(1, String.valueOf(workers.get(i).getRegHours()));
                    table.addCell(per);
                }
                doc.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }else if (workers.size()<=40){
            try {
                par = new Paragraph("\n");
                doc.add(par);

                table = new PdfPTable(14);
                table.setWidthPercentage(100);
                per = getCell_15(14, "Manpower");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                per = getCell_15(5, "Name");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Contractor");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Activity");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(1, "Hours");
                per.setBackgroundColor(lightgray);
                table.addCell(per);


                for (int i = 0; i < workers.size(); i++) {
                    per = getCell_15(5, workers.get(i).getFirstName());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getCompany());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getActivity());
                    table.addCell(per);
                    per = getCell_15(1, String.valueOf(workers.get(i).getRegHours()));
                    table.addCell(per);
                }
                doc.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            doc.newPage();
        }else if(workers.size()>40){
            try {
                par = new Paragraph("\n");
                doc.add(par);

                table = new PdfPTable(14);
                table.setWidthPercentage(100);
                per = getCell_15(14, "Manpower");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                per = getCell_15(5, "Name");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Contractor");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Activity");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(1, "Hours");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                for (int i = 0; i < 40; i++) {
                    per = getCell_15(5, workers.get(i).getFirstName());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getCompany());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getActivity());
                    table.addCell(per);
                    per = getCell_15(1, String.valueOf(workers.get(i).getRegHours()));
                    table.addCell(per);
                }
                doc.add(table);

                doc.newPage();
                par = new Paragraph("\n");
                doc.add(par);

                table = new PdfPTable(14);
                table.setWidthPercentage(100);
                per = getCell_15(14, "Manpower");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                per = getCell_15(5, "Name");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Contractor");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(4, "Activity");
                per.setBackgroundColor(lightgray);
                table.addCell(per);
                per = getCell_15(1, "Hours");
                per.setBackgroundColor(lightgray);
                table.addCell(per);

                for (int i = 40; i < workers.size(); i++) {
                    per = getCell_15(5, workers.get(i).getFirstName());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getCompany());
                    table.addCell(per);
                    per = getCell_15(4, workers.get(i).getActivity());
                    table.addCell(per);
                    per = getCell_15(1, String.valueOf(workers.get(i).getRegHours()));
                    table.addCell(per);
                }
                doc.add(table);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

    }
    private void CreateEquipmentPage() {
        try{
            par = new Paragraph("\n");
            doc.add(par);

            table = new PdfPTable(14);
            table.setWidthPercentage(100);

            per = getCell_15(14,"Equipment");
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            per = getCell_15(5,"Name");
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            per = getCell_15(4,"Contractor");
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            per = getCell_15(4,"Activity");
            per.setBackgroundColor(lightgray);
            table.addCell(per);
            per = getCell_15(1,"Hours");
            per.setBackgroundColor(lightgray);
            table.addCell(per);

            ArrayList<Worker_Object> equipment = mcurrentreport.getSelectedequip();

            for(int i=0;i<equipment.size();i++){
                per = getCell_15(5,equipment.get(i).getFirstName());
                table.addCell(per);
                per = getCell_15(4,equipment.get(i).getCompany());
                table.addCell(per);
                per = getCell_15(4,equipment.get(i).getActivity());
                table.addCell(per);
                per = getCell_15(1,String.valueOf(equipment.get(i).getRegHours()));
                table.addCell(per);
            }
            table.completeRow();
            table.setKeepTogether(true);
            doc.add(table);

        }catch (DocumentException e){
            e.printStackTrace();
        }
    }
    private void CreatePhotosPage() {
        ConvertPhotosToImages();
        int numberofphotopages;
        try {
            if(imagenes.size()!=0){
                double tempdob = Math.ceil((double)imagenes.size()/4);
                numberofphotopages = (int) tempdob;
            }else {return;}

            int counter =0;
            for(int u=0;u<numberofphotopages;u++){
                doc.newPage();
                table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1,1});
                addrowofphotos(counter);
                if(msharedpreferences.getBoolean(IncludePhotoDate,true)) {
                    addrowofdates(counter);
                    //addrowofactivities(counter);
                    //addrowofdesciptions(counter);
                }
                counter = counter+2;
                addrowofphotos(counter);
                if(msharedpreferences.getBoolean(IncludePhotoDate,true)) {
                    addrowofdates(counter);
                    //addrowofactivities(counter);
                    //addrowofdesciptions(counter);
                }
                counter = counter +2;
                doc.add(table);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void addrowofphotos(int counter) {
        if(counter<imagenes.size()){
            per = new PdfPCell(imagenes.get(counter),true);
            per.setFixedHeight(250);
            per.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            per.setPadding(5);
            table.addCell(per);
        }else {
            addwhitesquare();
        }
        if(counter+1<imagenes.size()){
            per = new PdfPCell(imagenes.get(counter+1),true);
            per.setFixedHeight(250);
            per.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            per.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            per.setPadding(5);
            table.addCell(per);
        }else {
            addwhitesquare();
        }
    }
    private void addrowofactivities(int counter) {
        if(counter<imagenes.size()){
            tempstring = mcurrentreport.getSelectedphotos().get(counter).getPhotoActivity();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring, Cal10);
        per = new PdfPCell(par);
        per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(2);
        per.setBorderWidthTop(0);
        per.setBorderWidthBottom(0);
        table.addCell(per);

        if(counter+1<imagenes.size()){
            tempstring = mcurrentreport.getSelectedphotos().get(counter+1).getPhotoActivity();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring, Cal10);
        per = new PdfPCell(par);
        per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(2);
        per.setBorderWidthTop(0);
        per.setBorderWidthBottom(0);
        table.addCell(per);
    }
    private void addrowofdates(int counter) {
        if(counter<imagenes.size()){
            tempstring = arrayofphotosforreport.get(counter).getPhotoDate();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring);
        per = new PdfPCell(par);
        //per.setBorderWidth(0);
        //per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(2);
        //per.setBorderWidthBottom(0);
        table.addCell(per);

        if(counter+1<imagenes.size()){
            tempstring = arrayofphotosforreport.get(counter+1).getPhotoDate();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring);
        per = new PdfPCell(par);
        //per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(2);
        //per.setBorderWidthBottom(0);
        table.addCell(per);
    }
    private void addrowofdesciptions(int counter) {
        if(counter<imagenes.size()){
            tempstring = mcurrentreport.getSelectedphotos().get(counter).getPhotoDescription();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring, Cal10);
        per = new PdfPCell(par);
        per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(5);
        per.setBorderWidthTop(0);
        table.addCell(per);

        if(counter+1<imagenes.size()){
            tempstring = mcurrentreport.getSelectedphotos().get(counter+1).getPhotoDescription();
            if(tempstring==null){tempstring=" ";}
        }else {
            tempstring=" ";
        }
        par = new Paragraph(tempstring, Cal10);
        per = new PdfPCell(par);
        per.setBackgroundColor(lightgray);
        per.setPaddingLeft(5);
        per.setPaddingTop(2);
        per.setPaddingBottom(2);
        per.setBorderWidthTop(0);
        table.addCell(per);
    }
    private void addwhitesquare() {
        try{
            PdfTemplate template = writer.getDirectContent().createTemplate(10, 10);
            template.rectangle(0, 0, 10, 10);
            template.setColorFill(BaseColor.WHITE);
            template.fill();
            writer.releaseTemplate(template);
            Image image = Image.getInstance(template);
            per = new PdfPCell(image,true);
            per.setFixedHeight(200);
            per.setHorizontalAlignment(Element.ALIGN_CENTER);
            per.setVerticalAlignment(Element.ALIGN_CENTER);
            per.setPadding(5);
            table.addCell(per);
        }
        catch (IOException | DocumentException e){
            e.printStackTrace();}
    }
    private void ConvertPhotosToImages() {
        bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        String photosquality = msharedpreferences.getString(PhotosQuality,"Medium");
        imagenes = new ArrayList<>();
        int maxpixels;
        Bitmap tempbitmap;

        switch (photosquality){
            case "High":
                maxpixels = 800;
                break;
            case "Medium":
                maxpixels = 400;
                break;
            case "Low":
                maxpixels = 200;
                break;
            default:
                maxpixels = 400;
                break;
        }

        try{
            for(int i=0;i<arrayofphotosforreport.size();i++){
                String photopath = arrayofphotosforreport.get(i).getPathDevice();
                String photodate = arrayofphotosforreport.get(i).getPhotoDate();

                bitmaphoto = BitmapFactory.decodeFile(photopath,bmOptions);

                int height1 = bitmaphoto.getHeight();
                int width1 = bitmaphoto.getWidth();

                if(height1>width1){
                    Double scale = ((double)height1) / maxpixels;
                    double tempdouble = ((double)height1)/scale;
                    int newheight = (int) tempdouble;
                    tempdouble = ((double)width1)/scale;
                    int newwidth = (int) tempdouble;

                    //int oldsize = bitmaphoto.getByteCount();
                    tempbitmap = Bitmap.createScaledBitmap(bitmaphoto,newwidth,newheight,true);
                    /*if(msharedpreferences.getBoolean(IncludePhotoDate,true)) {
                        Canvas cs = new Canvas(tempbitmap);
                        Paint tPaint = new Paint();
                        switch (photosquality) {
                            case "High":
                                tPaint.setTextSize(20);
                                break;
                            case "Medium":
                                tPaint.setTextSize(10);
                                break;
                            case "Low":
                                tPaint.setTextSize(5);
                                break;
                            default:
                                break;
                        }
                        tPaint.setColor(ContextCompat.getColor(this, R.color.textorange));
                        tPaint.setStyle(Paint.Style.FILL);
                        cs.drawBitmap(tempbitmap, 0f, 0f, null);
                        float height = tPaint.measureText("yY");
                        cs.drawText(photodate, 5f, height + 5f, tPaint);
                    }*/
                    bitmaphoto.recycle();
                    bitmaphoto = tempbitmap;

                    //int newsize = bitmaphoto.getByteCount();
                    /*Map<String,Object> photo = new HashMap<>();
                    photo.put("Scale",String.valueOf(scale));
                    photo.put("OldHeight",String.valueOf(height1));
                    photo.put("OldWidth",String.valueOf(width1));
                    photo.put("NewHeight",String.valueOf(newheight));
                    photo.put("NewWidth",String.valueOf(newwidth));
                    photo.put("OldSize",String.valueOf(oldsize));
                    photo.put("NewSize",String.valueOf(newsize));
                    db.collection("Photos")
                            .add(photo)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });*/

                }else if(width1>height1){
                    Double scale = ((double)width1) / maxpixels;
                    double tempdouble = ((double)height1)/scale;
                    int newheight = (int) tempdouble;
                    tempdouble = ((double)width1)/scale;
                    int newwidth = (int) tempdouble;

                    //int oldsize = bitmaphoto.getByteCount();
                    tempbitmap = Bitmap.createScaledBitmap(bitmaphoto,newwidth,newheight,true);
                    /*if(msharedpreferences.getBoolean(IncludePhotoDate,true)) {
                        Canvas cs = new Canvas(tempbitmap);
                        Paint tPaint = new Paint();
                        switch (photosquality) {
                            case "High":tPaint.setTextSize(20);break;
                            case "Medium":tPaint.setTextSize(10);break;
                            case "Low":tPaint.setTextSize(5);break;
                            default:break;
                        }
                        tPaint.setColor(Color.BLACK);
                        tPaint.setStyle(Paint.Style.FILL);
                        cs.drawRect(0f,cs.getHeight(),cs.getWidth(),cs.getHeight()+30,tPaint);
                        cs.drawBitmap(tempbitmap, 0f, 0f, null);
                        float height = tPaint.measureText("yY");
                        tPaint.setColor(ContextCompat.getColor(this,R.color.textorange));
                        cs.drawText(photodate, 5f, cs.getHeight()+height + 5f, tPaint);
                    }*/
                    bitmaphoto.recycle();
                    bitmaphoto = tempbitmap;
                    //int newsize = bitmaphoto.getByteCount();
                    /*Map<String,Object> photo = new HashMap<>();
                    photo.put("Scale",String.valueOf(scale));
                    photo.put("OldHeight",String.valueOf(height1));
                    photo.put("OldWidth",String.valueOf(width1));
                    photo.put("NewHeight",String.valueOf(newheight));
                    photo.put("NewWidth",String.valueOf(newwidth));
                    photo.put("OldSize",String.valueOf(oldsize));
                    photo.put("NewSize",String.valueOf(newsize));
                    db.collection("Photos")
                            .add(photo)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });*/
                }

                ByteArrayOutputStream btout = new ByteArrayOutputStream();
                bitmaphoto.compress(Bitmap.CompressFormat.JPEG, 80, btout);
                Image image = Image.getInstance(btout.toByteArray());
                imagenes.add(image);
            }
        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        bmOptions.inJustDecodeBounds = true;
    }
    private PdfPCell getCell_15(int units, String par) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(units);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(par, Cal10);
        cell.setPaddingLeft(5);
        cell.setPaddingTop(2);
        cell.setPaddingBottom(2);
        cell.addElement(p);
        return cell;
    }
    private PdfPCell getCell_15_2(int units, String par) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(units);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(12,par, Cal10);
        cell.setPaddingTop(8);
        cell.setPaddingBottom(8);
        cell.setPaddingLeft(5);
        cell.addElement(p);
        return cell;
    }
    private PdfPTable footertable(){
        table = new PdfPTable(8);
        table.setTotalWidth(540);

        Bitmap signaturebitmap, companybitmap;

        Drawable drawable = getResources().getDrawable(R.drawable.squarewhiteshape);
        Bitmap blankbitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankbitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);

        try{
            String StoredPath = "signature.jpg";
            File f = new File(this.getApplicationContext().getFilesDir(),StoredPath);
            signaturebitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        }catch (IOException e){
            e.printStackTrace();
            signaturebitmap = blankbitmap;
        }
        try{
            String StoredPath2 = "logo.jpg";
            File f2 = new File(this.getApplicationContext().getFilesDir(),StoredPath2);
            companybitmap = BitmapFactory.decodeStream(new FileInputStream(f2));
        }catch (IOException e){
            e.printStackTrace();
            companybitmap = blankbitmap;
        }




        try {
            //Fila 1 Columa 1 - Imagen de firma
            ByteArrayOutputStream btout = new ByteArrayOutputStream();
            signaturebitmap.compress(Bitmap.CompressFormat.JPEG, 100, btout);
            Image image = Image.getInstance(btout.toByteArray());
            per = new PdfPCell(image, true);
            per.setColspan(1);
            per.setUseAscender(true);
            per.setUseAscender(true);
            per.setBorderWidth(0);
            per.setHorizontalAlignment(Element.ALIGN_LEFT);
            per.setPadding(5);
            table.addCell(per);
            //Fila 1 Columna 2 - Espacio en blanco
            per = new PdfPCell(new Paragraph(""));
            per.setColspan(6);
            per.setUseAscender(true);
            per.setBorderWidth(0);
            table.addCell(per);
            //Fila 1 Columna 3 - Image de Empresa
            ByteArrayOutputStream btout2 = new ByteArrayOutputStream();
            companybitmap.compress(Bitmap.CompressFormat.JPEG, 100, btout2);
            Image image2 = Image.getInstance(btout2.toByteArray());
            per = new PdfPCell(image2, true);
            per.setColspan(1);
            per.setUseAscender(true);
            per.setUseAscender(true);
            per.setBorderWidth(0);
            per.setHorizontalAlignment(Element.ALIGN_RIGHT);
            per.setPadding(5);
            table.addCell(per);
            //Fila 2 Columna 1 - Nombre de Creador
            tempstring = msharedpreferences.getString(OriginatorName, "");
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setBorderWidth(0);
            per.setColspan(4);
            per.setUseAscender(true);
            per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(per);
            //Fila 2 Columna 2 - Espacio en blanco
            tempstring = msharedpreferences.getString(OriginatorCompany, "");
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setBorderWidth(0);
            per.setColspan(4);
            per.setUseAscender(true);
            per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(per);
            //Fila 3 Columna 1 - Posicion Creador
            tempstring = msharedpreferences.getString(OriginatorPosition, "");
            par = new Paragraph(tempstring, Cal10);
            per = new PdfPCell(par);
            per.setBorderWidth(0);
            per.setColspan(4);
            per.setUseAscender(true);
            per.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(per);
            //Fila 3 Columna 2 - Fecha
            per = new PdfPCell(new Phrase(String.format(Locale.US,
                    "Page %d of %d",writer.getPageNumber()), Cal10));
            per.setBorderWidth(0);
            per.setColspan(4);
            per.setUseAscender(true);
            per.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(per);


        }catch (Exception e){
            e.printStackTrace();
        }

        return table;
    }
    private String RearrangeDate(String date) {
        date = date.replace("/","");
        date = date.substring(4,6)+date.substring(0,4);
        //String timecreate = new SimpleDateFormat("hhmmss",Locale.US).format(new Date());
        //date = date + "_"+timecreate;
        return date;
    }
    private void DefineFonts() {
        Cal11BU = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD|Font.UNDERLINE);
        Cal11B = new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD);
        Cal6 = new Font(Font.FontFamily.TIMES_ROMAN,6);
        Cal7 = new Font(Font.FontFamily.TIMES_ROMAN,7);
        Cal8 = new Font(Font.FontFamily.TIMES_ROMAN,8);
        Cal8B = new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
        Cal9 = new Font(Font.FontFamily.TIMES_ROMAN,9);
        Cal10 = new Font(Font.FontFamily.TIMES_ROMAN,10);
        Cal10orange = new Font(Font.FontFamily.TIMES_ROMAN,10);
        Cal10orange.setColor(BaseColor.ORANGE);
        Cal10B_Gray = new Font(Font.FontFamily.TIMES_ROMAN,10);
        Cal10B_Gray.setColor(BaseColor.GRAY);
        //Cal9B_White = new Font(Font.FontFamily.TIMES_ROMAN,10,Font.NORMAL, BaseColor.WHITE);
    }

}
