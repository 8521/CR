package com.ilaquidain.constructionreporterpro.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilaquidain.constructionreporterpro.R;
import com.ilaquidain.constructionreporterpro.activities.MainActivity;
import com.ilaquidain.constructionreporterpro.object.Project_Object;
import com.ilaquidain.constructionreporterpro.object.Saved_Info_Object;
import com.ilaquidain.constructionreporterpro.object.SquareImageView;

import java.io.File;
import java.io.FileInputStream;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class F_1_1_ProjectMenu extends Fragment implements View.OnClickListener {

    private FragmentManager fm;
    private Saved_Info_Object savedinfo;
    private Project_Object currentproject;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private Integer projectnumber;
    //private AdView mAdView;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        projectnumber = mpref.getInt("projectnumber",-1);

        if(savedinfo!=null && projectnumber !=-1){
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
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
        View v = inflater.inflate(R.layout.fragment_main,container,false);

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Project Menu");
        }catch (Exception e){
            e.printStackTrace();
        }



        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        TextView projectname = (TextView)v.findViewById(R.id.projectname);
        TextView projectref = (TextView)v.findViewById(R.id.projectrefno);
        TextView projectaddress = (TextView)v.findViewById(R.id.projectaddress);
        projectname.setText(currentproject.getProjectName());
        projectref.setText(currentproject.getProjectRefNo());
        projectaddress.setText(currentproject.getProjectAddress());

        SquareImageView projectlogo = (SquareImageView)v.findViewById(R.id.ProjectLogo);
        String StoredPath2 = currentproject.getProjectId()+".jpg";
        Bitmap bitmaplogo = null;
        try{
            File f = new File(getActivity().getApplicationContext().getFilesDir(),StoredPath2);
            bitmaplogo = BitmapFactory.decodeStream(new FileInputStream(f));
        }catch (Exception e){e.printStackTrace();}
        if(bitmaplogo!=null){
            projectlogo.setImageBitmap(bitmaplogo);
        }

        fm = getFragmentManager();

        RelativeLayout RelA = (RelativeLayout)v.findViewById(R.id.OptionA);
        RelA.setOnClickListener(this);
        RelativeLayout RelB = (RelativeLayout)v.findViewById(R.id.OptionB);
        RelB.setOnClickListener(this);
        RelativeLayout RelD = (RelativeLayout)v.findViewById(R.id.RelLay_ViewPDFs);
        RelD.setOnClickListener(this);
        RelativeLayout RelE = (RelativeLayout)v.findViewById(R.id.RelLay_EditContractors);
        RelE.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.OptionA:
                //Create new report
                F_1_1_1_NewReport fgmt = new F_1_1_1_NewReport();
                mprefedit = mpref.edit();
                mprefedit.putInt("reportnumber", -1);
                mprefedit.putInt("newreport",1);
                mprefedit.apply();

                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .add(R.id.MainFrame,fgmt,getResources().getString(R.string.fragment_newreport))
                        .addToBackStack(getResources().getString(R.string.fragment_newreport))
                        .commit();
                break;
            case R.id.OptionB:
                F_1_1_2_EditViewReports fgmt2 = new F_1_1_2_EditViewReports();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt2,getResources().getString(R.string.fragment_editviewreports))
                        .addToBackStack(getResources().getString(R.string.fragment_editviewreports))
                        .commit();
                break;
            case R.id.RelLay_ViewPDFs:
                F_1_1_3_ViewPdfs fgmt4 = new F_1_1_3_ViewPdfs();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt4,getResources().getString(R.string.fragment_viewpdfs))
                        .addToBackStack(getResources().getString(R.string.fragment_viewpdfs))
                        .commit();
                break;
            case R.id.RelLay_EditContractors:
                F_1_1_4_Contractors fgmt5 = new F_1_1_4_Contractors();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt5,getResources().getString(R.string.fragment_contractors))
                        .addToBackStack(getResources().getString(R.string.fragment_contractors))
                        .commit();
        }
    }

}

