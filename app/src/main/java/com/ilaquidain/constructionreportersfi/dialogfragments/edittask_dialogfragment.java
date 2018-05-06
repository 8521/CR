package com.ilaquidain.constructionreportersfi.dialogfragments;

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
import android.widget.EditText;
import android.widget.TextView;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import com.ilaquidain.constructionreportersfi.object.Task_Object;

public class edittask_dialogfragment extends DialogFragment implements View.OnClickListener {

    private Task_Object task;
    private EditText editText;
    private Saved_Info_Object savedinfo;
    private Report_Object currentreport;
    private int projectnumber, reportnumber, reporttypenumber, tasknumber;
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
        View v = inflater.inflate(R.layout.f020121_dialogfragment,container,false);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        savedinfo = ((MainActivity)getActivity()).getSaved_info();

        if(savedinfo!=null && projectnumber != -1  && reporttypenumber !=-1 && reportnumber!=-1) {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports().get(reporttypenumber).get(reportnumber);
        }else if(savedinfo!=null && projectnumber != -1 && reporttypenumber !=-1){
            currentreport = savedinfo.getTemp_report();
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }

        task = currentreport.getSelectedactivities().get(tasknumber);
        editText = v.findViewById(R.id.edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        TextView title = v.findViewById(R.id.recyclerviewtitle);

        if(task!=null){
            String s1 = task.getTaskName()+" Description";
            title.setText(s1);
        editText.setText(task.getTaskDescription());
        editText.setSelection(editText.getText().length());}

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
        task.setTaskDescription(editText.getText().toString());
        currentreport.getSelectedactivities().set(tasknumber,task);
        if(reportnumber==-1){
            savedinfo.setTemp_report(currentreport);
        }else {
            savedinfo.getSavedProjects().get(projectnumber).getProjectReports()
                    .get(reporttypenumber).set(reportnumber,currentreport);
        }
        ((MainActivity)getActivity()).setSaved_info(savedinfo);
        getDialog().dismiss();
    }
}
