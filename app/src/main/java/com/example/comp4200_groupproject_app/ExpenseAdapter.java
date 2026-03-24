package com.example.comp4200_groupproject_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    public interface OnExpenseDeletedListener {
        void onExpenseDeleted();
    }

    ArrayList<Expense> list;
    Context context;
    DBHelper dbHelper;
    int userId;
    OnExpenseDeletedListener listener;

    public ExpenseAdapter(Context context, ArrayList<Expense> list,int userId, OnExpenseDeletedListener listener) {
        this.context = context;
        this.list = list;
        this.userId = userId;
        this.listener = listener;
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

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete Expense");
                dialog.setMessage("Are you sure you want to delete this?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        dbHelper.deleteexpense(e.id);
                        list.remove(position);
                        notifyItemRemoved(position);
                        dbHelper.addBalance(userId, e.amount);

                        if (listener != null) {
                            listener.onExpenseDeleted();
                        }

                        Toast.makeText(context.getApplicationContext(), "Deleted Expense", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
