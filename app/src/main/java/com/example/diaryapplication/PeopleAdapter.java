package com.example.diaryapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class PeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Person> list = new ArrayList<>();

    public PeopleAdapter(Context context){
        this.context =  context;
    }

    public void  setItems(ArrayList<Person> people){
        list = people;
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        PeopleViewHolder h = (PeopleViewHolder) holder;
        Person person = list.get(position);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        h.nameEditText.setText(person.getName());
        h.birthdayEditText.setText(person.getBirthday());
        LocalDate birthdayLD = LocalDate.parse(h.birthdayEditText.getText().toString(), dateTimeFormatter);
        if( context.getClass() == HomeActivity.class){
            h.ageEditText.setText(String.valueOf(person.getAge()));
            h.optionEditText.setClickable(false);
            h.optionEditText.setVisibility(View.GONE);
            return;
        }
        h.ageEditText.setText(String.valueOf((int) ChronoUnit.YEARS.between(birthdayLD, LocalDate.now())));
        h.optionEditText.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, h.optionEditText);
            popupMenu.inflate(R.menu.option);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.option_edit:
                        Intent intent = new Intent(context, NewPersonActivity1.class);
                        intent.putExtra("EDIT_PERSON", person);
                        context.startActivity(intent);
                        break;
                    case R.id.option_delete:
                        DAOPerson dao = new DAOPerson();
                        notifyItemRemoved(position);
                        dao.delete(person.getKey()).addOnSuccessListener(suc->{
                            Toast.makeText(context, "Person is deleted", Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            if (list.size() == 0) list = new ArrayList<>();
                        }).addOnFailureListener(er -> {
                            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        public TextView nameEditText;
        public TextView ageEditText;
        public TextView birthdayEditText;
        public TextView optionEditText;

        public PeopleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameEditText = itemView.findViewById(R.id.nameFB);
            ageEditText = itemView.findViewById(R.id.ageFB);
            birthdayEditText = itemView.findViewById(R.id.birthdayFB);
            optionEditText = itemView.findViewById(R.id.option);
        }
    }
}
