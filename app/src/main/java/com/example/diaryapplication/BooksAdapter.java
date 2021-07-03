package com.example.diaryapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Book> list = new ArrayList<>();

    public BooksAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Book> books) {
        list = books;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        BooksAdapter.BooksViewHolder h = (BooksAdapter.BooksViewHolder) holder;
        Book book = list.get(position);
        DAOBook dao = new DAOBook();
        h.titleEditText.setText(book.getTitle());
        h.authorEditText.setText(book.getAuthor());
        h.descriptionEditText.setText(book.getDesc());
        h.ratingBar.setRating(book.getRate());
        h.checkBox.setChecked(book.isDone());
        h.ratingBar.setEnabled(false);
        h.checkBox.setEnabled(true);
        if (h.checkBox.isChecked() || book.isRated()) {
            h.ratingBar.setEnabled(true);
            h.ratingBar.setIsIndicator(false);
            h.checkBox.setEnabled(false);
        }
        h.checkBox.setOnClickListener(v -> {
            dao.done(book.getKey()).addOnSuccessListener(suc -> {
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
                notifyItemChanged(position);
            }).addOnFailureListener(er -> {
                Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });


        h.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dao.rate(book.getKey(), rating).addOnFailureListener(er -> {
                    Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
                dao.rated(book.getKey()).addOnFailureListener(er -> {
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
                        Intent intent = new Intent(context, NewBookActivity.class);
                        intent.putExtra("EDIT_BOOK", book);
                        context.startActivity(intent);
                        break;
                    case R.id.option_delete:
                        dao.delete(book.getKey()).addOnSuccessListener(suc -> {
                            Toast.makeText(context, "Book is deleted", Toast.LENGTH_SHORT).show();
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

    public class BooksViewHolder extends RecyclerView.ViewHolder {
        public TextView titleEditText;
        public TextView authorEditText;
        public TextView descriptionEditText;
        public TextView optionEditText;
        public CheckBox checkBox;
        public RatingBar ratingBar;

        public BooksViewHolder(@NonNull @NotNull View itemView) {
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
