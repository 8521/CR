package com.ilaquidain.constructionreportersfi.fragments_newreport;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.object.Contractor_Object;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Report_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import com.ilaquidain.constructionreportersfi.object.Task_Object;
import com.ilaquidain.constructionreportersfi.object.Worker_Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManEquipFragment extends Fragment {

    private final static String level0 = "level0";
    private final static String level1 = "level1";
    private final static String level2 = "level2";
    private final static String manpower = "manpower";
    private final static String equipment = "equipment";
    private final static String title1 = "Manpower Selected";
    private final static String title2 = "Manpower Categories";
    private final static String title3 = "Manpower";
    private final static String title4 = "Equipment Selected";
    private final static String title5 = "Equipment Categories";
    private final static String title6 = "Equipmet";

    private int lightred;

    private RecyclerView mrecyclerview_6;
    private Adapter_5 madapter_6;
    private ItemTouchHelper mhelper_6;
    private TextView titleview;

    private ArrayList<String> grouparray;
    private ArrayList<ArrayList<Worker_Object>> availableitemsarray;
    private String itemstype;
    private String level;

    private FloatingActionButton fabaccept;
    private int categoryselected;

    private Spinner spinner_activity;
    private Spinner spinner_contractor;
    private ArrayAdapter<String> spinneradapterA;
    private ArrayAdapter<String> spinneradapterB;
    private ArrayList<String> spinnerlist_activities;
    private ArrayList<String> spinnerlist_contractors;

    private Project_Object currentproject;
    private Report_Object currentreport;
    private Saved_Info_Object savedinfo;

    private Integer projectnumber, reportnumber, reporttypenumber;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;

    private void ExitMethod() {
        if(reportnumber==-1){
            savedinfo.setTemp_report(currentreport);
        }else {
            savedinfo.getSavedProjects().get(projectnumber).getProjectReports()
                    .get(reporttypenumber).set(reportnumber,currentreport);
        }
        if(itemstype.equals(manpower)){
            currentproject.setListAvailableManpower(availableitemsarray);}
        else if(itemstype.equals(equipment)){
            currentproject.setListAvailableEquipment(availableitemsarray);
        }
        ((MainActivity)getActivity()).setSaved_info(savedinfo);
        if ( getFragmentManager().getBackStackEntryCount() > 0)
        {getFragmentManager().popBackStack();}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f02012_qanewreportactivities,container,false);

        level = level0;

        lightred = ContextCompat.getColor(getActivity(),R.color.selection_light_orange);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        itemstype = mpref.getString("itemstype",manpower);
        titleview = v.findViewById(R.id.recyclerviewtitle);
        if(itemstype.equals(manpower)){
            titleview.setText(title1);
        }else if(itemstype.equals(equipment)){
            titleview.setText(title4);
        }

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        if(savedinfo!=null && projectnumber != -1 && reportnumber!=-1) {
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            currentreport = savedinfo.getSavedProjects().get(projectnumber)
                    .getProjectReports().get(reporttypenumber).get(reportnumber);
        }else if(savedinfo!=null && projectnumber!= -1 &&reportnumber==-1){
            currentproject = savedinfo.getSavedProjects().get(projectnumber);
            currentreport = savedinfo.getTemp_report();
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }

        //currentreport.setSelectedWorkers(new ArrayList<Worker_Object>());
        if(itemstype.equals(manpower)){
            grouparray = new ArrayList<>(currentproject.getWorkerGroups());
            availableitemsarray = new ArrayList<>(currentproject.getListAvailableManpower());
        }else if (itemstype.equals(equipment)){
            grouparray = new ArrayList<>(currentproject.getEquipmentGroups());
            availableitemsarray = new ArrayList<>(currentproject.getListAvailableEquipment());
        }

        FloatingActionButton fabadd = v.findViewById(R.id.fabaddmanpower);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (level){
                    case level0:
                        level = level1;
                        if(itemstype.equals(manpower)) {
                            titleview.setText(title2);
                        }else if(itemstype.equals(equipment)){
                            titleview.setText(title5);
                        }
                        mrecyclerview_6.setAdapter(null);
                        mrecyclerview_6.setAdapter(madapter_6);
                        break;
                    case level1:
                        showdialog1();
                        break;
                    case level2:
                        showdialog2(null,-1,0);
                        break;
                }
            }

        });

        fabaccept = v.findViewById(R.id.fabaccept);
        fabaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (level){
                    case level0:
                        ExitMethod();
                        break;
                    case level1:
                        level = level0;
                        if(itemstype.equals(manpower)){titleview.setText(title1);}
                        else if(itemstype.equals(equipment)){titleview.setText(title4);}
                        mrecyclerview_6.setAdapter(null);
                        mrecyclerview_6.setAdapter(madapter_6);
                        break;
                    case level2:
                        level = level1;
                        if(itemstype.equals(manpower)) {
                            titleview.setText(title2);
                        }else if(itemstype.equals(equipment)){
                            titleview.setText(title5);
                        }
                        mrecyclerview_6.setAdapter(null);
                        mrecyclerview_6.setAdapter(madapter_6);
                }
            }
        });

        madapter_6 = new Adapter_5(getActivity());
        mrecyclerview_6 = v.findViewById(R.id.recyclerview_manpower);
        mrecyclerview_6.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrecyclerview_6.setAdapter(madapter_6);

        ItemTouchHelper.Callback callback_6 = new Callback_5(madapter_6);
        mhelper_6 = new ItemTouchHelper(callback_6);
        mhelper_6.attachToRecyclerView(mrecyclerview_6);

        return v;
    }

    private class Adapter_5 extends RecyclerView.Adapter<ViewHolder_5> implements Interface_Adapter_5{

        private final Drag_Listener_5 mdraglistener;
        private int mCurrentType;

        private Adapter_5(Context context) {
            mdraglistener = new Drag_Listener_5() {
                @Override
                public void OnStartDrag_5(RecyclerView.ViewHolder viewHolder) {
                    mhelper_6.startDrag(viewHolder);
                }
            };
        }

        @Override
        public ViewHolder_5 onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (level){
                case level0:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.rvitem_05_selectedworker,parent,false));
                case level1:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.rvitem_01_manpowergroup,parent,false));
                case level2:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.rvitem_06_workerobject,parent,false));
                default:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            android.R.layout.simple_list_item_1,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder_5 holder, int position) {
            switch (level){
                case level0:
                    holder.itemView.setBackgroundColor(0);
                    List<String> tem = Arrays.asList(getResources().getStringArray(R.array.hours));
                    if(itemstype.equals(manpower)) {
                        holder.textView1.setText(currentreport.getSelectedlabor().get(position).getFirstName());
                        holder.textView2.setText(currentreport.getSelectedlabor().get(position).getActivity());
                        holder.textView3.setText(currentreport.getSelectedlabor().get(position).getCompany());
                        holder.hourspinner.setSelection(tem.indexOf(currentreport.
                                getSelectedlabor().get(position).getRegHours()));
                    }else if(itemstype.equals(equipment)){
                        holder.textView1.setText(currentreport.getSelectedequip().get(position).getFirstName());
                        holder.textView2.setText(currentreport.getSelectedequip().get(position).getActivity());
                        holder.textView3.setText(currentreport.getSelectedequip().get(position).getCompany());
                        holder.hourspinner.setSelection(tem.indexOf(currentreport.
                                getSelectedequip().get(position).getRegHours()));
                    }
                    break;
                case level1:
                    holder.itemView.setBackgroundColor(0);
                    holder.textView1.setText(grouparray.get(position));
                    break;
                case level2:
                    holder.itemView.setBackgroundColor(0);
                    holder.textView1.setText(availableitemsarray.get(categoryselected).get(position).getFirstName());
                    if(itemstype.equals(manpower)) {
                        for (int j = 0; j < currentreport.getSelectedlabor().size(); j++) {
                            if (availableitemsarray.get(categoryselected).get(position).getIdNumber().equals(
                                    currentreport.getSelectedlabor().get(j).getIdNumber())) {
                                holder.itemView.setBackgroundColor(lightred);
                            }
                        }
                    }else if(itemstype.equals(equipment)){
                        for (int j = 0; j < currentreport.getSelectedequip().size(); j++) {
                            if (availableitemsarray.get(categoryselected).get(position).getIdNumber().equals(
                                    currentreport.getSelectedequip().get(j).getIdNumber())) {
                                holder.itemView.setBackgroundColor(lightred);
                            }
                        }
                    }
                    holder.textView2.setText(availableitemsarray.get(categoryselected).get(position).getActivity());
                    holder.textView3.setText(availableitemsarray.get(categoryselected).get(position).getCompany());
                    break;
            }

            holder.textView1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mdraglistener.OnStartDrag_5(holder);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            switch (level){
                case level0:
                    if(itemstype.equals(manpower)){
                        return currentreport.getSelectedlabor().size();
                    }else if(itemstype.equals(equipment)){
                        return currentreport.getSelectedequip().size();
                    }
                case level1:
                    return grouparray.size();
                case level2:
                    return availableitemsarray.get(categoryselected).size();
                default:
                    return 0;
            }
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            switch (level){
                case level0:
                    if(itemstype.equals(manpower)) {
                        Collections.swap(currentreport.getSelectedlabor(), fromPosition, toPosition);
                    }else if(itemstype.equals(equipment)){
                        Collections.swap(currentreport.getSelectedequip(),fromPosition,toPosition);
                    }
                    break;
                case level1:
                    Collections.swap(grouparray,fromPosition,toPosition);
                    Collections.swap(availableitemsarray,fromPosition,toPosition);
                    break;
                case level2:
                    Collections.swap(availableitemsarray.get(categoryselected),fromPosition,toPosition);
                    break;
            }
            notifyItemMoved(fromPosition,toPosition);
            return false;
        }

        @Override
        public void onItemDismiss(final int position) {
            switch (level){
                case level0:
                    if(itemstype.equals(manpower)){
                        final Worker_Object worker_object = currentreport.getSelectedlabor().get(position);
                        Snackbar snackbar = Snackbar
                                .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentreport.getSelectedlabor().add(position,worker_object);
                                        notifyItemInserted(position);
                                        mrecyclerview_6.scrollToPosition(position);
                                    }
                                });
                        snackbar.show();
                        currentreport.getSelectedlabor().remove(position);
                        break;
                    }else if(itemstype.equals(equipment)){
                        final Worker_Object worker_object = currentreport.getSelectedequip().get(position);
                        Snackbar snackbar = Snackbar
                                .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentreport.getSelectedequip().add(position,worker_object);
                                        notifyItemInserted(position);
                                        mrecyclerview_6.scrollToPosition(position);
                                    }
                                });
                        snackbar.show();
                        currentreport.getSelectedequip().remove(position);
                        break;
                    }
                case level1:
                    final String group2 = grouparray.get(position);
                    final ArrayList<Worker_Object> listaworkers = availableitemsarray.get(position);
                    Snackbar snackbar2 = Snackbar
                            .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    grouparray.add(position,group2);
                                    availableitemsarray.add(position,listaworkers);
                                    notifyItemInserted(position);
                                    mrecyclerview_6.scrollToPosition(position);
                                }
                            });
                    snackbar2.show();
                    grouparray.remove(position);
                    availableitemsarray.remove(position);
                    if(itemstype.equals(manpower)){
                        currentproject.setWorkerGroups(grouparray);
                        currentproject.setListAvailableManpower(availableitemsarray);
                    }else if(itemstype.equals(equipment)){
                        currentproject.setEquipmentGroups(grouparray);
                        currentproject.setListAvailableEquipment(availableitemsarray);
                    }
                    ((MainActivity)getActivity()).setSaved_info(savedinfo);
                    madapter_6.notifyDataSetChanged();
                    break;
                case level2:
                    final Worker_Object worker_object2 = availableitemsarray.get(categoryselected).get(position);
                    Snackbar snackbar3 = Snackbar
                            .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    availableitemsarray.get(categoryselected).add(position,worker_object2);
                                    notifyItemInserted(position);
                                    mrecyclerview_6.scrollToPosition(position);
                                }
                            });
                    snackbar3.show();
                    availableitemsarray.get(categoryselected).remove(position);
                    if(itemstype.equals(manpower)){
                        currentproject.setListAvailableManpower(availableitemsarray);
                    }else if(itemstype.equals(equipment)){
                        currentproject.setListAvailableEquipment(availableitemsarray);
                    }
                    ((MainActivity)getActivity()).setSaved_info(savedinfo);
                    madapter_6.notifyDataSetChanged();
                    break;
            }
            notifyItemRemoved(position);
        }
    }

    private class ViewHolder_5 extends RecyclerView.ViewHolder implements Interface_ViewHolder_5,
            View.OnClickListener{
        final TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageButton editbutton;
        Spinner hourspinner;

        private ViewHolder_5(View itemView) {
            super(itemView);
            switch (level){
                case level0:
                    textView1 = itemView.findViewById(R.id.text1);
                    textView2 = itemView.findViewById(R.id.text2);
                    textView3 = itemView.findViewById(R.id.text3);
                    hourspinner = itemView.findViewById(R.id.hourspinner);
                    hourspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(itemstype.equals(manpower)) {
                                currentreport.getSelectedlabor().get(getAdapterPosition()).setRegHours(
                                        Double.parseDouble(hourspinner.getSelectedItem().toString()));
                            }else if(itemstype.equals(equipment)){
                                currentreport.getSelectedequip().get(getAdapterPosition()).setRegHours(
                                        Double.parseDouble(hourspinner.getSelectedItem().toString()));
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    break;
                case level1:
                    textView1 = itemView.findViewById(R.id.text1);
                    break;
                case level2:
                    textView1 = itemView.findViewById(R.id.text1);
                    textView2 = itemView.findViewById(R.id.text2);
                    textView3 = itemView.findViewById(R.id.text3);
                    editbutton = itemView.findViewById(R.id.edit);
                    editbutton.setOnClickListener(this);
                    break;
                default:
                    textView1 = itemView.findViewById(android.R.id.text1);
                    break;
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==itemView.getId()) {
                switch (level) {
                    case level0:
                        if(itemstype.equals(manpower)){
                            Worker_Object workera = currentreport.getSelectedlabor().get(getAdapterPosition());
                            showdialog2(workera,getAdapterPosition(),1);
                        }else if (itemstype.equals(equipment)){
                            Worker_Object workera = currentreport.getSelectedequip().get(getAdapterPosition());
                            showdialog2(workera,getAdapterPosition(),1);
                        }
                        break;
                    case level1:
                        categoryselected = getAdapterPosition();
                        if (availableitemsarray.size() <= categoryselected) {
                            for (int i = availableitemsarray.size(); i <= categoryselected; i++) {
                                availableitemsarray.add(new ArrayList<Worker_Object>());
                            }
                        }
                        level = level2;
                        if(itemstype.equals(manpower)) {
                            titleview.setText(title3 + " > " + grouparray.get(categoryselected));
                        }else if(itemstype.equals(equipment)){
                            titleview.setText(title6 + " > " + grouparray.get(categoryselected));
                        }
                        mrecyclerview_6.setAdapter(null);
                        mrecyclerview_6.setAdapter(madapter_6);
                        break;
                    case level2:
                        Boolean boo = true;
                        Worker_Object worker2 = availableitemsarray.get(categoryselected)
                                .get(getAdapterPosition()).deepClone();
                        if(itemstype.equals(manpower)) {
                            for (int z = 0; z < currentreport.getSelectedlabor().size(); z++) {
                                if (availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber().equals(
                                        currentreport.getSelectedlabor().get(z).getIdNumber())) {
                                    boo = false;
                                }
                            }
                        }else if(itemstype.equals(equipment)){
                            for (int z = 0; z < currentreport.getSelectedequip().size(); z++) {
                                if (availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber().equals(
                                        currentreport.getSelectedequip().get(z).getIdNumber())) {
                                    boo = false;
                                }
                            }
                        }
                        if (boo) { //the worker is not included in the list
                            if(itemstype.equals(manpower)){
                                currentreport.getSelectedlabor().add(worker2);
                            }else if(itemstype.equals(equipment)){
                                currentreport.getSelectedequip().add(worker2);
                            }
                            itemView.setBackgroundColor(lightred);
                        } else { //the worker was already included in the list
                            if(itemstype.equals(manpower)) {
                                for (int i = 0; i < currentreport.getSelectedlabor().size(); i++) {
                                    if (currentreport.getSelectedlabor().get(i).getIdNumber().equals(
                                            availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber())) {
                                        currentreport.getSelectedlabor().remove(i);
                                    }
                                }
                            }else if(itemstype.equals(equipment)){
                                for (int i = 0; i < currentreport.getSelectedequip().size(); i++) {
                                    if (currentreport.getSelectedequip().get(i).getIdNumber().equals(
                                            availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber())) {
                                        currentreport.getSelectedequip().remove(i);
                                    }
                                }
                            }
                            itemView.setBackgroundColor(0);
                        }
                }
            }else if(v.getId()==R.id.edit){
                showdialog2(availableitemsarray.get(categoryselected).get(getAdapterPosition()),
                        getAdapterPosition(),0);
            }
        }

        @Override
        public void onItemSelected() {itemView.setBackgroundColor(Color.LTGRAY);}
        @Override
        public void onItemClear() {itemView.setBackgroundColor(0);}
    }

    private class Callback_5 extends ItemTouchHelper.Callback{
        private static final float ALPHA_FULL = 1.0f;
        private final Adapter_5 madapter_5;

        private Callback_5(Adapter_5 adapter) {madapter_5 = adapter;}
        @Override
        public boolean isLongPressDragEnabled() {return true;}
        @Override
        public boolean isItemViewSwipeEnabled() {return true;}
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if(viewHolder.getItemViewType()!=target.getItemViewType()){return false;}
            madapter_5.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return false;
        }
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            madapter_5.onItemDismiss(viewHolder.getAdapterPosition());
        }
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE ){
                if(viewHolder instanceof ViewHolder_5){
                    ViewHolder_5 v5 = (ViewHolder_5) viewHolder;
                    v5.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(ALPHA_FULL);
            if(viewHolder instanceof ViewHolder_5){
                ViewHolder_5 v5 = (ViewHolder_5) viewHolder;
                v5.onItemClear();
            }
        }
    }
    private interface Interface_Adapter_5{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
    private interface Interface_ViewHolder_5{
        void onItemSelected();
        void onItemClear();
    }
    private interface Drag_Listener_5{
        void OnStartDrag_5(RecyclerView.ViewHolder viewHolder);
    }

    //dialog to add category
    private void showdialog1() {
        if(getActivity().getCurrentFocus()!=null){
            getActivity().getCurrentFocus().clearFocus();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText etxt_category = new EditText(getActivity());
        etxt_category.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(etxt_category);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!etxt_category.getText().toString().equals("")){
                    if(level.equals(level1)){
                        grouparray.add(etxt_category.getText().toString());
                        availableitemsarray.add(new ArrayList<Worker_Object>());
                        if(itemstype.equals(manpower)) {
                            currentproject.setWorkerGroups(grouparray);
                        }else if(itemstype.equals(equipment)){
                            currentproject.setEquipmentGroups(grouparray);
                        }
                        madapter_6.notifyDataSetChanged();
                    }else {
                        Worker_Object worker = new Worker_Object();
                        worker.setFirstName(etxt_category.getText().toString());
                        availableitemsarray.get(categoryselected).add(worker);
                        madapter_6.notifyDataSetChanged();
                    }
                }else {
                    etxt_category.setError("Field cannot be blank");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog_newcategory = builder.create();
        if(itemstype.equals(manpower)){
            dialog_newcategory.setTitle("Add Manpower Category");
        }else if(itemstype.equals(equipment)){
            dialog_newcategory.setTitle("Add Equipment Category");
        }

        if(dialog_newcategory.getWindow()!=null){
            dialog_newcategory.getWindow().setSoftInputMode
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog_newcategory.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            //etxt_category.hasFocus();
        }
        dialog_newcategory.show();
    }
    //dialog to add worker
    private void showdialog2(final Worker_Object worker, final int position, final int caso){
        if(getActivity().getCurrentFocus()!=null){
            getActivity().getCurrentFocus().clearFocus();
        }
        AlertDialog.Builder builder_2 = new AlertDialog.Builder(getActivity());

        if(worker!=null){
            builder_2.setTitle("Edit Element");
        }else {
            builder_2.setTitle("New Element");
        }

        LinearLayout ll_2 = new LinearLayout(getActivity());
        ll_2.setOrientation(LinearLayout.VERTICAL);
        ll_2.setPadding(20,0,20,0);
        TextView txt_blank = new TextView(getActivity());
        txt_blank.setText("\n");
        TextView txt_name = new TextView(getActivity());
        txt_name.setText("Name");
        final EditText etxt_name = new EditText(getActivity());
        TextView txt_activty = new TextView(getActivity());
        txt_activty.setText("Activity");
        TextView txt_contractor = new TextView(getActivity());
        txt_contractor.setText("Contractor");
        spinner_activity = new Spinner(getActivity());
        spinner_contractor = new Spinner(getActivity());
        spinner_activity.setPadding(5,5,5,5);
        spinner_contractor.setPadding(5,5,5,5);
        spinner_activity.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorSecondaryLight));
        spinner_contractor.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorSecondaryLight));
        ImageButton btn_add_activity = new ImageButton(getActivity());
        ImageButton btn_add_contractor = new ImageButton(getActivity());
        btn_add_activity.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_add_black_24dp));
        btn_add_activity.setBackgroundColor(Color.TRANSPARENT);
        btn_add_contractor.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_add_black_24dp));
        btn_add_contractor.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout ll_3 = new LinearLayout(getActivity());
        ll_3.setOrientation(LinearLayout.HORIZONTAL);
        ll_3.setWeightSum(10);
        LinearLayout.LayoutParams lp_3 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,2);
        LinearLayout.LayoutParams lp_4 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,8);
        spinner_activity.setLayoutParams(lp_3);
        btn_add_activity.setLayoutParams(lp_4);
        ll_3.addView(spinner_activity);
        ll_3.addView(btn_add_activity);
        LinearLayout ll_4 = new LinearLayout(getActivity());
        ll_4.setOrientation(LinearLayout.HORIZONTAL);
        ll_4.setWeightSum(10f);
        spinner_contractor.setLayoutParams(lp_3);
        btn_add_contractor.setLayoutParams(lp_4);
        ll_4.addView(spinner_contractor);
        ll_4.addView(btn_add_contractor);

        ll_2.addView(txt_blank);
        ll_2.addView(txt_name);
        ll_2.addView(etxt_name);
        ll_2.addView(txt_activty);
        ll_2.addView(ll_3);
        ll_2.addView(txt_contractor);
        ll_2.addView(ll_4);


        if(worker!=null){
            etxt_name.setText(worker.getFirstName());
        }

        spinnerlist_activities = new ArrayList<>();
        for(int i =0;i<currentproject.getListAvailableTasks().size();i++){
            spinnerlist_activities.add(currentproject.getListAvailableTasks().get(i).getTaskName());
        }
        spinnerlist_contractors = new ArrayList<>();
        for(int i=0;i<currentproject.getProjectContractors().size();i++){
            spinnerlist_contractors.add(currentproject.getProjectContractors().get(i).getContractor_name());
        }

        btn_add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_activity_contractor("activity");
            }
        });
        btn_add_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_activity_contractor("contractor");
            }
        });

        if(!spinnerlist_activities.contains("N/A")){spinnerlist_activities.add(0,"N/A");}
        spinneradapterA = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,spinnerlist_activities);
        spinneradapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity.setAdapter(spinneradapterA);
        if(worker!=null && spinnerlist_activities.contains(worker.getActivity())){
            spinner_activity.setSelection(spinnerlist_activities.indexOf(worker.getActivity()));
        }else {
            spinner_activity.setSelection(spinnerlist_activities.indexOf(savedinfo.getListasOpciones().get(0).get(9)));
        }
        spinner_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savedinfo.getListasOpciones().get(0).set(9,spinnerlist_activities.get(position));
                spinner_activity.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!spinnerlist_contractors.contains("N/A")){spinnerlist_contractors.add(0,"N/A");}
        spinneradapterB = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,spinnerlist_contractors);
        spinneradapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_contractor.setAdapter(spinneradapterB);
        if(worker!=null && spinnerlist_contractors.contains(worker.getCompany())){
            spinner_contractor.setSelection(spinnerlist_contractors.indexOf(worker.getCompany()));
        }else {
            spinner_contractor.setSelection(spinnerlist_contractors.indexOf(savedinfo.getListasOpciones().get(0).get(4)));
        }
        spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savedinfo.getListasOpciones().get(0).set(4,spinnerlist_contractors.get(position));
                spinner_contractor.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder_2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Worker_Object worker2;
                if(worker==null){
                    worker2 = new Worker_Object();}
                else {worker2 = worker;}
                worker2.setFirstName(etxt_name.getText().toString());
                worker2.setActivity(spinner_activity.getSelectedItem().toString());
                worker2.setCompany(spinner_contractor.getSelectedItem().toString());
                worker2.setRegHours(8.00);
                if(position==-1 && caso ==0){//añadir nuevo
                    //si position = -1 estamos anadiendo un nuevo trabajador
                    availableitemsarray.get(categoryselected).add(worker2);}
                else if(position!=-1 && caso==1){//editar de la lista de selected
                    //si position es diferente de -1 estamos editando uno de la lista y caso = 1
                    //hemos selectionado uno de selected reports
                    if(itemstype.equals(manpower)){
                        currentreport.getSelectedlabor().set(position,worker);
                    }else if (itemstype.equals(equipment)){
                        currentreport.getSelectedequip().set(position,worker);
                    }
                }else{ //editar de la la lista de available
                    if(itemstype.equals(manpower)) {
                        for (int i = 0; i < currentreport.getSelectedlabor().size(); i++) {
                            if (currentreport.getSelectedlabor().get(i).getIdNumber()
                                    .equals(worker.getIdNumber())) {
                                currentreport.getSelectedlabor().set(i, worker);
                            }
                        }
                    }else if (itemstype.equals(equipment)){
                        for (int i = 0; i < currentreport.getSelectedequip().size(); i++) {
                            if (currentreport.getSelectedequip().get(i).getIdNumber()
                                    .equals(worker.getIdNumber())) {
                                currentreport.getSelectedequip().set(i, worker);
                            }
                        }
                    }
                    availableitemsarray.get(categoryselected).set(position,worker2);
                }
                madapter_6.notifyDataSetChanged();
            }
        });

        builder_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder_2.setView(ll_2);
        Dialog dialog_edit = builder_2.create();

        if(dialog_edit.getWindow()!=null){
            dialog_edit.getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog_edit.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            //etxt_name.hasFocus();
        }
        dialog_edit.show();

    }
    private void add_activity_contractor(final String activity) {
        if(getActivity().getCurrentFocus()!=null){
            getActivity().getCurrentFocus().clearFocus();
        }
        AlertDialog.Builder AddItemBuilder = new AlertDialog.Builder(getActivity());

        LinearLayout ll_5 = new LinearLayout(getActivity());
        ll_5.setOrientation(LinearLayout.VERTICAL);
        TextView txt_blank = new TextView(getActivity());
        final EditText etxt_add_actcon = new EditText(getActivity());

        ll_5.addView(txt_blank);
        ll_5.addView(etxt_add_actcon);
        AddItemBuilder.setView(ll_5);

        if(activity.equals("activity")){
            AddItemBuilder.setTitle("New Activity");
        }else if (activity.equals("contractor")){
            AddItemBuilder.setTitle("New Contractor");
        }

        AddItemBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etxt_add_actcon.getText().toString().equals("")){
                    etxt_add_actcon.setError("Field cannot be blank");
                }else{
                    switch (activity){
                        case "activity":

                            spinnerlist_activities.add(etxt_add_actcon.getText().toString());
                            spinner_activity.setSelection(spinnerlist_activities.size()-1);
                            Task_Object temptask = new Task_Object();
                            temptask.setTaskName(etxt_add_actcon.getText().toString());
                            currentproject.getListAvailableTasks().add(temptask);
                            savedinfo.getListasOpciones().get(9).add(etxt_add_actcon.getText().toString());
                            spinneradapterA.notifyDataSetChanged();
                            break;
                        case "contractor":
                            spinnerlist_contractors.add(etxt_add_actcon.getText().toString());
                            spinner_contractor.setSelection(spinnerlist_contractors.size()-1);
                            Contractor_Object cont_obje = new Contractor_Object();
                            cont_obje.setContractor_name(etxt_add_actcon.getText().toString());
                            currentproject.getProjectContractors().add(cont_obje);
                            savedinfo.getListasOpciones().get(4).add(etxt_add_actcon.getText().toString());
                            spinneradapterB.notifyDataSetChanged();
                            break;
                    }
                    dialog.dismiss();
                }
            }
        });

        AddItemBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog_add_actobje = AddItemBuilder.create();

        if(dialog_add_actobje.getWindow()!=null){
            dialog_add_actobje.getWindow().setSoftInputMode(WindowManager.
            LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog_add_actobje.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            //etxt_add_actcon.hasFocus();
        }
        dialog_add_actobje.show();

    }


}
