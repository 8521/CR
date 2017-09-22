package com.ilaquidain.constructionreporterpro.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilaquidain.constructionreporterpro.R;
import com.ilaquidain.constructionreporterpro.activities.MainActivity;
import com.ilaquidain.constructionreporterpro.object.Contractor_Object;
import com.ilaquidain.constructionreporterpro.object.Project_Object;
import com.ilaquidain.constructionreporterpro.object.Saved_Info_Object;

import java.util.ArrayList;
import java.util.Collections;

public class F_1_1_4_Contractors extends Fragment implements View.OnClickListener {

    private RecyclerView mrecyclerview;
    private adapter_contractor madapter;
    private ItemTouchHelper itemtouchhelpercontractor;
    private ArrayList<Contractor_Object> mcontractorlist = new ArrayList<>();
    private int lightred;
    private SharedPreferences mpref;
    private int projectnumber;
    private Saved_Info_Object savedinfo;
    private Project_Object currentproject;
    private InputMethodManager imm;
    private int mExpandedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contractorlist,container,false);
        lightred = ContextCompat.getColor(getActivity(),R.color.selection_light_orange);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);


        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        if(savedinfo!=null && projectnumber != -1) {
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            mcontractorlist = currentproject.getProjectContractors();
        }else if(savedinfo!=null && projectnumber!= -1){
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            mcontractorlist = currentproject.getProjectContractors();
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }

        FloatingActionButton fab_add_contractor = (FloatingActionButton)
                v.findViewById(R.id.fab_addcontractor);
        fab_add_contractor.setOnClickListener(this);

        mrecyclerview = (RecyclerView)v.findViewById(R.id.recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        madapter = new adapter_contractor();
        mrecyclerview.setAdapter(madapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mrecyclerview.getContext(),
                DividerItemDecoration.VERTICAL);
        mrecyclerview.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback = new HelperCallbackcontractor(madapter);
        itemtouchhelpercontractor = new ItemTouchHelper(callback);
        itemtouchhelpercontractor.attachToRecyclerView(mrecyclerview);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_addcontractor:
                dialog_contractor(null,-1);
                break;
        }
    }

    private class adapter_contractor extends RecyclerView.Adapter<viewholder_contractor>
            implements HelperAdaptercontractor{
        private final OnStarDragListenercontractor mdraglistenercontractor;

        private adapter_contractor() {
            super();

            mdraglistenercontractor = new OnStarDragListenercontractor() {
                @Override
                public void onStartDragcontractor(RecyclerView.ViewHolder viewHolder) {
                    itemtouchhelpercontractor.startDrag(viewHolder);
                }
            };
        }

        @Override
        public viewholder_contractor onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_contractor,parent,false);
            return new viewholder_contractor(v);
        }

        @Override
        public void onBindViewHolder(viewholder_contractor holder, int position) {
            if(mcontractorlist.get(position).getContractor_name()!=null){
            holder.txt_contractorname.setText(mcontractorlist.get(position).getContractor_name());}
            final int tempos = position;
            final boolean isExpanded = position==mExpandedPosition;
            holder.expanded_contractorview.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            if(mcontractorlist.get(position).getContractor_address()!=null){
                holder.txt_contractor_address.setText(mcontractorlist.get(position)
                        .getContractor_address());
            }
            if(mcontractorlist.get(position).getContractor_phone()!=null){
                holder.txt_contractor_number.setText(mcontractorlist.get(position)
                .getContractor_phone());
            }
            holder.itemView.setActivated(isExpanded);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:tempos;
                    //TransitionManager.beginDelayedTransition(mrecyclerview);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mcontractorlist.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(mcontractorlist,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(final int position) {
        }
    }
    private class viewholder_contractor extends RecyclerView.ViewHolder
            implements View.OnClickListener,HelperViewHoldercontractor{
        LinearLayout expanded_contractorview;
        TextView txt_contractorname;
        TextView txt_contractor_address;
        TextView txt_contractor_number;
        TextView txt_contractor_edit;
        TextView txt_contractor_delete;

        private viewholder_contractor(View itemView) {
            super(itemView);
            expanded_contractorview = (LinearLayout)itemView.findViewById(R.id.expanded_contractorview);
            txt_contractorname = (TextView)itemView.findViewById(R.id.txt_contractor_name);
            txt_contractor_address =(TextView)itemView.findViewById(R.id.txt_expanded_contractoraddress);
            txt_contractor_number = (TextView)itemView.findViewById(R.id.txt_expanded_contractornumber);
            txt_contractor_edit = (TextView)itemView.findViewById(R.id.txt_expanded_contractoredit);
            txt_contractor_delete = (TextView)itemView.findViewById(R.id.txt_expanded_contractordelete);

            txt_contractor_edit.setOnClickListener(this);
            txt_contractor_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_expanded_contractoredit:
                    dialog_contractor(mcontractorlist.get(getAdapterPosition()),getAdapterPosition());
                    break;
                case R.id.txt_expanded_contractordelete:
                    final int temp_position = getAdapterPosition();
                    final Contractor_Object cont_obj = mcontractorlist.get(getAdapterPosition());
                    Snackbar snackbar = Snackbar
                            .make(mrecyclerview, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mcontractorlist.add(temp_position,cont_obj);
                                    madapter.notifyItemInserted(temp_position);
                                    mrecyclerview.scrollToPosition(getAdapterPosition());
                                }
                            });
                    snackbar.show();
                    mcontractorlist.remove(getAdapterPosition());
                    madapter.notifyItemRemoved(getAdapterPosition());
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    private void dialog_contractor(final Contractor_Object contractor_object, final int position) {
        //Create Linear layout
        LinearLayout ll_1 = new LinearLayout(getActivity());
        ll_1.setOrientation(LinearLayout.VERTICAL);
        ll_1.setPadding(20,0,20,0);
        TextView txt_blank = new TextView(getActivity());
        txt_blank.setText("\n");
        TextView txt_cname = new TextView(getActivity());
        txt_cname.setText("Contractor Name");
        TextView txt_caddress = new TextView(getActivity());
        txt_caddress.setText("Contractor Address");
        TextView txt_cnumber = new TextView(getActivity());
        txt_cnumber.setText("Contractor Phone");
        final EditText etxt_cname = new EditText(getActivity());
        final EditText etxt_caddress = new EditText(getActivity());
        final EditText etxt_cnumber = new EditText(getActivity());
        if(contractor_object!=null){
            etxt_cname.setText(contractor_object.getContractor_name());
            etxt_caddress.setText(contractor_object.getContractor_address());
            etxt_cnumber.setText(contractor_object.getContractor_phone());
        }
        ll_1.addView(txt_blank);
        ll_1.addView(txt_cname);
        ll_1.addView(etxt_cname);
        ll_1.addView(txt_caddress);
        ll_1.addView(etxt_caddress);
        ll_1.addView(txt_cnumber);
        ll_1.addView(etxt_cnumber);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(ll_1);
        if(contractor_object!=null){
            builder.setTitle("Edit Contractor");
        }else{builder.setTitle("New Contractor");}
        
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(contractor_object!=null){
                    contractor_object.setContractor_name(etxt_cname.getText().toString());
                    contractor_object.setContractor_address(etxt_caddress.getText().toString());
                    contractor_object.setContractor_phone(etxt_cnumber.getText().toString());
                    mcontractorlist.set(position,contractor_object);
                }else{
                    Contractor_Object con_obj = new Contractor_Object();
                    con_obj.setContractor_name(etxt_cname.getText().toString());
                    con_obj.setContractor_address(etxt_caddress.getText().toString());
                    con_obj.setContractor_phone(etxt_cnumber.getText().toString());
                    mcontractorlist.add(con_obj);
                }
                madapter.notifyDataSetChanged();
                currentproject.setProjectContractors(mcontractorlist);
                savedinfo.getSavedProjects().set(projectnumber,currentproject);
                ((MainActivity)getActivity()).setSaved_info(savedinfo);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Dialog dialog_contractor = builder.create();
        if(getActivity().getCurrentFocus()!=null){
            getActivity().getCurrentFocus().clearFocus();
        }
        //etxt_cname.hasWindowFocus();
        dialog_contractor.getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog_contractor.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_contractor.show();

    }

    private class HelperCallbackcontractor extends ItemTouchHelper.Callback{
        private static final float ALPHA_FULL = 1.0f;

        private final HelperAdaptercontractor helperadapter;

        private HelperCallbackcontractor(HelperAdaptercontractor mhelperadapter){
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
            final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
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
                if (viewHolder instanceof HelperViewHoldercontractor) {
                    HelperViewHoldercontractor itemViewHolder =
                            (HelperViewHoldercontractor) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(ALPHA_FULL);
            if (viewHolder instanceof HelperViewHoldercontractor) {
                HelperViewHoldercontractor itemViewHolder =
                        (HelperViewHoldercontractor) viewHolder;
                itemViewHolder.onItemClear();
            }
        }
    }
    private interface HelperAdaptercontractor{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
    private interface HelperViewHoldercontractor{
        void onItemSelected();
        void onItemClear();
    }
    private interface OnStarDragListenercontractor{
        void onStartDragcontractor(RecyclerView.ViewHolder viewHolder);
    }
}