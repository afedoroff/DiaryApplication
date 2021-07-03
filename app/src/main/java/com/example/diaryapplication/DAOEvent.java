package com.example.diaryapplication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOEvent {
    private DatabaseReference databaseReference;
    public DAOEvent(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");
    }

    public Task<Void> add(Event event){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events").push()
                .setValue(event);
    }

    public  Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events")
                .child(key).updateChildren(hashMap);
    }

    public  Task<Void> delete(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events")
                .child(key).removeValue();
    }

    public Query get(String key){
        if(key == null) {
            return databaseReference
                    .child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child("Events").orderByKey().limitToFirst(8);
        }
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child("Events").startAfter(key).limitToFirst(8);
    }

}
