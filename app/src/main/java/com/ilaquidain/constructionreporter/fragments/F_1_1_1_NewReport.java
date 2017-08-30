package com.ilaquidain.constructionreporter.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ilaquidain.constructionreporter.R;
import com.ilaquidain.constructionreporter.activities.MainActivity;
import com.ilaquidain.constructionreporter.fragments_newreport.ManEquipFragment;
import com.ilaquidain.constructionreporter.fragments_newreport.Photos_Fragment;
import com.ilaquidain.constructionreporter.fragments_newreport.ReportInfoFragment;
import com.ilaquidain.constructionreporter.fragments_newreport.ReportTasksFragment;
import com.ilaquidain.constructionreporter.object.Project_Object;
import com.ilaquidain.constructionreporter.object.Report_Object;
import com.ilaquidain.constructionreporter.object.Saved_Info_Object;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class F_1_1_1_NewReport extends Fragment implements View.OnClickListener{

    public F_1_1_1_NewReport() {
        super();
    }

    private Project_Object currentproject;
    private Integer projectnumber = null;
    private Integer reportnumber = null;
    private Report_Object currentreport = null;
    private Saved_Info_Object savedinfo = null;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private View v;
    private InterstitialAd mInterstitialAd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_newreport,container,false);
        v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-9978137837326968/9780327651");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("BED5CEE69ABEED9A21B0D1A4819302F8")
                .build());

        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        if(savedinfo==null || projectnumber==-1){
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }else {
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
        }
        reportnumber = mpref.getInt("reportnumber",-1);
        if(reportnumber == -1){
            int newreport = mpref.getInt("newreport",1);
            if(newreport == 1){
                currentreport = new Report_Object();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Report");
            }else {
                currentreport = savedinfo.getTempreport();
                String s1 = currentreport.getReportInfo().get(8)+" Report";
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(s1);
            }
            savedinfo.setTempreport(currentreport);
            mprefedit = mpref.edit();
            mprefedit.putInt("newreport",0);
            mprefedit.apply();
            ((MainActivity)getActivity()).setSaved_info(savedinfo);
        }else {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports().get(reportnumber);
            String s1 = currentreport.getReportInfo().get(8)+" Report";
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(s1);
        }

        TextView title = (TextView)v.findViewById(R.id.newreport_title);
        if(reportnumber==-1){title.setText(getResources().getText(R.string.NewReport));}
        else {title.setText(getResources().getText(R.string.EditReport));}

        RelativeLayout lay1 = (RelativeLayout)v.findViewById(R.id.lay1);
        lay1.setOnClickListener(this);
        RelativeLayout lay5 = (RelativeLayout)v.findViewById(R.id.lay5);
        lay5.setOnClickListener(this);
        RelativeLayout lay6 = (RelativeLayout)v.findViewById(R.id.lay6);
        lay6.setOnClickListener(this);
        RelativeLayout lay7 = (RelativeLayout)v.findViewById(R.id.lay7);
        lay7.setOnClickListener(this);
        RelativeLayout lay8 = (RelativeLayout)v.findViewById(R.id.lay8);
        lay8.setOnClickListener(this);
        FloatingActionButton fabaccept = (FloatingActionButton)v.findViewById(R.id.fabsave);
        FloatingActionButton fabpdf = (FloatingActionButton)v.findViewById(R.id.fabpdf);
        fabaccept.setOnClickListener(this);
        fabpdf.setOnClickListener(this);

        setDefaultValues();

        return v;
    }

    private void setDefaultValues() {
        if(currentreport.getReportInfo().get(1).equals("")){
            currentreport.getReportInfo().set(1,savedinfo.getListasOpciones().get(0).get(1));}
        if(currentreport.getReportInfo().get(2).equals("")){
            currentreport.getReportInfo().set(2,savedinfo.getListasOpciones().get(0).get(2));}
        if(currentreport.getReportInfo().get(3).equals("")){
            currentreport.getReportInfo().set(3,savedinfo.getListasOpciones().get(0).get(3));}
        if(currentreport.getReportInfo().get(8).equals("")){
            currentreport.getReportInfo().set(8,new SimpleDateFormat("MM/dd/yy",Locale.US).format(new Date()));}
        if(currentreport.getReportInfo().get(6).equals("")){
            currentreport.getReportInfo().set(6,savedinfo.getListasOpciones().get(0).get(6));}
        if (currentreport.getReportInfo().get(7).equals("")){
            currentreport.getReportInfo().set(7,savedinfo.getListasOpciones().get(0).get(7));}
        if(currentreport.getReportInfo().get(5).equals("")){
            currentreport.getReportInfo().set(5,savedinfo.getListasOpciones().get(0).get(5));}
        if(currentreport.getReportInfo().get(10).equals("")){
            currentreport.getReportInfo().set(10,savedinfo.getListasOpciones().get(0).get(10));}
        if(currentreport.getReportInfo().get(11).equals("")){
            currentreport.getReportInfo().set(11,savedinfo.getListasOpciones().get(0).get(11));}
    }

    @Override
    public void onClick(View v) {
        Fragment currentfragment = getActivity().getFragmentManager().findFragmentById(R.id.MainFrame);
        FragmentManager fm = getFragmentManager();
        switch (v.getId()){
            case R.id.lay1:
                ReportInfoFragment reportinfo = new ReportInfoFragment();
                //reportinfo.setTargetFragment(currentfragment,1002);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,reportinfo,getResources().getString(R.string.fragment_reportinfofragment))
                        .addToBackStack(getResources().getString(R.string.fragment_reportinfofragment))
                        .commit();
                break;
            case R.id.lay5:
                ReportTasksFragment fgmt7 = new ReportTasksFragment();
                //fgmt7.setTargetFragment(currentfragment,1003);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt7,getResources().getString(R.string.fragment_reporttasksfragment))
                        .addToBackStack(getResources().getString(R.string.fragment_reporttasksfragment))
                        .commit();
                break;
            case R.id.lay6:
                mpref.edit().putString("itemstype","manpower").apply();
                ManEquipFragment fragment1 = new ManEquipFragment();
                //fragment1.setTargetFragment(currentfragment,1005);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fragment1,getResources().getString(R.string.fragment_equipmentfragment))
                        .addToBackStack(getResources().getString(R.string.fragment_equipmentfragment))
                        .commit();
                /*ManpowerFragment dialogmanpower = new ManpowerFragment();
                dialogmanpower.setTargetFragment(currentfragment,1004);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,dialogmanpower,getResources().getString(R.string.fragment_manpowerfragment))
                        .addToBackStack(getResources().getString(R.string.fragment_manpowerfragment))
                        .commit();*/
                break;
            case R.id.lay7:
                mpref.edit().putString("itemstype","equipment").apply();
                ManEquipFragment fragment2 = new ManEquipFragment();
                //fragment2.setTargetFragment(currentfragment,1005);
                fm.beginTransaction()
                    .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                    .replace(R.id.MainFrame,fragment2,getResources().getString(R.string.fragment_equipmentfragment))
                    .addToBackStack(getResources().getString(R.string.fragment_equipmentfragment))
                    .commit();
                /*EquipmentFragment dialogequipment = new EquipmentFragment();
                dialogequipment.setTargetFragment(currentfragment,1005);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,dialogequipment,getResources().getString(R.string.fragment_equipmentfragment))
                        .addToBackStack(getResources().getString(R.string.fragment_equipmentfragment))
                        .commit()*/;
                break;
            case R.id.lay8:
                Photos_Fragment dialogphotos = new Photos_Fragment();
                //dialogphotos.setTargetFragment(currentfragment,1006);
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,dialogphotos,getResources().getString(R.string.fragment_photosfragment))
                        .addToBackStack(getResources().getString(R.string.fragment_photosfragment))
                        .commit();
                break;
            case R.id.fabsave:
                ExitFragment(0);
                break;
            case R.id.fabpdf:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        ExitFragment(1);
                    }
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        ExitFragment(1);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        ExitFragment(1);
                    }
                });
                break;
        }
    }

    public void ExitFragment(int createpdf) {
        //Si el informe ya existia y se habia guardado
        if(createpdf==1 && reportnumber != -1){
            new GeneratePDF().execute();
            //((MainActivity)getActivity()).GeneratePDFReport(currentproject,reportnumber);
            /*Toast toast = Toast.makeText(getActivity(),"PDF Report Created",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();*/
            //Si el informe no exisitia - primero se guarda y luego se genera el PDF
        }else if(createpdf==1){
            currentproject.getProjectReports().add(currentreport);
            savedinfo.getSavedProjects().set(projectnumber,currentproject);
            ((MainActivity)getActivity()).setSaved_info(savedinfo);
            new GeneratePDF().execute();
            /*Toast toast = Toast.makeText(getActivity(),"PDF Report Created",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();*/
        } else {
            //Solo se guarda un informe nuevo per no se genera el` PDF
            if(reportnumber == -1){
                currentproject.getProjectReports().add(savedinfo.getTempreport());
            }else {
                //Se guardan los cambios en un informe que ya existia pero no se modifica
                currentproject.getProjectReports().set(reportnumber,currentreport);
            }
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView().findViewById(R.id.relativelayout_newreport),
                    "Report Saved Succesfully",Snackbar.LENGTH_SHORT).show();
            /*Toast toast = Toast.makeText(getActivity(),"Report Saved Succesfully",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();*/
        }
        savedinfo.getSavedProjects().set(projectnumber,currentproject);
        ((MainActivity)getActivity()).setSaved_info(savedinfo);

        /*F_1_1_ProjectMenu fgmt = new F_1_1_ProjectMenu();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                .replace(R.id.MainFrame,fgmt,getResources().getString(R.string.fragment_projectmenu))
                .addToBackStack(getResources().getString(R.string.fragment_projectmenu))
                .commit();*/

    }

    private class GeneratePDF extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            if(reportnumber==-1){
                ((MainActivity)getActivity()).GeneratePDFReport(currentproject,currentproject.getProjectReports().size()-1);
            }else {
                ((MainActivity)getActivity()).GeneratePDFReport(currentproject,reportnumber);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            v.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }
}
