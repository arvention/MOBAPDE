package com.example.arces.mobapde_minichallenge1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseSkeletonAdapter extends RecyclerView.Adapter<ExpenseSkeletonAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> mExpenseArrayList;
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater inflater;
    private Activity main;
    private float total;
    private Context context;

    public ExpenseSkeletonAdapter(Context context, final ArrayList<Expense> expenseArrayList){
        this.mExpenseArrayList = expenseArrayList;
        this.inflater = LayoutInflater.from(context);
        this.main = (Activity) context;

        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int positionOfItemClicked) {
                Expense tempex = expenseArrayList.get(positionOfItemClicked);
                MainActivity.showExpense(positionOfItemClicked, tempex.getName(), convertPriceToString(tempex.getPrice()), convertDateToString(tempex.getDate()));
            }
        });

        total = computeTotal();
        Log.d("adapter", "total = " + total);
        MainActivity.showTotalPrice(convertPriceToString(total));
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView name, price;
        public ExpenseViewHolder(View itemView) {
            super(itemView);
            container = (View) itemView.findViewById(R.id.container);
            name = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
        }
    }


    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder holder, int position) {
        Expense expense = mExpenseArrayList.get(position);

        holder.container.setTag(holder);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(((ExpenseViewHolder) v.getTag()).getAdapterPosition());
            }
        });

        holder.name.setText(expense.getName());
        holder.price.setText(convertPriceToString(expense.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mExpenseArrayList.size();
    }

    public String convertPriceToString(double price){
        // This will make your expense's price ready for display
        DecimalFormat df = new DecimalFormat("#.00");
        return "Php " + df.format(price);
    }

    public String convertDateToString(Date d){
        // This will make your expense's date ready for display
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(d);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public Expense getItemAtPosition(int position){
        return mExpenseArrayList.get(position);
    }

    public interface OnItemClickListener{
        public void onItemClick(int positionOfItemClicked);
    }

    public void addExpense(Expense e){
        mExpenseArrayList.add(e);
        total = computeTotal();
        MainActivity.showTotalPrice(convertPriceToString(total));
        notifyDataSetChanged();
    }

    public void editExpense(Expense e, int position){
        Expense ex = mExpenseArrayList.get(position);

        ex.setName(e.getName());
        ex.setPrice(e.getPrice());
        ex.setDate(e.getDate());

        notifyItemChanged(position);
        total = computeTotal();
        MainActivity.showTotalPrice(convertPriceToString(total));
        MainActivity.showEditUpdate(ex.getName(), convertPriceToString(ex.getPrice()), convertDateToString(ex.getDate()));
    }

    public void deleteExpense(int position){
        mExpenseArrayList.remove(position);
        total = computeTotal();
        MainActivity.showTotalPrice(convertPriceToString(total));
        notifyDataSetChanged();
    }

    public float computeTotal(){
        float total = 0;
        for(Expense e : mExpenseArrayList){
            total += e.getPrice();
        }

        return total;
    }
}
