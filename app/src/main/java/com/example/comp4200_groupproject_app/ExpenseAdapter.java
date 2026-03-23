package com.example.comp4200_groupproject_app;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    ArrayList<Expense> list;
    Context context;
    DBHelper dbHelper;

    public ExpenseAdapter(Context context, ArrayList<Expense> list) {
        this.context = context;
        this.list = list;
        dbHelper = new DBHelper(context, "PocketPalUsers_database", null, 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texttitle, textcategory, textdate, textamount;

        public ViewHolder(View itemView) {
            super(itemView);
            texttitle = itemView.findViewById(R.id.texttitle);
            textcategory = itemView.findViewById(R.id.textcategory);
            textdate = itemView.findViewById(R.id.textdate);
            textamount = itemView.findViewById(R.id.textamount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense e = list.get(position);

        holder.texttitle.setText(e.title);
        holder.textcategory.setText(e.category);
        holder.textdate.setText(e.date);
        holder.textamount.setText("$" + e.amount);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Expense e = list.get(position);

                new AlertDialog.Builder(context)
                        .setTitle("Delete Expense")
                        .setMessage("Are you sure you want to delete this?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            dbHelper.deleteexpense(e.id);
                            list.remove(position);
                            notifyItemRemoved(position);
                            dbHelper.addBalance(e.amount);

                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
