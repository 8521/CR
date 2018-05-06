package com.ilaquidain.constructionreportersfi.qareports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.dialogfragments.chooselist_dialogfragment;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class f002011_QAnewreportinfo extends Fragment implements View.OnClickListener {

    private Saved_Info_Object savedinfo;
    private Integer projectnumber, reportnumber, reporttypenumber;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private Report_Object currentreport;

    private TextView reportdate;
    private TextView discipline;
    private TextView reportweather;

    private TextView startime;
    private TextView endtime;

    private TextView location;
    private TextView contractor;
    private TextView activityids;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f02011_qanewreportinfo,container,false);

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber", -1);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        
        if(savedinfo!=null && projectnumber != -1  && reporttypenumber!=-1 && reportnumber!=-1) {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports()
                    .get(reporttypenumber).get(reportnumber);
        }else if(savedinfo!=null && projectnumber != -1 && reporttypenumber!=-1){
            currentreport = savedinfo.getTemp_report();
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }
        
        FloatingActionButton fabaccet = v.findViewById(R.id.fabaccept);
        fabaccet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitMethod();
            }
        });

        reportdate = v.findViewById(R.id.df_projectinfo_3);
        discipline = v.findViewById(R.id.df_projectinfo_9);
        reportweather = v.findViewById(R.id.df_projectinfo_12);
        startime = v.findViewById(R.id.df_projectinfo_15);
        endtime = v.findViewById(R.id.df_projectinfo_16);
        location = v.findViewById(R.id.df_projectinfo_20);
        contractor = v.findViewById(R.id.df_projectinfo_22);
        activityids = v.findViewById(R.id.df_projectinfo_24);

        reportdate.setText(currentreport.getReportinfo().get(8));
        discipline.setText(currentreport.getReportinfo().get(7));
        reportweather.setText(currentreport.getReportinfo().get(17));
        startime.setText(currentreport.getReportinfo().get(10));
        endtime.setText(currentreport.getReportinfo().get(11));
        location.setText(currentreport.getReportinfo().get(12));
        contractor.setText(currentreport.getReportinfo().get(13));
        activityids.setText(currentreport.getReportinfo().get(9));

        reportdate.setOnClickListener(this);
        discipline.setOnClickListener(this);
        reportweather.setOnClickListener(this);
        startime.setOnClickListener(this);
        endtime.setOnClickListener(this);
        location.setOnClickListener(this);
        contractor.setOnClickListener(this);
        activityids.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        final Fragment currentfragment = getActivity().getFragmentManager().findFragmentById(R.id.MainFrame);
        switch (v.getId()){
            case R.id.df_projectinfo_15:
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.dialogdatepicker,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String mhour, mminute;
                        if(selectedHour<10){mhour="0"+String.valueOf(selectedHour);}
                        else {mhour=String.valueOf(selectedHour);}
                        if(selectedMinute<10){mminute="0"+String.valueOf(selectedMinute);}
                        else {mminute=String.valueOf(selectedMinute);}
                        String time = mhour+":"+mminute;
                        startime.setText(time);
                        currentreport.getReportinfo().set(10,time);
                    }
                }, 07, 00, true);//Yes 24 hour time
                mTimePicker.show();
                break;
            case R.id.df_projectinfo_16:
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(getActivity(),R.style.dialogdatepicker,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String mhour, mminute;
                                if(selectedHour<10){mhour="0"+String.valueOf(selectedHour);}
                                else {mhour=String.valueOf(selectedHour);}
                                if(selectedMinute<10){mminute="0"+String.valueOf(selectedMinute);}
                                else {mminute=String.valueOf(selectedMinute);}
                                String time = mhour+":"+mminute;
                                endtime.setText(time);
                                currentreport.getReportinfo().set(11,time);
                            }
                        }, 16, 00, true);//Yes 24 hour time
                mTimePicker2.show();
                break;
            case R.id.df_projectinfo_3:
                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        currentreport.getReportinfo().set(8,sdf.format(myCalendar.getTime()));
                        reportdate.setText(currentreport.getReportinfo().get(8));
                    }
                };
                new DatePickerDialog(getActivity(),R.style.dialogdatepicker, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.df_projectinfo_6:
            case R.id.df_projectinfo_9:
            case R.id.df_projectinfo_12:

                /*Bundle bundle2 = new Bundle();
                bundle2.putSerializable("currentreport",currentreport);*/
                Integer option =0;
                switch (v.getId()){
                    case R.id.df_projectinfo_6:option=6;break;
                    case R.id.df_projectinfo_9:option=7;break;
                    case R.id.df_projectinfo_12:option =5;break;
                }

                mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
                mprefedit = mpref.edit();
                mprefedit.putInt("option",option);
                mprefedit.apply();

                chooselist_dialogfragment dialog_choose = new chooselist_dialogfragment();

                dialog_choose.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        savedinfo = ((MainActivity)getActivity()).getSaved_info();
                        if(reportnumber == -1){
                            currentreport = savedinfo.getTemp_report();
                        }else {
                            currentreport = savedinfo.getSavedProjects().
                                    get(projectnumber).getProjectReports()
                                    .get(reporttypenumber).get(reportnumber);
                        }
                        reportdate.setText(currentreport.getReportinfo().get(8));
                        discipline.setText(currentreport.getReportinfo().get(7));
                        reportweather.setText(currentreport.getReportinfo().get(17));
                        startime.setText(currentreport.getReportinfo().get(10));
                        endtime.setText(currentreport.getReportinfo().get(11));
                        location.setText(currentreport.getReportinfo().get(12));
                        contractor.setText(currentreport.getReportinfo().get(13));
                        activityids.setText(currentreport.getReportinfo().get(9));
                    }
                });
                dialog_choose.show(getActivity().getFragmentManager(),"Dialog");
                //dialog_choose.show(getFragmentManager(),"Dialog2");
                break;
            case R.id.df_projectinfo_20:
                inputtextmethod("Location");
                break;
            case R.id.df_projectinfo_22:
                inputtextmethod("Contractor");
                break;
            case R.id.df_projectinfo_24:
                inputtextmethod("ActivityID");
                break;
        }
    }

    private void inputtextmethod(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText etxt = new EditText(getActivity());
        builder.setView(etxt);
        if(title.equals("Location")){
            etxt.setText(currentreport.getReportinfo().get(12));
            etxt.setSelection(etxt.getText().length());
        }else if(title.equals("Contractor")){
            etxt.setText(currentreport.getReportinfo().get(13));
            etxt.setSelection(etxt.getText().length());
        }else if(title.equals("ActivityID")){
            etxt.setText(currentreport.getReportinfo().get(9));
            etxt.setSelection(etxt.getText().length());
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!etxt.equals("")){
                    if(title.equals("Location")){
                        currentreport.getReportinfo().set(12,etxt.getText().toString());
                        location.setText(etxt.getText().toString());
                    }else if(title.equals("Contractor")){
                        currentreport.getReportinfo().set(13,etxt.getText().toString());
                        contractor.setText(etxt.getText().toString());
                    }else if(title.equals("ActivityID")){
                        currentreport.getReportinfo().set(9,etxt.getText().toString());
                        activityids.setText(etxt.getText().toString());
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void ExitMethod() {
        if(reportnumber==-1){
            savedinfo.setTemp_report(currentreport);
        }else {
            savedinfo.getSavedProjects().get(projectnumber).getProjectReports().get(reporttypenumber)
                    .set(reportnumber,currentreport);
        }
        ((MainActivity)getActivity()).setSaved_info(savedinfo);

        if ( getFragmentManager().getBackStackEntryCount() > 0)
        {getFragmentManager().popBackStack();}
    }

}
