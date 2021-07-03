package com.example.diaryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class EventsActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ImageView addIcon;
    DatabaseReference database;
    EventsAdapter adapter;
    DAOEvent dao;
    boolean isLoading = false;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addIcon = (ImageView) findViewById(R.id.event_add);
        addIcon.setOnClickListener(view -> {
            startActivity(new Intent(EventsActivity.this, NewEventActivity.class));
        });
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.recyclerviewEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventsAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAOEvent();

        database = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events");
        loadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem < lastVisible){
                    if(!isLoading){
                        isLoading = true;
                        loadData();
                    }
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_home:
                                startActivity(new Intent(EventsActivity.this, HomeActivity.class));
                                break;
                            case R.id.nav_logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(EventsActivity.this, MainActivity.class));
                                break;
                            case R.id.nav_films:
                                startActivity(new Intent(EventsActivity.this, FilmsActivity.class));
                                break;
                            case R.id.nav_books:
                                startActivity(new Intent(EventsActivity.this, BooksActivity.class));
                                break;
                            case R.id.nav_people:
                                startActivity(new Intent(EventsActivity.this, PeopleActivity.class));
                                break;
                        }
                        return true;
                    }
                }
        );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_events);
        }
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Event> list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Event event = dataSnapshot.getValue(Event.class);
                    event.setKey(dataSnapshot.getKey());
                    list.add(event);
                    key = database.getKey();
                }
                adapter.setItems(list);
                adapter.notifyDataSetChanged();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}