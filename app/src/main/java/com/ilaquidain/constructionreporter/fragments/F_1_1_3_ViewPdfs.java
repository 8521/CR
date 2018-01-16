package com.ilaquidain.constructionreporter.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilaquidain.constructionreporter.R;
import com.ilaquidain.constructionreporter.activities.MainActivity;
import com.ilaquidain.constructionreporter.object.Project_Object;
import com.ilaquidain.constructionreporter.object.Saved_Info_Object;

import java.io.File;
import java.util.ArrayList;

public class F_1_1_3_ViewPdfs extends Fragment{

    private Project_Object currentproject;
    private File FolderPathFile;
    private ArrayList<String> filesPaths = new ArrayList<>();
    private ArrayList<String> filesNames = new ArrayList<>();

    private RecyclerView mrecyclerview;
    private RecyclerView.Adapter madapter;
    private Integer projectnumber;
    private Saved_Info_Object savedinfo;
    private SharedPreferences mpref;
    private int mExpandedPosition = -1;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout currentextededview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpdfs,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Select Report");

        coordinatorLayout = v.findViewById(R.id.coordinatorlayout_2);

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        if(savedinfo!=null && projectnumber !=-1){
        currentproject = savedinfo.getSavedProjects().get(projectnumber);}
        else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
        }

        //currentproject = (Project_Object)getArguments().getSerializable("currentproject");
        //getArguments().remove("currentproject");

        if(currentproject!=null){
            String s1 = currentproject.getProjectName();
            FolderPathFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS)+"/ConstructionReporter",s1);
            ObtainProjectPDFS();
        }
        mrecyclerview = v.findViewById(R.id.recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        madapter = new AdapterPDFS();
        mrecyclerview.setAdapter(madapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerview.getContext(),
                DividerItemDecoration.VERTICAL);
        mrecyclerview.addItemDecoration(dividerItemDecoration);

        return v;
    }

    private void ObtainProjectPDFS() {
        filesPaths = new ArrayList<>();
        filesNames = new ArrayList<>();
        File directory = new File(FolderPathFile.toString());
        File[] pdfs = directory.listFiles();
        if(pdfs!=null) {
            for (File f : pdfs) {
                filesPaths.add(f.toString());
                filesNames.add(f.getName());
            }
        }
    }

    private class AdapterPDFS extends RecyclerView.Adapter<ViewHolderPDFS>{
        private AdapterPDFS() {
            super();
        }

        @Override
        public ViewHolderPDFS onCreateViewHolder(ViewGroup parent, int viewType) {
            View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pdfreport,parent,false);
            return new ViewHolderPDFS(v2);
        }

        @Override
        public void onBindViewHolder(ViewHolderPDFS holder, int position) {
            holder.textview1.setText(filesNames.get(position));
            /*final int tempos = position;
            final boolean isExpanded = position==mExpandedPosition;
            holder.extendedview_pdf.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.textview1.setText(filesNames.get(position));
            holder.itemView.setActivated(isExpanded);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:tempos;
                    //TransitionManager.beginDelayedTransition(mrecyclerview);
                    notifyDataSetChanged();
                }
            });*/

        }

        @Override
        public int getItemCount() {
            return filesNames.size();
        }
    }
    private class ViewHolderPDFS extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView textview1;
        LinearLayout initialview;
        LinearLayout extendedview;
        TextView viewpdf;
        TextView deletepdf;
        //final TextView textview2;
        public ViewHolderPDFS(View itemView) {
            super(itemView);
            initialview = itemView.findViewById(R.id.initialview);
            extendedview = itemView.findViewById(R.id.extendedview);

            initialview.setOnClickListener(this);

            textview1 = itemView.findViewById(R.id.text1);

            viewpdf = itemView.findViewById(R.id.extended_pdf_view);
            deletepdf = itemView.findViewById(R.id.extended_pdf_delete);
            viewpdf.setOnClickListener(this);
            deletepdf.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.initialview:
                    TransitionManager.beginDelayedTransition(coordinatorLayout);

                    if(!extendedview.isShown()){
                        if(mExpandedPosition!=-1){
                            currentextededview.setVisibility(View.GONE);
                        }
                        currentextededview = extendedview;
                        extendedview.setVisibility(View.VISIBLE);
                        mExpandedPosition = getAdapterPosition();
                    }

                    break;

                case R.id.extended_pdf_view:
                    Intent ViewPDFIntent = new Intent(Intent.ACTION_VIEW);
                    ViewPDFIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY);
                    File f = new File(filesPaths.get(getAdapterPosition()));
                    Uri uri = FileProvider.getUriForFile(getActivity(),
                            "com.ilaquidain.constructionreporter.provider",f);
                    ViewPDFIntent.setDataAndType(uri,getActivity().getContentResolver().getType(uri));
                    startActivity(ViewPDFIntent);
                    break;
                case R.id.extended_pdf_delete:
                    AlertDialog.Builder deletedialog = new AlertDialog.Builder(getActivity());
                    deletedialog.setMessage("Are you sure you want to delete this Report?");
                    deletedialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File filetodelete = new File(filesPaths.get(getAdapterPosition()));
                            boolean delete = filetodelete.delete();
                            filesNames.remove(getAdapterPosition());
                            filesPaths.remove(getAdapterPosition());
                            madapter.notifyItemRemoved(getAdapterPosition());
                        }
                    });
                    deletedialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            madapter.notifyDataSetChanged();
                        }
                    });
                    deletedialog.show();
                    break;
            }

        }
    }
}
