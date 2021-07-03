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

public class NewBookActivity extends AppCompatActivity{

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
        setContentView(R.layout.activity_new_book);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(NewBookActivity.this, BooksActivity.class));
        });

        addButton = (Button) findViewById(R.id.add_new_book);

        titleET = (EditText) findViewById(R.id.book_title);
        authorET = (EditText) findViewById(R.id.book_author);
        descriptionET = (EditText) findViewById(R.id.book_description);

        DAOBook dao = new DAOBook();
        Book book_edit = (Book) getIntent().getSerializableExtra("EDIT_BOOK");
        if(book_edit != null){
            addButton.setText("UPDATE");
            titleET.setText(book_edit.getTitle());
            authorET.setText(book_edit.getAuthor());
            descriptionET.setText(book_edit.getDesc());
        }else{
            addButton.setText("ADD");
        }
        addButton.setOnClickListener(view -> {
            String title = titleET.getText().toString().trim();
            String author = authorET.getText().toString().trim();
            String description = descriptionET.getText().toString().trim();
            Book book = new Book(title, author, description);
            if(book_edit == null) {
                dao.add(book).addOnSuccessListener(suc -> {
                    Toast.makeText(this, "Book is added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewBookActivity.this, BooksActivity.class));
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }else{
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("author", author);
                hashMap.put("desc", description);
                dao.update(book_edit.getKey(), hashMap).addOnSuccessListener(suc->{
                    Toast.makeText(this, "Book is updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewBookActivity.this, BooksActivity.class));
                    finish();
                }).addOnFailureListener(er -> {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}