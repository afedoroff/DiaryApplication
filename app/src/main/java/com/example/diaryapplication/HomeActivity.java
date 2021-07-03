package com.example.diaryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    DatabaseReference databaseEvents;
    DatabaseReference databasePeople;
    EventsAdapter adapterEvents;
    PeopleAdapter adapterPeople;
    DAOEvent daoEvent;
    DAOPerson daoPerson;
    RecyclerView recyclerViewEvents;
    RecyclerView recyclerViewPeople;
    String key = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewEvents = findViewById(R.id.recyclerviewEvents);
        recyclerViewEvents.setHasFixedSize(true);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewPeople = findViewById(R.id.recyclerviewPeople);
        recyclerViewPeople.setHasFixedSize(true);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(this));

        adapterEvents = new EventsAdapter(this);
        recyclerViewEvents.setAdapter(adapterEvents);
        adapterPeople = new PeopleAdapter(this);
        recyclerViewPeople.setAdapter(adapterPeople);

        daoEvent = new DAOEvent();
        daoPerson = new DAOPerson();

        databaseEvents = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events");
        databasePeople = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("People");
        loadData();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_events:
                                startActivity(new Intent(HomeActivity.this, EventsActivity.class));
                                break;
                            case R.id.nav_logout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                break;
                            case R.id.nav_films:
                                startActivity(new Intent(HomeActivity.this, FilmsActivity.class));
                                break;
                            case R.id.nav_books:
                                startActivity(new Intent(HomeActivity.this, BooksActivity.class));
                                break;
                            case R.id.nav_people:
                                startActivity(new Intent(HomeActivity.this, PeopleActivity.class));
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

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void loadData() {
        daoEvent.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Event> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    event.setKey(dataSnapshot.getKey());
                    LocalDate date = LocalDate.parse(event.getDate(), formatter);
                    if(date.isBefore(LocalDate.now().plusMonths(1))
                            && date.isAfter(LocalDate.now().minusDays(1)))
                        list.add(event);
                    key = databaseEvents.getKey();
                }

                adapterEvents.setItems(sortEvents(list, formatter));
                adapterEvents.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        daoPerson.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Person> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Person person = dataSnapshot.getValue(Person.class);
                    person.setKey(dataSnapshot.getKey());
                    LocalDate date = LocalDate.parse(person.getBirthday(), formatter);
                    int minusYear = date.getYear();
                    int age = (int) ChronoUnit.YEARS.between(date, LocalDate.now());
                    date = date.plusYears(LocalDate.now().getYear()).minusYears(minusYear);
                    person.setBirthday(date.format(formatter));
                    if(date.isBefore(LocalDate.now().plusMonths(1))
                            && date.isAfter(LocalDate.now())) {
                        person.setAge(++age);
                        list.add(person);
                    }
                    key = databaseEvents.getKey();
                }

                adapterPeople.setItems(sortPeople(list));
                adapterPeople.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
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

    private ArrayList<Person> sortPeople(ArrayList<Person> dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        Map<LocalDate, Person> dateFormatMap = new TreeMap<>();
        for (Person person : dates){
            dateFormatMap.put(LocalDate.parse(person.getBirthday(), formatter), person);
        }
        return new ArrayList<>(dateFormatMap.values());
    }

    private ArrayList<Event> sortEvents(ArrayList<Event> dates, DateTimeFormatter formatter) {
        Map<LocalDate, Event> dateFormatMap = new TreeMap<>();
        for (Event event : dates)
            dateFormatMap.put(LocalDate.parse(event.getDate(), formatter), event);
        return new ArrayList<>(dateFormatMap.values());
    }
}