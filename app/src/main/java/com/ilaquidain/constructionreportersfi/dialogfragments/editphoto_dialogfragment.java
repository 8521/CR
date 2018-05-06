package com.ilaquidain.constructionreportersfi.dialogfragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Image_Object;
import com.ilaquidain.constructionreportersfi.object.Project_Object;

import java.util.ArrayList;

public class editphoto_dialogfragment extends DialogFragment implements View.OnClickListener {
    private Spinner spinner;
    private TextView textView2;
    private Image_Object photoobject;
    private Integer projectnumber;
    private Project_Object currentproject;
    private SharedPreferences mpref;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog!=null && dialog.getWindow()!=null){
            int Width = ViewGroup.LayoutParams.MATCH_PARENT;
            int Height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(Width,Height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f020131_photosdialfrag,container,false);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        if(projectnumber==-1){
            if(getFragmentManager().getBackStackEntryCount() > 0){
                getFragmentManager().popBackStack();}
        }else{
            currentproject = ((MainActivity)getActivity()).getSaved_info().getSavedProjects().get(projectnumber);
        }

        ArrayList<String> availableactivities = new ArrayList<>();
        for(int i=0;i<currentproject.getListAvailableTasks().size();i++){
            availableactivities.add(currentproject.getListAvailableTasks().
            get(i).getTaskName());
        }
        photoobject = (Image_Object)getArguments().getSerializable("photo");
        Bitmap photoshown = getArguments().getParcelable("bitmap");
        getArguments().remove("photo");
        getArguments().remove("bitmap");

        ImageView imageView = v.findViewById(R.id.image1);
        spinner = v.findViewById(R.id.spinner1);

        TextView textView1 = v.findViewById(R.id.textview1);
        textView2 = v.findViewById(R.id.textview2);

        textView2.setOnClickListener(this);

        FloatingActionButton fabaccept = v.findViewById(R.id.fabaccept);
        fabaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitMethod();
            }
        });


        if(!availableactivities.contains("N/A")){availableactivities.add(0,"N/A");}
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_simpleitem, availableactivities); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdownitem);
        spinner.setAdapter(spinnerArrayAdapter);

        if(photoobject.getPhotoActivity()!=null && availableactivities.contains(photoobject.getPhotoActivity())){
            spinner.setSelection(availableactivities.indexOf(photoobject.getPhotoActivity()));
        }

        textView1.setText(photoobject.getPhotoDate());
        textView2.setText(photoobject.getPhotoDescription());
        imageView.setImageBitmap(photoshown);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview2:
                if(getActivity().getCurrentFocus()!=null){
                    getActivity().getCurrentFocus().clearFocus();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(getResources().getString(R.string.PhotoDescription));
                LinearLayout ll1 = new LinearLayout(getActivity());
                ll1.setOrientation(LinearLayout.VERTICAL);
                ll1.setPadding(20,0,20,0);
                TextView etxt_blank = new TextView(getActivity());
                etxt_blank.setText("\n");
                final EditText etxt_description = new EditText(getActivity());
                etxt_description.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                if(photoobject.getPhotoDescription()!=null){
                    etxt_description.setText(photoobject.getPhotoDescription());
                }
                ll1.addView(etxt_blank);
                ll1.addView(etxt_description);
                builder.setView(ll1);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s1 = etxt_description.getText().toString();
                        if(!s1.equals("")){
                            photoobject.setPhotoDescription(s1);
                            textView2.setText(s1);
                        }else {
                            etxt_description.setError("Field cannot be blank");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                Dialog dialog = builder.create();
                if(dialog.getWindow()!=null){
                    dialog.getWindow().setSoftInputMode(WindowManager.
                            LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    //etxt_description.hasFocus();
                }
                dialog.show();

        }
    }

    //Guradamos la lista de posibles opciones y la opcion default
    private void ExitMethod() {
        photoobject.setPhotoDescription(textView2.getText().toString());
        photoobject.setPhotoActivity(spinner.getSelectedItem().toString());
        //Intent intent = new Intent();
        //intent.putExtra("photo", (Serializable) photoobject);
        //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getDialog().dismiss();
    }
}
