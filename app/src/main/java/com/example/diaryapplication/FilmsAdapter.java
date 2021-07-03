package com.example.diaryapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilmsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Film> list = new ArrayList<>();

    public FilmsAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Film> films) {
        list = films;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new FilmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        FilmsViewHolder h = (FilmsViewHolder) holder;
        Film film = list.get(position);
        DAOFilm dao = new DAOFilm();
        h.titleEditText.setText(film.getTitle());
        h.authorEditText.setText(film.getAuthor());
        h.descriptionEditText.setText(film.getDesc());
        h.ratingBar.setRating(film.getRate());
        h.checkBox.setChecked(film.isDone());
        h.ratingBar.setEnabled(false);
        h.checkBox.setEnabled(true);
        if (h.checkBox.isChecked() || film.isRated()) {
            h.ratingBar.setEnabled(true);
            h.ratingBar.setIsIndicator(false);
            h.checkBox.setEnabled(false);
        }
        h.checkBox.setOnClickListener(v -> {
            dao.done(film.getKey()).addOnSuccessListener(suc -> {
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                notifyItemChanged(position);
            }).addOnFailureListener(er -> {
                Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });


        h.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dao.rate(film.getKey(), rating).addOnFailureListener(er -> {
                    Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
                dao.rated(film.getKey()).addOnFailureListener(er -> {
                    Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


        h.optionEditText.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, h.optionEditText);
            popupMenu.inflate(R.menu.option);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.option_edit:
                        Intent intent = new Intent(context, NewFilmActivity.class);
                        intent.putExtra("EDIT_FILM", film);
                        context.startActivity(intent);
                        break;
                    case R.id.option_delete:
                        dao.delete(film.getKey()).addOnSuccessListener(suc -> {
                            Toast.makeText(context, "Film is deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(er -> {
                            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        notifyItemRemoved(position);
                        break;
                }
                return false;
            });
            //
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FilmsViewHolder extends RecyclerView.ViewHolder {
        public TextView titleEditText;
        public TextView authorEditText;
        public TextView descriptionEditText;
        public TextView optionEditText;
        public CheckBox checkBox;
        public RatingBar ratingBar;

        public FilmsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            titleEditText = itemView.findViewById(R.id.titleFB);
            authorEditText = itemView.findViewById(R.id.authorFB);
            descriptionEditText = itemView.findViewById(R.id.descriptionFB);
            optionEditText = itemView.findViewById(R.id.option);
            checkBox = itemView.findViewById(R.id.checkbox);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
