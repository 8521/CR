package com.ilaquidain.constructionreportersfi.qcreports;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import com.ilaquidain.constructionreportersfi.object.Task_Object;

public class f001012_z2_qcnewreportactitvitiesedit_df extends DialogFragment implements View.OnClickListener {

    private Task_Object task;
    private EditText activity_id;
    private EditText activity_dwgspec;
    private EditText activity_description;
    private CheckBox chkyes,chkno;

    private Saved_Info_Object savedinfo;
    private Report_Object currentreport;
    private int projectnumber, reportnumber, tasknumber, reporttypenumber;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(),getTheme()) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                ExitMethod();
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if(dialog!=null){
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            int Width = ViewGroup.LayoutParams.MATCH_PARENT;
            int Height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(Width,Height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f010121_dialogfragment_activities,container,false);

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);
        tasknumber = mpref.getInt("tasknumber",-1);
        if(tasknumber==-1){task=new Task_Object();}

        if(savedinfo==null || projectnumber==-1 || reporttypenumber == -1){
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }else if(reportnumber==-1){
            currentreport = savedinfo.getTemp_report();
            if(tasknumber!=-1){task = currentreport.getSelectedactivities().get(tasknumber);}
        }else {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).
                    getProjectReports().get(reporttypenumber).get(reportnumber);
            if(tasknumber!=-1){task = currentreport.getSelectedactivities().get(tasknumber);}
        }


        activity_id = v.findViewById(R.id.qcactivity_in2);
        activity_dwgspec = v.findViewById(R.id.qcactivity_in1);
        activity_description = v.findViewById(R.id.qcactivity_in3);
        chkyes = v.findViewById(R.id.chkbox_yes);
        chkno = v.findViewById(R.id.chkbox_no);

        activity_description.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        chkyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setAct_Compliance(0);
                chkno.setChecked(!chkno.isChecked());
            }
        });
        chkno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setAct_Compliance(1);
                chkyes.setChecked(!chkyes.isChecked());
            }
        });

        if(task!=null){
            if(task.getAct_Compliance()!=0){chkno.setChecked(true);chkyes.setChecked(false);}else{
                chkno.setChecked(false);chkyes.setChecked(true);}
            activity_id.setText(task.getAct_No());
            activity_id.setSelection(activity_id.getText().length());
            activity_dwgspec.setText(task.getAct_dwgspec());
            activity_dwgspec.setSelection(activity_dwgspec.getText().length());
            activity_description.setText(task.getTaskDescription());
            activity_description.setSelection(activity_description.getText().length());
            if(task.getAct_Compliance()!=0){
                chkyes.setChecked(false);
                chkno.setChecked(true);
            }
        }

        FloatingActionButton fabaccept = v.findViewById(R.id.fabaccept);
        fabaccept.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fabaccept){
            ExitMethod();
        }
    }

    private void ExitMethod(){
        if(activity_id.getText().toString().equals("")){
            activity_id.setError("Field cannot be blank");
            return;
        }

        task.setAct_No(activity_id.getText().toString());
        task.setAct_dwgspec(activity_dwgspec.getText().toString());
        task.setTaskDescription(activity_description.getText().toString());

        if(tasknumber==-1){
            currentreport.getSelectedactivities().add(task);
        }else{
            currentreport.getSelectedactivities().set(tasknumber,task);
        }
        if(reportnumber==-1){
            savedinfo.setTemp_report(currentreport);
        }else {
            savedinfo.getSavedProjects().get(projectnumber).getProjectReports().get(reporttypenumber)
                    .set(reportnumber,currentreport);
        }
        ((MainActivity)getActivity()).setSaved_info(savedinfo);
        getDialog().dismiss();
    }
}
