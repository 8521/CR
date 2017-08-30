package com.ilaquidain.constructionreporter.fragments_newreport;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ilaquidain.constructionreporter.R;
import com.ilaquidain.constructionreporter.activities.MainActivity;
import com.ilaquidain.constructionreporter.object.Report_Object;
import com.ilaquidain.constructionreporter.object.Saved_Info_Object;
import com.ilaquidain.constructionreporter.object.Worker_Object;

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

    private Spinner spinnerA; //Activities
    private Spinner spinnerB; //Contractors
    private ArrayAdapter<String> spinneradapterA;
    private ArrayAdapter<String> spinneradapterB;
    private ArrayList<String> spinnerlist_activities;
    private ArrayList<String> spinnerlist_contractors;

    private Report_Object currentreport;
    private Saved_Info_Object savedinfo;

    private Integer projectnumber, reportnumber;
    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;

    private InputMethodManager imm;

    private void ExitMethod() {
        if(reportnumber==-1){
            savedinfo.setTempreport(currentreport);
        }else {
            savedinfo.getSavedProjects().get(projectnumber).getProjectReports().set(reportnumber,currentreport);
        }
        if(itemstype.equals(manpower)){
            savedinfo.setListAvailableManpower(availableitemsarray);}
        else if(itemstype.equals(equipment)){
            savedinfo.setListAvailableEquipment(availableitemsarray);
        }
        ((MainActivity)getActivity()).setSaved_info(savedinfo);
        if ( getFragmentManager().getBackStackEntryCount() > 0)
        {getFragmentManager().popBackStack();}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialogfragment_manpower,container,false);
        level = level0;

        lightred = ContextCompat.getColor(getActivity(),R.color.selection_light_orange);

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);
        projectnumber = mpref.getInt("projectnumber",-1);
        reportnumber = mpref.getInt("reportnumber",-1);
        itemstype = mpref.getString("itemstype",manpower);
        titleview = (TextView)v.findViewById(R.id.recyclerviewtitle);
        if(itemstype.equals(manpower)){
            titleview.setText(title1);
        }else if(itemstype.equals(equipment)){
            titleview.setText(title4);
        }

        savedinfo = ((MainActivity)getActivity()).getSaved_info();
        if(savedinfo!=null && projectnumber != -1 && reportnumber!=-1) {
            currentreport = savedinfo.getSavedProjects().get(projectnumber).getProjectReports().get(reportnumber);
        }else if(savedinfo!=null && reportnumber==-1){
            currentreport = savedinfo.getTempreport();
        }else{
            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {getFragmentManager().popBackStack();}
        }

        //currentreport.setSelectedWorkers(new ArrayList<Worker_Object>());
        if(itemstype.equals(manpower)){
            grouparray = new ArrayList<>(savedinfo.getWorkerGroups());
            availableitemsarray = new ArrayList<>(savedinfo.getListAvailableManpower());
        }else if (itemstype.equals(equipment)){
            grouparray = new ArrayList<>(savedinfo.getEquipmentGroups());
            availableitemsarray = new ArrayList<>(savedinfo.getListAvailableEquipment());
        }

        FloatingActionButton fabadd = (FloatingActionButton)v.findViewById(R.id.fabaddmanpower);
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

        fabaccept = (FloatingActionButton)v.findViewById(R.id.fabaccept);
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
        mrecyclerview_6 = (RecyclerView)v.findViewById(R.id.recyclerview_manpower);
        mrecyclerview_6.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrecyclerview_6.setAdapter(madapter_6);

        ItemTouchHelper.Callback callback_6 = new Callback_5(madapter_6);
        mhelper_6 = new ItemTouchHelper(callback_6);
        mhelper_6.attachToRecyclerView(mrecyclerview_6);

        imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

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
                            R.layout.recyclerviewitem_selectedworker,parent,false));
                case level1:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerviewitem_manpowergroup,parent,false));
                case level2:
                    return new ViewHolder_5(LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerviewitem_workerobject,parent,false));
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
                        holder.textView1.setText(currentreport.getSelectedWorkers().get(position).getName());
                        holder.textView2.setText(currentreport.getSelectedWorkers().get(position).getActivity());
                        holder.textView3.setText(currentreport.getSelectedWorkers().get(position).getCompany());
                        holder.hourspinner.setSelection(tem.indexOf(currentreport.
                                getSelectedWorkers().get(position).getHours()));
                    }else if(itemstype.equals(equipment)){
                        holder.textView1.setText(currentreport.getSelectedEquipment().get(position).getName());
                        holder.textView2.setText(currentreport.getSelectedEquipment().get(position).getActivity());
                        holder.textView3.setText(currentreport.getSelectedEquipment().get(position).getCompany());
                        holder.hourspinner.setSelection(tem.indexOf(currentreport.
                                getSelectedEquipment().get(position).getHours()));
                    }
                    break;
                case level1:
                    holder.itemView.setBackgroundColor(0);
                    holder.textView1.setText(grouparray.get(position));
                    break;
                case level2:
                    holder.itemView.setBackgroundColor(0);
                    holder.textView1.setText(availableitemsarray.get(categoryselected).get(position).getName());
                    if(itemstype.equals(manpower)) {
                        for (int j = 0; j < currentreport.getSelectedWorkers().size(); j++) {
                            if (availableitemsarray.get(categoryselected).get(position).getIdNumber().equals(
                                    currentreport.getSelectedWorkers().get(j).getIdNumber())) {
                                holder.itemView.setBackgroundColor(lightred);
                            }
                        }
                    }else if(itemstype.equals(equipment)){
                        for (int j = 0; j < currentreport.getSelectedEquipment().size(); j++) {
                            if (availableitemsarray.get(categoryselected).get(position).getIdNumber().equals(
                                    currentreport.getSelectedEquipment().get(j).getIdNumber())) {
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
                        return currentreport.getSelectedWorkers().size();
                    }else if(itemstype.equals(equipment)){
                        return currentreport.getSelectedEquipment().size();
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
                        Collections.swap(currentreport.getSelectedWorkers(), fromPosition, toPosition);
                    }else if(itemstype.equals(equipment)){
                        Collections.swap(currentreport.getSelectedEquipment(),fromPosition,toPosition);
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
                        final Worker_Object worker_object = currentreport.getSelectedWorkers().get(position);
                        Snackbar snackbar = Snackbar
                                .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentreport.getSelectedWorkers().add(position,worker_object);
                                        notifyItemInserted(position);
                                        mrecyclerview_6.scrollToPosition(position);
                                    }
                                });
                        snackbar.show();
                        currentreport.getSelectedWorkers().remove(position);
                        break;
                    }else if(itemstype.equals(equipment)){
                        final Worker_Object worker_object = currentreport.getSelectedEquipment().get(position);
                        Snackbar snackbar = Snackbar
                                .make(mrecyclerview_6, "ITEM REMOVED", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentreport.getSelectedEquipment().add(position,worker_object);
                                        notifyItemInserted(position);
                                        mrecyclerview_6.scrollToPosition(position);
                                    }
                                });
                        snackbar.show();
                        currentreport.getSelectedEquipment().remove(position);
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
                        savedinfo.setWorkerGroups(grouparray);
                        savedinfo.setListAvailableManpower(availableitemsarray);
                    }else if(itemstype.equals(equipment)){
                        savedinfo.setEquipmentGroups(grouparray);
                        savedinfo.setListAvailableEquipment(availableitemsarray);
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
                        savedinfo.setListAvailableManpower(availableitemsarray);
                    }else if(itemstype.equals(equipment)){
                        savedinfo.setListAvailableEquipment(availableitemsarray);
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
                    textView1 = (TextView)itemView.findViewById(R.id.text1);
                    textView2 = (TextView)itemView.findViewById(R.id.text2);
                    textView3 = (TextView)itemView.findViewById(R.id.text3);
                    hourspinner = (Spinner)itemView.findViewById(R.id.hourspinner);
                    hourspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(itemstype.equals(manpower)) {
                                currentreport.getSelectedWorkers().get(getAdapterPosition()).setHours(
                                        hourspinner.getSelectedItem().toString());
                            }else if(itemstype.equals(equipment)){
                                currentreport.getSelectedEquipment().get(getAdapterPosition()).setHours(
                                        hourspinner.getSelectedItem().toString());
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    break;
                case level1:
                    textView1 = (TextView)itemView.findViewById(R.id.text1);
                    break;
                case level2:
                    textView1 = (TextView)itemView.findViewById(R.id.text1);
                    textView2 = (TextView)itemView.findViewById(R.id.text2);
                    textView3 = (TextView)itemView.findViewById(R.id.text3);
                    editbutton = (ImageButton) itemView.findViewById(R.id.edit);
                    editbutton.setOnClickListener(this);
                    break;
                default:
                    textView1 = (TextView)itemView.findViewById(android.R.id.text1);
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
                            Worker_Object workera = currentreport.getSelectedWorkers().get(getAdapterPosition());
                            showdialog2(workera,getAdapterPosition(),1);
                        }else if (itemstype.equals(equipment)){
                            Worker_Object workera = currentreport.getSelectedEquipment().get(getAdapterPosition());
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
                            for (int z = 0; z < currentreport.getSelectedWorkers().size(); z++) {
                                if (availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber().equals(
                                        currentreport.getSelectedWorkers().get(z).getIdNumber())) {
                                    boo = false;
                                }
                            }
                        }else if(itemstype.equals(equipment)){
                            for (int z = 0; z < currentreport.getSelectedEquipment().size(); z++) {
                                if (availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber().equals(
                                        currentreport.getSelectedEquipment().get(z).getIdNumber())) {
                                    boo = false;
                                }
                            }
                        }
                        if (boo) { //the worker is not included in the list
                            if(itemstype.equals(manpower)){
                                currentreport.getSelectedWorkers().add(worker2);
                            }else if(itemstype.equals(equipment)){
                                currentreport.getSelectedEquipment().add(worker2);
                            }
                            itemView.setBackgroundColor(lightred);
                        } else { //the worker was already included in the list
                            if(itemstype.equals(manpower)) {
                                for (int i = 0; i < currentreport.getSelectedWorkers().size(); i++) {
                                    if (currentreport.getSelectedWorkers().get(i).getIdNumber().equals(
                                            availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber())) {
                                        currentreport.getSelectedWorkers().remove(i);
                                    }
                                }
                            }else if(itemstype.equals(equipment)){
                                for (int i = 0; i < currentreport.getSelectedEquipment().size(); i++) {
                                    if (currentreport.getSelectedEquipment().get(i).getIdNumber().equals(
                                            availableitemsarray.get(categoryselected).get(getAdapterPosition()).getIdNumber())) {
                                        currentreport.getSelectedEquipment().remove(i);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v5 = inflater.inflate(R.layout.builder_simpleadditem,null);
        builder.setView(v5);

        TextView title = (TextView)v5.findViewById(R.id.builder_title);

        if(level.equals(level1)){title.setText("Add Manpower Category");}
        else{title.setText("Add Manpower");}

        final EditText editText = (EditText)v5.findViewById(R.id.edittext1);
        ImageButton OK = (ImageButton)v5.findViewById(R.id.fabaccept);
        ImageButton CANCEL =(ImageButton)v5.findViewById(R.id.fabcancel);

        final AlertDialog dialog = builder.create();

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    if(level.equals(level1)){
                        grouparray.add(editText.getText().toString());
                        availableitemsarray.add(new ArrayList<Worker_Object>());
                        if(itemstype.equals(manpower)) {
                            savedinfo.setWorkerGroups(grouparray);
                        }else if(itemstype.equals(equipment)){
                            savedinfo.setEquipmentGroups(grouparray);
                        }
                        madapter_6.notifyDataSetChanged();
                    }else {
                        Worker_Object worker = new Worker_Object();
                        worker.setName(editText.getText().toString());
                        availableitemsarray.get(categoryselected).add(worker);
                        madapter_6.notifyDataSetChanged();
                    }
                }
                dialog.dismiss();
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });
        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        editText.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }
    //dialog to add worker
    private void showdialog2(final Worker_Object worker, final int position, final int caso){
        final AlertDialog.Builder builder = new AlertDialog.Builder(
        new ContextThemeWrapper(getActivity(),R.style.dialogdatepicker));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v3 = inflater.inflate(R.layout.builder_editworker,null);
        builder.setView(v3);

        spinnerlist_activities = new ArrayList<>(savedinfo.getListasOpciones().get(9));
        spinnerlist_contractors = new ArrayList<>(savedinfo.getListasOpciones().get(4));

        final EditText edittext1 = (EditText)v3.findViewById(R.id.edittext1);
        if(worker!=null){edittext1.setText(worker.getName());}

        ImageButton add_activity = (ImageButton)v3.findViewById(R.id.btn_addactivity);
        ImageButton add_contractor = (ImageButton)v3.findViewById(R.id.btn_addcontractor);
        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_activity_contractor("activity");
            }
        });
        add_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_activity_contractor("contractor");
            }
        });

        spinnerA = (Spinner)v3.findViewById(R.id.spinner1);
        if(!spinnerlist_activities.contains("N/A")){spinnerlist_activities.add(0,"N/A");}
        spinneradapterA = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_simpleitem,spinnerlist_activities);
        spinneradapterA.setDropDownViewResource(R.layout.spinner_dropdownitem);

        spinnerA.setAdapter(spinneradapterA);
        if(worker!=null && spinnerlist_activities.contains(worker.getActivity())){
            spinnerA.setSelection(spinnerlist_activities.indexOf(worker.getActivity()));
        }else {
            spinnerA.setSelection(spinnerlist_activities.indexOf(savedinfo.getListasOpciones().get(0).get(9)));
        }
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savedinfo.getListasOpciones().get(0).set(9,spinnerlist_activities.get(position));
                spinnerA.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerB = (Spinner)v3.findViewById(R.id.spinner2);
        if(!spinnerlist_contractors.contains("N/A")){spinnerlist_contractors.add(0,"N/A");}
        spinneradapterB = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_simpleitem,spinnerlist_contractors);
        spinneradapterB.setDropDownViewResource(R.layout.spinner_dropdownitem);
        spinnerB.setAdapter(spinneradapterB);
        if(worker!=null && spinnerlist_contractors.contains(worker.getCompany())){
            spinnerB.setSelection(spinnerlist_contractors.indexOf(worker.getCompany()));
        }else {
            spinnerB.setSelection(spinnerlist_contractors.indexOf(savedinfo.getListasOpciones().get(0).get(4)));
        }
        spinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savedinfo.getListasOpciones().get(0).set(4,spinnerlist_contractors.get(position));
                spinnerB.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //linearlayout.addView(spinnerB);

        //builder.setView(linearlayout);
        ImageButton accept = (ImageButton)v3.findViewById(R.id.fabaccept);
        ImageButton cancel = (ImageButton)v3.findViewById(R.id.fabcancel);

        final AlertDialog dialog = builder.create();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker_Object worker2;
                if(worker==null){
                    worker2 = new Worker_Object();}
                else {worker2 = worker;}
                worker2.setName(edittext1.getText().toString());
                worker2.setActivity(spinnerA.getSelectedItem().toString());
                worker2.setCompany(spinnerB.getSelectedItem().toString());
                worker2.setHours("8");
                if(position==-1 && caso ==0){//añadir nuevo
                    //si position = -1 estamos anadiendo un nuevo trabajador
                    availableitemsarray.get(categoryselected).add(worker2);}
                else if(position!=-1 && caso==1){//editar de la lista de selected
                    //si position es diferente de -1 estamos editando uno de la lista y caso = 1
                    //hemos selectionado uno de selected reports
                    if(itemstype.equals(manpower)){
                        currentreport.getSelectedWorkers().set(position,worker);
                    }else if (itemstype.equals(equipment)){
                        currentreport.getSelectedEquipment().set(position,worker);
                    }
                }else{ //editar de la la lista de available
                    if(itemstype.equals(manpower)) {
                        for (int i = 0; i < currentreport.getSelectedWorkers().size(); i++) {
                            if (currentreport.getSelectedWorkers().get(i).getIdNumber().equals(worker.getIdNumber())) {
                                currentreport.getSelectedWorkers().set(i, worker);
                            }
                        }
                    }else if (itemstype.equals(equipment)){
                        for (int i = 0; i < currentreport.getSelectedEquipment().size(); i++) {
                            if (currentreport.getSelectedEquipment().get(i).getIdNumber().equals(worker.getIdNumber())) {
                                currentreport.getSelectedEquipment().set(i, worker);
                            }
                        }
                    }
                    availableitemsarray.get(categoryselected).set(position,worker2);
                }
                madapter_6.notifyDataSetChanged();
                dialog.dismiss();
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        edittext1.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
    private void add_activity_contractor(final String activity) {
        AlertDialog.Builder AddItemBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v4 = inflater.inflate(R.layout.builder_simpleadditem,null);
        AddItemBuilder.setView(v4);

        TextView title = (TextView)v4.findViewById(R.id.builder_title);
        if(activity.equals("activity")){title.setText("New Activity");}else {
            title.setText("New Contractor");
        }

        final EditText edittext = (EditText)v4.findViewById(R.id.edittext1);
        ImageButton OK = (ImageButton)v4.findViewById(R.id.fabaccept);
        ImageButton Cancel = (ImageButton)v4.findViewById(R.id.fabcancel);

        final AlertDialog dialog = AddItemBuilder.create();

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edittext.getText().toString().equals("")){
                    dialog.dismiss();
                }else{
                    switch (activity){
                        case "activity":
                            savedinfo.getListasOpciones().get(9).add(edittext.getText().toString());
                            spinnerlist_activities.add(edittext.getText().toString());
                            spinneradapterA.notifyDataSetChanged();
                            break;
                        case "contractor":
                            savedinfo.getListasOpciones().get(4).add(edittext.getText().toString());
                            spinnerlist_contractors.add(edittext.getText().toString());
                            spinneradapterB.notifyDataSetChanged();
                            break;
                    }
                    dialog.dismiss();
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        edittext.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }


}
