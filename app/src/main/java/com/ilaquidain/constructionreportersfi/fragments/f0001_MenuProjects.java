package com.ilaquidain.constructionreportersfi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilaquidain.constructionreportersfi.R;
import com.ilaquidain.constructionreportersfi.activities.MainActivity;
import com.ilaquidain.constructionreportersfi.fragments_newreport.NewProjectFragment;
import com.ilaquidain.constructionreportersfi.object.Project_Object;
import com.ilaquidain.constructionreportersfi.object.Saved_Info_Object;
import com.ilaquidain.constructionreportersfi.object.SquareImageView;

import java.util.ArrayList;
import java.util.Collections;

public class f0001_MenuProjects extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;
    private ItemTouchHelper mitemTouchHelper;
    private Saved_Info_Object saved_info_object;
    private ArrayList<Project_Object> mProjects;
    private Bitmap bitmaplogo = null;
    private SquareImageView projectlogo;
    private Fragment currentfragment;
    //private final Project_Object currentproject;
    private Bundle bundle;
    private FragmentManager fm;

    private SharedPreferences mpref;
    private SharedPreferences.Editor mprefedit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saved_info_object = ((MainActivity)getActivity()).getSaved_info();
        mProjects = new ArrayList<>();
        if(saved_info_object!=null){
            mProjects = saved_info_object.getSavedProjects();
            if(saved_info_object.getSavedProjects().size()==0){
                Project_Object newproject = new Project_Object();
                newproject.setProjectName("GDBR");
                newproject.setProjectContractNo("HD-7961");
                mProjects.add(newproject);
            }
        }else{
            saved_info_object = new Saved_Info_Object();
            Project_Object newproject = new Project_Object();
            newproject.setProjectName("GDBR");
            newproject.setProjectContractNo("HD-7961");
            mProjects.add(newproject);
        }
        saved_info_object.setSavedProjects(mProjects);
        ((MainActivity)getActivity()).setSaved_info(saved_info_object);
    }

    @Override
    public void onPause() {
        super.onPause();
        saved_info_object.setSavedProjects(mProjects);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f001_00_projectmenu,container,false);

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Project Menu");
        }catch (Exception e){
            e.printStackTrace();
        }

        mpref = getActivity().getPreferences(Context.MODE_PRIVATE);

        currentfragment = getActivity().getFragmentManager().findFragmentById(R.id.MainFrame);
        fm = getActivity().getFragmentManager();

        mRecyclerView = v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectAdapter();
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new HelperCallback14(mAdapter);
        mitemTouchHelper = new ItemTouchHelper(callback);
        mitemTouchHelper.attachToRecyclerView(mRecyclerView);


        //FloatingActionButton fabaddproject = v.findViewById(R.id.fabadd);
        //fabaddproject.setOnClickListener(this);

        return v;
    }

    private class ProjectAdapter extends RecyclerView.Adapter<ProjectViewHolder> implements HelperAdapter14{
        private final OnStarDragListener14 mdraglistener14;

        public ProjectAdapter() {
            super();
            mdraglistener14 = new OnStarDragListener14() {
                @Override
                public void onStartDrag14(RecyclerView.ViewHolder viewHolder) {
                    mitemTouchHelper.startDrag(viewHolder);
                }
            };
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_project,parent,false);
            return new ProjectViewHolder(v2);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            holder.projectname.setText(mProjects.get(position).getProjectName());
            holder.projectrefno.setText(mProjects.get(position).getProjectContractNo());
            holder.projectaddress.setText(mProjects.get(position).getProjectAddress());
            /*String StoredPath2 = mProjects.get(position).getProjectId()+".jpg";
            try{
                File f = new File(getActivity().getApplicationContext().getFilesDir(),StoredPath2);
                Bitmap bitmaplogo = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.projectlogo.setImageBitmap(bitmaplogo);
            }catch (Exception e){
                holder.projectlogo.setImageResource(android.R.color.transparent);
                //holder.projectlogo.setImageResource(R.drawable.constructionreportericon);
                e.printStackTrace();
            }*/
        }

        @Override
        public int getItemCount() {
            return mProjects.size();
        }
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(mProjects,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
            return false;
        }

        @Override
        public void onItemDismiss(final int position) {
            final Project_Object tempproject = mProjects.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete Project");
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to delete this project?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar snackbar = Snackbar
                            .make(mRecyclerView, "PROJECT REMOVED", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mProjects.add(position,tempproject);
                                    notifyItemInserted(position);
                                    mRecyclerView.scrollToPosition(position);
                                }
                            });
                    snackbar.show();
                    mProjects.remove(position);
                    saved_info_object.setSavedProjects(mProjects);
                    ((MainActivity)getActivity()).setSaved_info(saved_info_object);
                    notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notifyDataSetChanged();
                }
            });
            builder.show();

        }
    }
    private class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, HelperViewHolder14{
        final CardView cardView;
        final TextView projectname;
        final TextView projectrefno;
        final TextView projectaddress;
        final SquareImageView projectlogo;
        final ImageButton editproject;
        public ProjectViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.projectcardview);
            RelativeLayout rellay = cardView.findViewById(R.id.rellay_middle);
            editproject = itemView.findViewById(R.id.menu_icon);
            projectname = itemView.findViewById(R.id.projectname);
            projectrefno = itemView.findViewById(R.id.projectrefno);
            projectaddress = itemView.findViewById(R.id.projectaddress);
            projectlogo = itemView.findViewById(R.id.ProjectLogo);
            rellay.setOnClickListener(this);
            editproject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mprefedit = mpref.edit();
            mprefedit.putInt("projectnumber",getAdapterPosition());
            mprefedit.apply();

            switch (v.getId()){
                case R.id.menu_icon:
                    NewProjectFragment newproject = new NewProjectFragment();
                    newproject.setTargetFragment(currentfragment,5001);
                    fm.beginTransaction()
                            .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                            .replace(R.id.MainFrame,newproject,getResources().getString(R.string.level00))
                            .addToBackStack(getResources().getString(R.string.level00))
                            .commit();
                    break;
                case R.id.rellay_middle:
                    f0002_MenuDocuments mainmenuproject = new f0002_MenuDocuments();
                    fm.beginTransaction()
                            .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                            .replace(R.id.MainFrame,mainmenuproject,getResources().getString(R.string.level01))
                            .addToBackStack(getResources().getString(R.string.level01))
                            .commit();
                    break;
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

    @Override
    public void onClick(View v) {
        //Applicacion solo para GDB no se pueden añadir mas proyectos
        /*switch (v.getId()){
            case R.id.fabadd:
                NewProjectFragment newproject = new NewProjectFragment();
                newproject.setTargetFragment(currentfragment,5001);

                mprefedit = mpref.edit();
                mprefedit.putInt("projectnumber",-1);
                mprefedit.apply();

                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_left_enter,R.animator.slide_right_exit)
                        .replace(R.id.MainFrame,newproject,getResources().getString(R.string.level01))
                        .addToBackStack(getResources().getString(R.string.level01))
                        .commit();
                break;
        }*/
    }

    private class HelperCallback14 extends ItemTouchHelper.Callback{
        private static final float ALPHA_FULL = 1.0f;

        private final HelperAdapter14 helperadapter;

        private HelperCallback14(HelperAdapter14 mhelperadapter){
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
            helperadapter.onItemDismiss(viewHolder.getAdapterPosition());
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
                if (viewHolder instanceof HelperViewHolder14) {
                    HelperViewHolder14 itemViewHolder = (HelperViewHolder14) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(ALPHA_FULL);
            if (viewHolder instanceof HelperViewHolder14) {
                HelperViewHolder14 itemViewHolder = (HelperViewHolder14) viewHolder;
                itemViewHolder.onItemClear();
            }
        }
    }
    private interface HelperAdapter14{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
    private interface HelperViewHolder14{
        void onItemSelected();
        void onItemClear();
    }
    private interface OnStarDragListener14{
        void onStartDrag14(RecyclerView.ViewHolder viewHolder);
    }
}
