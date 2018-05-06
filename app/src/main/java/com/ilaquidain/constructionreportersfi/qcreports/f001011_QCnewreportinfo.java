package com.ilaquidain.constructionreportersfi.qcreports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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

public class f001011_QCnewreportinfo extends Fragment implements View.OnClickListener {

    private Saved_Info_Object savedinfo;
    private Integer projectnumber, reportnumber, reporttypenumber;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private Report_Object currentreport;
    private int option2;

    private TextView date1;                     //8
    private TextView timein;                    //10
    private TextView timeout;                   //11
    private TextView contractor;                //13
    private TextView subcontractor;             //14
    private TextView contractno;                //15
    private TextView section;                   //16
    private TextView weather_sky;               //17
    private TextView weather_wind;              //18
    private TextView weather_precipitation;     //19
    private TextView temp_high;                 //21
    private TextView temp_low;                  //20
    private TextView discipline;                //7
    private TextView structureno;               //22
    private TextView station_location;          //23

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f01011_qcnewreportinfo,container,false);

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


        date1 = v.findViewById(R.id.df_projectinfo_5in);
        timein = v.findViewById(R.id.df_projectinfo_6in);
        timeout = v.findViewById(R.id.df_projectinfo_7in);
        contractor = v.findViewById(R.id.df_projectinfo_8in);
        subcontractor = v.findViewById(R.id.df_projectinfo_9in);
        contractno = v.findViewById(R.id.df_projectinfo_10in);
        section = v.findViewById(R.id.df_projectinfo_11in);
        weather_sky = v.findViewById(R.id.df_projectinfo_12in);
        weather_wind = v.findViewById(R.id.df_projectinfo_13in);
        weather_precipitation = v.findViewById(R.id.df_projectinfo_14in);
        temp_high = v.findViewById(R.id.df_projectinfo_15in);
        temp_low = v.findViewById(R.id.df_projectinfo_16in);
        discipline = v.findViewById(R.id.df_projectinfo_17in);
        structureno = v.findViewById(R.id.df_projectinfo_18in);
        station_location = v.findViewById(R.id.df_projectinfo_19in);

        date1.setText(currentreport.getReportinfo().get(8));
        timein.setText(currentreport.getReportinfo().get(10));
        timeout.setText(currentreport.getReportinfo().get(11));
        contractor.setText(currentreport.getReportinfo().get(13));
        subcontractor.setText(currentreport.getReportinfo().get(14));
        contractno.setText(currentreport.getReportinfo().get(15));
        section.setText(currentreport.getReportinfo().get(16));
        weather_sky.setText(currentreport.getReportinfo().get(17));
        String s1;
        if(!currentreport.getReportinfo().get(18).equals("")){
        s1 = currentreport.getReportinfo().get(18)+" mph";}else{
            s1=currentreport.getReportinfo().get(18);}
        weather_wind.setText(s1);
        weather_precipitation.setText(currentreport.getReportinfo().get(19));
        if(!currentreport.getReportinfo().get(21).equals("")){
        s1 = currentreport.getReportinfo().get(21)+" F";}else{
            s1 = currentreport.getReportinfo().get(21);}
        temp_high.setText(s1);
        if(!currentreport.getReportinfo().get(20).equals("")){
            s1 = currentreport.getReportinfo().get(20)+" F";}else{
            s1 = currentreport.getReportinfo().get(20);}
        temp_low.setText(s1);
        discipline.setText(currentreport.getReportinfo().get(7));
        structureno.setText(currentreport.getReportinfo().get(22));
        station_location.setText(currentreport.getReportinfo().get(23));

        date1.setOnClickListener(this);
        timein.setOnClickListener(this);
        timeout.setOnClickListener(this);
        contractor.setOnClickListener(this);
        subcontractor.setOnClickListener(this);
        contractno.setOnClickListener(this);
        section.setOnClickListener(this);
        weather_sky.setOnClickListener(this);
        weather_wind.setOnClickListener(this);
        weather_precipitation.setOnClickListener(this);
        temp_high.setOnClickListener(this);
        temp_low.setOnClickListener(this);
        discipline.setOnClickListener(this);
        structureno.setOnClickListener(this);
        station_location.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        final Fragment currentfragment = getActivity().getFragmentManager().findFragmentById(R.id.MainFrame);
        switch (v.getId()){
            case R.id.df_projectinfo_5in: //date
                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        myCalendar.set(Calendar.YEAR, i);
                        myCalendar.set(Calendar.MONTH, i1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        currentreport.getReportinfo().set(5,sdf.format(myCalendar.getTime()));
                        date1.setText(currentreport.getReportinfo().get(5));
                    }
                };
                new DatePickerDialog(getActivity(),R.style.dialogdatepicker, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.df_projectinfo_6in: //time in
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
                        timein.setText(time);
                        currentreport.getReportinfo().set(10,time);
                    }
                }, 07, 00, true);//Yes 24 hour time
                mTimePicker.show();
                break;
            case R.id.df_projectinfo_7in: // time out
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
                                timeout.setText(time);
                                currentreport.getReportinfo().set(11,time);
                            }
                        }, 16, 00, true);//Yes 24 hour time
                mTimePicker2.show();
                break;

            case R.id.df_projectinfo_13in: //weather wind
                numberselectiondialog(18);
                break;
            case R.id.df_projectinfo_15in: //temperature high
                numberselectiondialog(21);
                break;
            case R.id.df_projectinfo_16in: //temperature low
                numberselectiondialog(20);
                break;


            case R.id.df_projectinfo_8in: //contractor
                inputtextmethod(13, getResources().getString(R.string.Contractor));
                break;
            case R.id.df_projectinfo_9in: //subcontractor
                inputtextmethod(14,getResources().getString(R.string.Subcontractor));
                break;
            case R.id.df_projectinfo_10in: //contactno
                inputtextmethod(15,getResources().getString(R.string.ContractNo));
                break;
            case R.id.df_projectinfo_11in: //section
                inputtextmethod(16,getResources().getString(R.string.Section));
                break;
            case R.id.df_projectinfo_12in: //weather sky
                choosedialogfrag(17);
                break;
            case R.id.df_projectinfo_14in:
                inputtextmethod(19,getResources().getString(R.string.Precipitation));
                break;
            case R.id.df_projectinfo_17in:
                inputtextmethod(7,getResources().getString(R.string.Discipline));
                break;
            case R.id.df_projectinfo_18in:
                inputtextmethod(22,getResources().getString(R.string.StructureNo));
                break;
            case R.id.df_projectinfo_19in:
                inputtextmethod(23,getResources().getString(R.string.StationLocation));
                break;
        }
    }

    private void inputtextmethod(final int input, final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final EditText etxt = new EditText(getActivity());
        builder.setView(etxt);
        etxt.setText(currentreport.getReportinfo().get(input));
        etxt.setSelection(etxt.getText().length());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (input){
                    case 13:currentreport.getReportinfo().set(13,etxt.getText().toString());
                        contractor.setText(etxt.getText().toString());break;
                    case 14:currentreport.getReportinfo().set(14,etxt.getText().toString());
                        subcontractor.setText(etxt.getText().toString());break;
                    case 15:currentreport.getReportinfo().set(15,etxt.getText().toString());
                        contractno.setText(etxt.getText().toString());break;
                    case 16:currentreport.getReportinfo().set(16,etxt.getText().toString());
                        section.setText(etxt.getText().toString());break;
                    case 17:currentreport.getReportinfo().set(17,etxt.getText().toString());
                        weather_sky.setText(etxt.getText().toString());break;
                    case 18:currentreport.getReportinfo().set(18,etxt.getText().toString());
                        weather_wind.setText(etxt.getText().toString());break;
                    case 19:currentreport.getReportinfo().set(19,etxt.getText().toString());
                        weather_precipitation.setText(etxt.getText().toString());break;
                    case 21:currentreport.getReportinfo().set(21,etxt.getText().toString());
                        temp_high.setText(etxt.getText().toString());break;
                    case 20:currentreport.getReportinfo().set(20,etxt.getText().toString());
                        temp_low.setText(etxt.getText().toString());break;
                    case 7:currentreport.getReportinfo().set(7,etxt.getText().toString());
                        discipline.setText(etxt.getText().toString());break;
                    case 22:currentreport.getReportinfo().set(22,etxt.getText().toString());
                        structureno.setText(etxt.getText().toString());break;
                    case 23:currentreport.getReportinfo().set(23,etxt.getText().toString());
                        station_location.setText(etxt.getText().toString());break;
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

    private void choosedialogfrag(Integer option){
        option2 = option;

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mprefedit = mpref.edit();
        mprefedit.putInt("option",option2);
        mprefedit.apply();

        chooselist_dialogfragment dialog_choose = new chooselist_dialogfragment();

        //una vez cerramos el dialog window volvemos a cargar el current report por si se hubiera modificado
        dialog_choose.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                savedinfo = ((MainActivity)getActivity()).getSaved_info();
                if(reportnumber == -1){
                    currentreport = savedinfo.getTemp_report();
                }else {
                    currentreport = savedinfo.getSavedProjects().
                            get(projectnumber).getProjectReports().get(reporttypenumber).get(reportnumber);
                }
                //Cambiamos el campo que se haya modificado con el dialog window
                settext();
            }

            private void settext() {
                switch (option2){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 17:
                        weather_sky.setText(currentreport.getReportinfo().get(17));
                        break;
                }
            }
        });
        dialog_choose.show(getActivity().getFragmentManager(),"Dialog");
    }
    private void numberselectiondialog(final Integer option){
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v2 = inflater.inflate(R.layout.dialog_numberpicker,null);
        b.setView(v2);
        final NumberPicker np = v2.findViewById(R.id.numberPicker1);
        TextView txt = v2.findViewById(R.id.txtview_dialognumberpicker);
        TextView txt2 = v2.findViewById(R.id.txt_numberpicker_title);

        switch (option){
            case 18:
                txt2.setText("Wind Speed");
                txt.setText("mph");
                np.setMinValue(0);np.setMaxValue(35);
                if(!currentreport.getReportinfo().get(18).equals("")){
                    np.setValue(Integer.valueOf(currentreport.getReportinfo().get(18)));}
                break;
            case 20:
                txt2.setText("Temperature Low");
                txt.setText("F");
                np.setMinValue(0);np.setMaxValue(120);
                if(!currentreport.getReportinfo().get(20).equals("")){
                    np.setValue(Integer.valueOf(currentreport.getReportinfo().get(20)));}
                break;
            case 21:
                txt2.setText("Temperature High");
                txt.setText("F");
                np.setMinValue(0);np.setMaxValue(120);
                if(!currentreport.getReportinfo().get(21).equals("")){
                    np.setValue(Integer.valueOf(currentreport.getReportinfo().get(21)));}
                break;
        }

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s1 = String.valueOf(np.getValue());
                switch (option){
                    case 18:
                        currentreport.getReportinfo().set(18,s1);
                        s1 = s1+" mph";
                        weather_wind.setText(s1);
                        break;
                    case 20:
                        currentreport.getReportinfo().set(20,s1);
                        s1 = s1+" F";
                        temp_low.setText(s1);
                        break;
                    case 21:
                        currentreport.getReportinfo().set(21,s1);
                        s1 = s1+" F";
                        temp_high.setText(s1);
                        break;
                }
            }
        });

        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        b.show();
    }
}
