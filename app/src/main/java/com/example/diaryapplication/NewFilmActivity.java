package com.example.diaryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class NewFilmActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private ImageView backButton;
    private Button addButton;

    private EditText titleET;
    private EditText authorET;
    private EditText descriptionET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_film);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(NewFilmActivity.this, FilmsActivity.class));
        });

        addButton = (Button) findViewById(R.id.add_new_film);

        titleET = (EditText) findViewById(R.id.film_title);
        authorET = (EditText) findViewById(R.id.film_author);
        descriptionET = (EditText) findViewById(R.id.film_description);

        DAOFilm dao = new DAOFilm();
        Film film_edit = (Film) getIntent().getSerializableExtra("EDIT_FILM");
        if(film_edit != null){
            addButton.setText("UPDATE");
            titleET.setText(film_edit.getTitle());
            authorET.setText(film_edit.getAuthor());
            descriptionET.setText(film_edit.getDesc());
        }else{
            addButton.setText("ADD");
        }
        addButton.setOnClickListener(view -> {
            String title = titleET.getText().toString().trim();
            String author = authorET.getText().toString().trim();
            String description = descriptionET.getText().toString().trim();
            Film film = new Film(title, author, description);
            if(film_edit == null) {
                dao.add(film).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Film is added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewFilmActivity.this, FilmsActivity.class));
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }else{
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("author", author);
                hashMap.put("desc", description);
                dao.update(film_edit.getKey(), hashMap).addOnSuccessListener(suc->{
                    Toast.makeText(this, "Film is updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewFilmActivity.this, FilmsActivity.class));
                    finish();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}