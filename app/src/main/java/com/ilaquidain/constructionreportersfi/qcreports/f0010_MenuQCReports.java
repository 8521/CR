package com.ilaquidain.constructionreportersfi.qcreports;

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

//import com.google.android.gms.ads.AdView;
import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;

public class f0010_MenuQCReports extends Fragment implements View.OnClickListener {

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
        projectnumber = mpref.getInt("projectnumber",-1); //obtenemos el numero de proyecto

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
        View v = inflater.inflate(R.layout.f010_menuqcprojects,container,false);

        /*mAdView = v.findViewById(R.id.add_banner_1);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("BED5CEE69ABEED9A21B0D1A4819302F8")
                .addTestDevice("934F37CC296CE37BD4D2E079C3446A48")
                .addTestDevice("FF28F97FA318BBF476FC55EA1D89C112")
                .build();
        mAdView.loadAd(adRequest);*/

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(currentproject.getProjectName());
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("QC Report");
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

        RelativeLayout RelA = v.findViewById(R.id.qcmainmenu_rellay1);
        RelA.setOnClickListener(this);
        RelativeLayout RelB = v.findViewById(R.id.qcmainmenu_rellay2);
        RelB.setOnClickListener(this);
        RelativeLayout RelD = v.findViewById(R.id.qcmainmenu_rellay3);
        RelD.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qcmainmenu_rellay1: //Create new QC report
                f00101_QCnewreport fgmt = new f00101_QCnewreport();
                mprefedit = mpref.edit();
                mprefedit.putInt("reportnumber", -1);
                mprefedit.putInt("newreport",1);
                mprefedit.apply();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .add(R.id.MainFrame,fgmt,getResources().getString(R.string.level101))
                        .addToBackStack(getResources().getString(R.string.level101))
                        .commit();
                break;
            case R.id.qcmainmenu_rellay2: // Edit existing QC reports
                f00102_QCeditreport fgmt2 = new f00102_QCeditreport();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt2,getResources().getString(R.string.level102))
                        .addToBackStack(getResources().getString(R.string.level102))
                        .commit();
                break;
            case R.id.qcmainmenu_rellay3: //View QC Report PDFs
                f00103_QCviewpdf fgmt4 = new f00103_QCviewpdf();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,fgmt4,getResources().getString(R.string.level103))
                        .addToBackStack(getResources().getString(R.string.level103))
                        .commit();
                break;

        }
    }

}

