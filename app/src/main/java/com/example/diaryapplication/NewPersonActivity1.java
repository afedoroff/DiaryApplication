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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import java.util.HashMap;


public class NewPersonActivity1 extends AppCompatActivity{

    private ImageView backButton;
    private Button addButton;

    private EditText nameET;
    private EditText birthdayET;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person1);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(NewPersonActivity1.this, PeopleActivity.class));
        });

        addButton = (Button) findViewById(R.id.add_new_person);

        nameET = (EditText) findViewById(R.id.person_name);
        birthdayET = (EditText) findViewById(R.id.person_birthday);
        birthdayET.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(NewPersonActivity1.this,
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
                birthdayET.setText(date);
            }
        };

        DAOPerson dao = new DAOPerson();
        Person person_edit = (Person) getIntent().getSerializableExtra("EDIT_PERSON");
        if(person_edit != null){
            addButton.setText("UPDATE");
            nameET.setText(person_edit.getName());
            birthdayET.setText(person_edit.getBirthday());
        }else{
            addButton.setText("ADD");
        }
        addButton.setOnClickListener(view -> {
            String name = nameET.getText().toString().trim();
            String birthday = birthdayET.getText().toString().trim();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate birthdayLD = LocalDate.parse(birthday, dateTimeFormatter);
            int age = LocalDate.now().getYear() - birthdayLD.getYear();
            Person person = new Person(name, birthday, age);
            if(person_edit == null) {
                dao.add(person).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Person is added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPersonActivity1.this, PeopleActivity.class));
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }else{
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("birthday", birthday);
                hashMap.put("age", age);
                dao.update(person_edit.getKey(), hashMap).addOnSuccessListener(suc->{
                    Toast.makeText(this, "Person is updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPersonActivity1.this, PeopleActivity.class));
                    finish();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}