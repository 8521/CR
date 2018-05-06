package com.ilaquidain.constructionreportersfi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;

//import com.google.android.gms.ads.AdView;
import com.ilaquidain.constructionreportersfi.qareports.f0020_MenuQAReports;
import com.ilaquidain.constructionreportersfi.qcreports.f0010_MenuQCReports;

import java.util.ArrayList;

public class f0002_MenuDocuments extends Fragment implements View.OnClickListener {

    private FragmentManager fm;
    private Saved_Info_Object savedinfo;
    private Project_Object currentproject;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private Integer projectnumber;
    private final String tag1 = "field1";

    //private AdView mAdView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        projectnumber = mpref.getInt("projectnumber",-1);

        if(savedinfo!=null && projectnumber !=-1){
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            if(savedinfo.getSavedProjects().get(projectnumber).getProjectReports().size()<
                    getActivity().getResources().getInteger(R.integer.reporttypenumber)){
                for(int i=savedinfo.getSavedProjects().get(projectnumber).getProjectReports().size();
                    i<getActivity().getResources().getInteger(R.integer.reporttypenumber);i++){
                    savedinfo.getSavedProjects().get(projectnumber).getProjectReports().add(
                            new ArrayList<Report_Object>());
                }
            }
        }
        else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).getSaved_info().getSavedProjects().set(projectnumber,currentproject);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f002_menudocuments,container,false);

        /*mAdView = v.findViewById(R.id.add_banner_1);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BED5CEE69ABEED9A21B0D1A4819302F8")
                .addTestDevice("934F37CC296CE37BD4D2E079C3446A48")
                .addTestDevice("FF28F97FA318BBF476FC55EA1D89C112")
                .build();
        mAdView.loadAd(adRequest);*/

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentproject.getProjectName());
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        }catch (Exception e){
            e.printStackTrace();
        }

        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        /*TextView projectname = v.findViewById(R.id.projectname);
        TextView projectref = v.findViewById(R.id.projectrefno);
        TextView projectaddress = v.findViewById(R.id.projectaddress);
        projectname.setText(currentproject.getProjectName());
        projectref.setText(currentproject.getProjectContractNo());
        projectaddress.setText(currentproject.getProjectAddress());

        SquareImageView projectlogo = v.findViewById(R.id.ProjectLogo);
        String StoredPath2 = currentproject.getProjectId()+".jpg";
        try{
            File f = new File(getActivity().getApplicationContext().getFilesDir(),StoredPath2);
            Bitmap bitmaplogo = BitmapFactory.decodeStream(new FileInputStream(f));
            projectlogo.setImageBitmap(bitmaplogo);
        }catch (Exception e){
            e.printStackTrace();
            //Drawable d = getResources().getDrawable(R.drawable.constructionreportericon);
            //projectlogo.setImageDrawable(d);
        }*/

        fm = getFragmentManager();

        RelativeLayout RelA = v.findViewById(R.id.rellay_main_1);
        RelA.setOnClickListener(this);
        RelativeLayout RelB = v.findViewById(R.id.rellay_main_2);
        RelB.setOnClickListener(this);
        RelativeLayout RelD = v.findViewById(R.id.rellay_main_3);
        RelD.setOnClickListener(this);
        RelativeLayout RelE = v.findViewById(R.id.rellay_main_4);
        RelE.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        mprefedit = mpref.edit();
        switch (v.getId()){
            case R.id.rellay_main_1:
                mprefedit.putString(tag1,"QC").apply();
                startnextfragment();
                break;
            case R.id.rellay_main_2:
                mprefedit.putString(tag1,"QA").apply();
                startnextfragment();
                break;
            case R.id.rellay_main_3:
                mprefedit.putString(tag1,"Prod").apply();
                startnextfragment();
                break;
            case R.id.rellay_main_4:
                mprefedit.putString(tag1,"Safety").apply();
                startnextfragment();
                break;
        }
    }

    private void startnextfragment(){
        f0003_MenuDocuments2 fgmt1 = new f0003_MenuDocuments2();
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                .replace(R.id.MainFrame,fgmt1,getResources().getString(R.string.level02))
                .addToBackStack(getResources().getString(R.string.level02))
                .commit();
    }

}

