package com.example.diaryapplication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.HashMap;

public class DAOPerson {
    private DatabaseReference databaseReference;
    public DAOPerson(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users");
    }

    public Task<Void> add(Person person){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("People").push()
                .setValue(person);
    }

    public  Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("People")
                .child(key).updateChildren(hashMap);
    }

    public  Task<Void> delete(String key){
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("People")
                .child(key).removeValue();
    }

    public Query get(String key){
        if(key == null) {
            return databaseReference
                    .child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child("People").orderByKey().limitToFirst(8);
        }
        return databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child("People").startAfter(key).limitToFirst(8);
    }

}
