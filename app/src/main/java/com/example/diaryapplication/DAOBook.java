package com.example.diaryapplication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOBook {
    private DatabaseReference databaseReference;
    public DAOBook(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");
    }

    public Task<Void> add(Book book){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books").push()
                .setValue(book);
    }

    public Task<Void> done(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books").child(key).child("done").setValue(true);
    }

    public Task<Void> rate(String key, float rate){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books").child(key).child("rate").setValue(rate);
    }

    public Task<Void> rated(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books").child(key).child("rated").setValue(true);
    }

    public  Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books")
                .child(key).updateChildren(hashMap);
    }

    public  Task<Void> delete(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Books")
                .child(key).removeValue();
    }

    public Query get(String key){
        if(key == null) {
            return databaseReference
                    .child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child("Books").orderByKey().limitToFirst(8);
        }
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child("Books").startAfter(key).limitToFirst(8);
    }

}
