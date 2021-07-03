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

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Event> list = new ArrayList<>();

    public EventsAdapter(Context context){
        this.context =  context;
    }

    public void  setItems(ArrayList<Event> events){
        list = events;
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        EventsViewHolder h = (EventsViewHolder) holder;
        Event event = list.get(position);
        h.titleEditText.setText(event.getTitle());
        h.dateEditText.setText(event.getDate());
        if( context.getClass() == HomeActivity.class){
            h.optionEditText.setClickable(false);
            h.optionEditText.setVisibility(View.GONE);
            return;
        }
        h.optionEditText.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, h.optionEditText);
            popupMenu.inflate(R.menu.option);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.option_edit:
                        Intent intent = new Intent(context, NewEventActivity.class);
                        intent.putExtra("EDIT_EVENT", event);
                        context.startActivity(intent);
                        break;
                    case R.id.option_delete:
                        DAOEvent dao = new DAOEvent();
                        notifyItemRemoved(position);
                        dao.delete(event.getKey()).addOnSuccessListener(suc->{
                            Toast.makeText(context, "Event is deleted", Toast.LENGTH_SHORT).show();
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

    public class EventsViewHolder extends RecyclerView.ViewHolder{
        public TextView titleEditText;
        public TextView dateEditText;
        public TextView optionEditText;

        public EventsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            titleEditText = itemView.findViewById(R.id.eventFB);
            dateEditText = itemView.findViewById(R.id.dateFB);
            optionEditText = itemView.findViewById(R.id.option);
        }
    }
}
