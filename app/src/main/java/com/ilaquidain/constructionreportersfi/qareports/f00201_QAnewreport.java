package com.ilaquidain.constructionreportersfi.qareports;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

//import com.google.android.gms.ads.InterstitialAd;
import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class f00201_QAnewreport extends Fragment implements View.OnClickListener{

    public f00201_QAnewreport() {
        super();
    }

    private Project_Object currentproject;
    private Integer projectnumber = null;
    private Integer reportnumber = null;
    private Integer reporttypenumber = null;
    private Report_Object currentreport = null;
    private Saved_Info_Object savedinfo = null;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private View v;
    //private InterstitialAd mInterstitialAd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.f0201_qanewreport,container,false);
        v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        /*mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-9978137837326968/9780327651");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("BED5CEE69ABEED9A21B0D1A4819302F8")
                .addTestDevice("934F37CC296CE37BD4D2E079C3446A48")
                .addTestDevice("FF28F97FA318BBF476FC55EA1D89C112")
                .build());*/

        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);

        projectnumber = mpref.getInt("projectnumber",-1);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);

        if(savedinfo==null || projectnumber==-1 || reporttypenumber==-1){
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }else {
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
        }

        if(reportnumber == -1){
            int newreport = mpref.getInt("newreport",1);
            if(newreport == 1){
                currentreport = new Report_Object();
                currentreport.setReporttype(getActivity().getResources().getStringArray(
                        R.array.reporttypes)[1]);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("New Report");
            }else {
                currentreport = savedinfo.getTemp_report();
                String s1 = currentreport.getReportinfo().get(8)+"-QA Report";
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Edit: "+s1);
            }
            savedinfo.setTemp_report(currentreport);
            mprefedit = mpref.edit();
            mprefedit.putInt("newreport",0);
            mprefedit.apply();
            ((MainActivity)getActivity()).setSaved_info(savedinfo);
        }else {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports().
                    get(reporttypenumber).get(reportnumber);
            String s1 = currentreport.getReportinfo().get(8)+" QA Report";
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Edit: "+s1);
        }

        RelativeLayout lay1 = v.findViewById(R.id.lay1);
        lay1.setOnClickListener(this);
        RelativeLayout lay5 = v.findViewById(R.id.lay5);
        lay5.setOnClickListener(this);
        //RelativeLayout lay6 = v.findViewById(R.id.lay6);
        //lay6.setOnClickListener(this);
        //RelativeLayout lay7 = v.findViewById(R.id.lay7);
        //lay7.setOnClickListener(this);
        RelativeLayout lay8 = v.findViewById(R.id.lay8);
        lay8.setOnClickListener(this);
        FloatingActionButton fabaccept = v.findViewById(R.id.fabsave);
        FloatingActionButton fabpdf = v.findViewById(R.id.fabpdf);
        fabaccept.setOnClickListener(this);
        fabpdf.setOnClickListener(this);

        setDefaultValues();

        return v;
    }

    private void setDefaultValues() {
        if(currentreport.getReportinfo().get(1).equals("")){
            currentreport.getReportinfo().set(1,savedinfo.getListasOpciones().get(0).get(1));}
        if(currentreport.getReportinfo().get(2).equals("")){
            currentreport.getReportinfo().set(2,savedinfo.getListasOpciones().get(0).get(2));}
        if(currentreport.getReportinfo().get(3).equals("")){
            currentreport.getReportinfo().set(3,savedinfo.getListasOpciones().get(0).get(3));}
        if(currentreport.getReportinfo().get(8).equals("")){
            currentreport.getReportinfo().set(8,new SimpleDateFormat("MM/dd/yy",Locale.US).format(new Date()));}
        if(currentreport.getReportinfo().get(6).equals("")){
            currentreport.getReportinfo().set(6,savedinfo.getListasOpciones().get(0).get(6));}
        if (currentreport.getReportinfo().get(7).equals("")){
            currentreport.getReportinfo().set(7,savedinfo.getListasOpciones().get(0).get(7));}
        if(currentreport.getReportinfo().get(5).equals("")){
            currentreport.getReportinfo().set(5,savedinfo.getListasOpciones().get(0).get(5));}
        if(currentreport.getReportinfo().get(10).equals("")){
            currentreport.getReportinfo().set(10,savedinfo.getListasOpciones().get(0).get(10));}
        if(currentreport.getReportinfo().get(11).equals("")){
            currentreport.getReportinfo().set(11,savedinfo.getListasOpciones().get(0).get(11));}
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        switch (v.getId()){
            case R.id.lay1:
                f002011_QAnewreportinfo reportinfo = new f002011_QAnewreportinfo();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit,
                                R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,reportinfo,getResources().getString(R.string.level2011))
                        .addToBackStack(getResources().getString(R.string.level2011))
                        .commit();
                break;
            case R.id.lay5:
                f002012_QAnewreportactivities fgmt7 = new f002012_QAnewreportactivities();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit,
                                R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,fgmt7,getResources().getString(R.string.level2012))
                        .addToBackStack(getResources().getString(R.string.level2012))
                        .commit();
                break;
            case R.id.lay8:
                f002013_QAnewreportphotos dialogphotos = new f002013_QAnewreportphotos();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit,
                                R.animator.slide_right_enter,R.animator.slide_left_exit)
                        .replace(R.id.MainFrame,dialogphotos,getResources().getString(R.string.level2013))
                        .addToBackStack(getResources().getString(R.string.level2013))
                        .commit();
                break;
            case R.id.fabsave:
                ExitFragment(0);
                break;
            case R.id.fabpdf:
                ExitFragment(1);
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    ExitFragment(1);
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
                });*/
                break;
        }
    }

    public void ExitFragment(int createpdf) {
        //Si el informe ya existia y se habia guardado
        if(createpdf==1 && reportnumber != -1){
            new GeneratePDF().execute();
            //Si el informe no exisitia - primero se guarda y luego se genera el PDF
        }else if(createpdf==1){
            currentproject.getProjectReports().get(reporttypenumber).add(currentreport);
            savedinfo.getSavedProjects().set(projectnumber,currentproject);
            ((MainActivity)getActivity()).setSaved_info(savedinfo);
            //((MainActivity)getActivity()).SaveInfoToMemory();
            new GeneratePDF().execute();
        } else {
            //Solo se guarda un informe nuevo per no se genera el` PDF
            if(reportnumber == -1){
                currentproject.getProjectReports().get(reporttypenumber).add(savedinfo.getTemp_report());
            }else {
                //Se guardan los cambios en un informe que ya existia pero no se modifica
                currentproject.getProjectReports().get(reporttypenumber).set(reportnumber,currentreport);
            }
            Snackbar.make(getActivity().getWindow().getDecorView().getRootView().findViewById(R.id.relativelayout_newreport),
                    "Report Saved Succesfully",Snackbar.LENGTH_SHORT).show();
        }
        savedinfo.getSavedProjects().set(projectnumber,currentproject);
        ((MainActivity)getActivity()).setSaved_info(savedinfo);
        //((MainActivity)getActivity()).SaveInfoToMemory();
    }

    private class GeneratePDF extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if(reportnumber==-1){
                ((MainActivity)getActivity()).GeneratePDFReport(currentproject,reporttypenumber,
                        currentproject.getProjectReports().get(reporttypenumber).size()-1);
            }else {
                ((MainActivity)getActivity()).GeneratePDFReport(currentproject,reporttypenumber,reportnumber);
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
