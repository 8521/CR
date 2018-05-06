package com.ilaquidain.constructionreportersfi.qcreports;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class f00102_QCeditreport extends Fragment {

    private Project_Object currentproject;
    private ArrayList<Report_Object> savedreports;
    private RecyclerView mrecyclerview;
    private adapter_edit_reports madapter;
    private ItemTouchHelper itemtouchhelper31;
    private int mExpandedPosition = -1;
    private LinearLayout currentshownextendview;
    private int projectnumber, reportnumber, reporttypenumber;
    private Saved_Info_Object savedinfo;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;
    private CoordinatorLayout coordinatorLayout;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f0102_qcreportedit,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Select Report");
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        coordinatorLayout = v.findViewById(R.id.coordinatorlayout_1);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reporttypenumber = mpref.getInt("reporttypenumber",-1);

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        if(savedinfo!=null && projectnumber != -1 && reporttypenumber!=-1){
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            savedreports = currentproject.getProjectReports().get(reporttypenumber);
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }

        mrecyclerview = v.findViewById(R.id.recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        madapter = new adapter_edit_reports();
        mrecyclerview.setAdapter(madapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerview.getContext(),
                DividerItemDecoration.VERTICAL);
        mrecyclerview.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback = new HelperCallback31(madapter);
        itemtouchhelper31 = new ItemTouchHelper(callback);
        itemtouchhelper31.attachToRecyclerView(mrecyclerview);

        return v;
    }

    private class adapter_edit_reports extends RecyclerView.Adapter<viewholder_edit_reports> implements HelperAdapter31{
        private final OnStarDragListener31 mdraglistener31;

        public adapter_edit_reports() {
            super();
            mdraglistener31 = new OnStarDragListener31() {
                @Override
                public void onStartDrag31(RecyclerView.ViewHolder viewHolder) {
                    itemtouchhelper31.startDrag(viewHolder);
                }
            };
        }

        @Override
        public viewholder_edit_reports onCreateViewHolder(ViewGroup parent, int viewType) {
            View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_editreport_2,
                    parent,false);
            return new viewholder_edit_reports(v2);
        }

        @Override
        public void onBindViewHolder(viewholder_edit_reports holder, int position) {

            //final int tempos = position;
            //final boolean isExpanded = position==mExpandedPosition;
            //holder.extededview.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            String s1 = "Daily Report Dated: " + savedreports.get(position).getReportinfo().get(8);
            holder.textView1.setText(s1);
            //holder.itemView.setActivated(isExpanded);
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
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
            return savedreports.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(savedreports,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
            currentproject.getProjectReports().set(reporttypenumber,savedreports);
            ((MainActivity)getActivity()).getSaved_info().getSavedProjects().get(projectnumber)
                    .getProjectReports().set(reporttypenumber,savedreports);
            return false;
        }

        @Override
        public void onItemDismiss(final int position) {
            /*final qa_report_obj report = savedreports.get(position);
            Snackbar snackbar = Snackbar
                    .make(mrecyclerview, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            savedreports.add(position,report);
                            notifyItemInserted(position);
                            mrecyclerview.scrollToPosition(position);
                        }
                    });
            snackbar.show();
            savedreports.remove(position);
            currentproject.setQa_reports(savedreports);
            ((MainActivity)getActivity()).getSaved_info().getSavedProjects().get(projectnumber).
                    setQa_reports(savedreports);
            notifyItemRemoved(position);*/
        }
    }
    private class viewholder_edit_reports extends RecyclerView.ViewHolder implements
            HelperViewHolder31, View.OnClickListener {
        final TextView textView1;
        RelativeLayout initialview;
        LinearLayout extededview;
        TextView expanded_viewreport;
        TextView expanded_deletereport;
        TextView expanded_duplicatereport;
        //final TextView textView2;

        public viewholder_edit_reports(View itemView) {
            super(itemView);
            initialview = itemView.findViewById(R.id.initialview);
            extededview = itemView.findViewById(R.id.extended_view);

            initialview.setOnClickListener(this);

            textView1 = itemView.findViewById(R.id.text1);

            expanded_viewreport = itemView.findViewById(R.id.expanded_viewreport);
            expanded_deletereport = itemView.findViewById(R.id.expanded_deletereport);
            expanded_duplicatereport = itemView.findViewById(R.id.expanded_duplicate);
            expanded_viewreport.setOnClickListener(this);
            expanded_deletereport.setOnClickListener(this);
            expanded_duplicatereport.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.initialview:
                    TransitionManager.beginDelayedTransition(coordinatorLayout);
                    if(!extededview.isShown()){
                        if(mExpandedPosition!=-1){
                            currentshownextendview.setVisibility(View.GONE);
                        }
                        extededview.setVisibility(View.VISIBLE);
                        mExpandedPosition = getAdapterPosition();
                        currentshownextendview = extededview;
                    }
                    break;
                case R.id.expanded_viewreport:
                    FragmentManager fm = getFragmentManager();
                    f00101_QCnewreport fgmt = new f00101_QCnewreport();
                    mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    mprefedit = mpref.edit();
                    mprefedit.putInt("reportnumber",getAdapterPosition());
                    mprefedit.apply();
                    fm.beginTransaction()
                            .add(R.id.MainFrame,fgmt,getResources().getString(R.string.level101))
                            .addToBackStack(getResources().getString(R.string.level101))
                            .commit();
                    break;
                case R.id.expanded_deletereport:
                    final Report_Object report = savedreports.get(getAdapterPosition());
                    Snackbar snackbar = Snackbar
                            .make(mrecyclerview, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    savedreports.add(getAdapterPosition(),report);
                                    madapter.notifyItemInserted(getAdapterPosition());
                                    mrecyclerview.scrollToPosition(getAdapterPosition());
                                }
                            });
                    snackbar.show();
                    savedreports.remove(getAdapterPosition());
                    currentproject.getProjectReports().set(reporttypenumber,savedreports);
                    ((MainActivity)getActivity()).getSaved_info().getSavedProjects().get(projectnumber).
                            getProjectReports().set(reporttypenumber,savedreports);
                    madapter.notifyItemRemoved(getAdapterPosition());
                    break;
                case R.id.expanded_duplicate:
                    AlertDialog.Builder buider = new AlertDialog.Builder(getActivity());
                    buider.setMessage("Do you want to duplicate the report?");
                    buider.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Report_Object duplicated_report;
                            try{
                                duplicated_report = currentproject.getProjectReports()
                                        .get(reporttypenumber).get(getAdapterPosition()).deepClone();
                                String currentdate = new SimpleDateFormat("MM/dd/yy",Locale.US).format(new Date());
                                duplicated_report.getReportinfo().set(8,currentdate);
                                String id = UUID.randomUUID().toString();
                                duplicated_report.setReportid(id);
                                savedreports.add(duplicated_report);
                                currentproject.getProjectReports().set(reporttypenumber,savedreports);
                                savedinfo.getSavedProjects().set(projectnumber,currentproject);
                                ((MainActivity)getActivity()).setSaved_info(savedinfo);
                                madapter.notifyDataSetChanged();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Snackbar.make(getActivity().getWindow().getDecorView().getRootView().
                                                findViewById(R.id.coordinatorlayout_1),
                                        "Error Duplicating Report",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                    buider.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    buider.show();
                    break;
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(getActivity(),
                    R.color.colorSecondaryLight));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    private class HelperCallback31 extends ItemTouchHelper.Callback{
        private static final float ALPHA_FULL = 1.0f;

        private final HelperAdapter31 helperadapter;

        private HelperCallback31(HelperAdapter31 mhelperadapter){
            helperadapter = mhelperadapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            // Notify the adapter of the move
            helperadapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //helperadapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Fade out the view as it is swiped out of the parent's bounds
                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof HelperViewHolder31) {
                    HelperViewHolder31 itemViewHolder = (HelperViewHolder31) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(ALPHA_FULL);
            if (viewHolder instanceof HelperViewHolder31) {
                HelperViewHolder31 itemViewHolder = (HelperViewHolder31) viewHolder;
                itemViewHolder.onItemClear();
            }
        }
    }
    private interface HelperAdapter31{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
    private interface HelperViewHolder31{
        void onItemSelected();
        void onItemClear();
    }
    private interface OnStarDragListener31{
        void onStartDrag31(RecyclerView.ViewHolder viewHolder);
    }
}
