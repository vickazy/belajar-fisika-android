package id.rumahawan.belajarfisika.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.rumahawan.belajarfisika.AddLessonActivity;
import id.rumahawan.belajarfisika.Data.Session;
import id.rumahawan.belajarfisika.LessonDetailActivity;
import id.rumahawan.belajarfisika.Object.Lesson;
import id.rumahawan.belajarfisika.Object.ThreeItems;
import id.rumahawan.belajarfisika.R;
import id.rumahawan.belajarfisika.RecyclerViewAdapter.ThreeItemsListStyle1Adapter;

public class LessonListFragment extends Fragment {
    private ProgressBar progressBar;

    private Session session;
    private Query query;
    private ArrayList<ThreeItems> arrayList;
    private ThreeItemsListStyle1Adapter adapter;

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private LessonListFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final LessonListFragment.ClickListener clicklistener){

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child != null && clicklistener != null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(getContext(), AddLessonActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (session.getSessionString("currentLevel").equals("teacher")){
            inflater.inflate(R.menu.main_activity_menu, menu);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three_items_list, container, false);
        final Context context = getActivity();

        session = new Session(getContext());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://belajar-fisika.firebaseio.com/Lesson");
        query = databaseReference.orderByKey();
        query.keepSynced(true);

        progressBar = rootView.findViewById(R.id.progressBar);
        TextView tvTitleFragment = rootView.findViewById(R.id.tvTitleFragment);
        tvTitleFragment.setText("Lesson List");
        TextView tvListName = rootView.findViewById(R.id.tvListName);
        tvListName.setText("Daftar Pelajaran");

        addData();
        RecyclerView recyclerView = rootView.findViewById(R.id.rcContainer);
        adapter = new ThreeItemsListStyle1Adapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context,
                new LessonListFragment.ClickListener() {
                    @Override
                    public void onClick(View view, final int position) {
                        TextView tvId = view.findViewById(R.id.tvId);
                        startActivity(
                                new Intent(getContext(), LessonDetailActivity.class)
                                .putExtra("id", tvId.getText().toString())
                        );
                    }
                }));

        return rootView;
    }

    private void addData(){
        arrayList = new ArrayList<>();

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (getView() != null) {
                    TextView tvSubtitleFragment = getView().findViewById(R.id.tvSubtitleFragment);
                    tvSubtitleFragment.setText("Registered lesson : " + dataSnapshot.getChildrenCount());
                }

                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Lesson newLesson = postSnapshot.getValue(Lesson.class);
                    arrayList.add(new ThreeItems(newLesson.getId(), R.drawable.ic_local_library_grey_48dp, newLesson.getName(), newLesson.getSubject(), "Level " + newLesson.getLevel()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }

}