package com.ilaquidain.constructionreportersfi.qcreports;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.EquipPeopleHours_QC_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;

public class f001013_z2_qcnewreportequipmanedit_df extends DialogFragment implements View.OnClickListener {

    private EquipPeopleHours_QC_Object equipman;
    private EditText equipment;
    private EditText numberofpeople;
    private EditText numberofhours;

    private Saved_Info_Object savedinfo;
    private Report_Object currentreport;
    private int projectnumber, reportnumber, equipmannumber, reporttypenumber;
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

    private  DialogInterface.OnDismissListener onDismissListener3;
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener2) {
        this.onDismissListener3 = onDismissListener2;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener3 != null) {
            onDismissListener3.onDismiss(dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f010131_dialogfragment_equipman,container,false);

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        equipmannumber = mpref.getInt("equipmannumber",-1);
        if(equipmannumber ==-1){equipman=new EquipPeopleHours_QC_Object();}

        if(savedinfo==null || projectnumber==-1 ){
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }else if(reportnumber==-1){
            currentreport = savedinfo.getTemp_report();
            if(equipmannumber !=-1){equipman = currentreport.getSelectedEquipPeopleHours().get(equipmannumber);}
        }else {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports()
                    .get(reporttypenumber).get(reportnumber);
            if(equipmannumber !=-1){equipman = currentreport.getSelectedEquipPeopleHours().get(equipmannumber);}
        }

        equipment = v.findViewById(R.id.input1_equipmandf);
        numberofpeople = v.findViewById(R.id.input2_equipmandf);
        numberofhours = v.findViewById(R.id.input3_equipmandf);

        equipment.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        if(equipman!=null){
            equipment.setText(equipman.getEquipment1());
            if(equipman.getNumberOfPeople1()==0){
                numberofpeople.setText("");
            }else{numberofpeople.setText(String.valueOf(equipman.getNumberOfPeople1()));}
            if(equipman.getHours1()==0){
                numberofhours.setText("");
            }else{numberofhours.setText(String.valueOf(equipman.getHours1()));}
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

        if(numberofhours.getText().toString().equals("")){numberofhours.setError("Field cannot be blank");}
        else if(equipment.getText().toString().equals("")&&numberofpeople.getText().toString().equals("")){
            equipment.setError("Both fields cannot be blank");
            numberofpeople.setError("Both field cannot be blank");
        }

        if(numberofhours.getText().toString().equals("")){
            equipment.setError("Hours cannot be blank");
            return;
        }
        equipman.setEquipment1(equipment.getText().toString());
        if(!numberofpeople.getText().toString().equals("")){
            equipman.setNumberOfPeople1(Double.parseDouble(numberofpeople.getText().toString()));}
        if(!numberofhours.getText().toString().equals("")){
            equipman.setHours1(Double.parseDouble(numberofhours.getText().toString()));
        }

        if(equipmannumber ==-1){
            currentreport.getSelectedEquipPeopleHours().add(equipman);
        }else{
            currentreport.getSelectedEquipPeopleHours().set(equipmannumber,equipman);
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
