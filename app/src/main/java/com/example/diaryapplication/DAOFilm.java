package com.example.diaryapplication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOFilm {
    private DatabaseReference databaseReference;
    public DAOFilm(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");
    }

    public Task<Void> add(Film film){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films").push()
                .setValue(film);
    }

    public Task<Void> done(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films").child(key).child("done").setValue(true);
    }

    public Task<Void> rate(String key, float rate){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films").child(key).child("rate").setValue(rate);
    }

    public Task<Void> rated(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films").child(key).child("rated").setValue(true);
    }

    public  Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films")
                .child(key).updateChildren(hashMap);
    }

    public  Task<Void> delete(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Films")
                .child(key).removeValue();
    }

    public Query get(String key){
        if(key == null) {
            return databaseReference
                    .child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child("Films").orderByKey().limitToFirst(8);
        }
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child("Films").startAfter(key).limitToFirst(8);
    }

}
