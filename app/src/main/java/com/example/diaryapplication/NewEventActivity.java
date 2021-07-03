package com.example.diaryapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;

public class NewEventActivity extends AppCompatActivity{

    private ImageView backButton;
    private Button addButton;

    private EditText titleET;
    private EditText dateET;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(NewEventActivity.this, PeopleActivity.class));
        });

        addButton = (Button) findViewById(R.id.add_new_event);

        titleET = (EditText) findViewById(R.id.event_title);
        dateET = (EditText) findViewById(R.id.event_date);
        dateET.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(NewEventActivity.this,
                    android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
//                String monthS;
//                if(month < 10) monthS = "0" + month;
//                else monthS = "" + month;// TODO
                String date = day + "/" + month + "/" + year;
                dateET.setText(date);
            }
        };

        DAOEvent dao = new DAOEvent();
        Event event_edit = (Event) getIntent().getSerializableExtra("EDIT_EVENT");
        if(event_edit != null){
            addButton.setText("UPDATE");
            titleET.setText(event_edit.getTitle());
            dateET.setText(event_edit.getDate());
        }else{
            addButton.setText("ADD");
        }
        addButton.setOnClickListener(view -> {
            String title = titleET.getText().toString().trim();
            String date = dateET.getText().toString().trim();
            Event event = new Event(title, date);
            if(event_edit == null) {
                dao.add(event).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Event is added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewEventActivity.this, EventsActivity.class));
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }else{
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("date", date);
                dao.update(event_edit.getKey(), hashMap).addOnSuccessListener(suc->{
                    Toast.makeText(this, "Event is updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewEventActivity.this, EventsActivity.class));
                    finish();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}