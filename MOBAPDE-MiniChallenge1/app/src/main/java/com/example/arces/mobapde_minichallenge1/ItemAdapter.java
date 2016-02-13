package com.example.arces.mobapde_minichallenge1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Arces on 10/02/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Expense> itemList;
    private LayoutInflater inflater = null;
    private Context context;
    private View mainView;

    public ItemAdapter(Context context, ArrayList itemList) {
        this.context = context;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(Float.toString(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
        }
    }

    public void removeItem(int itemPosition){
        itemList.remove(itemPosition);
        notifyDataSetChanged();
    }

    public void editItem(final int itemPosition){
        final Expense item = itemList.get(itemPosition);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertView = inflater.inflate(R.layout.alert_dialog, null);

        String name = item.getName();
        final EditText editName = (EditText) alertView.findViewById(R.id.edit_name);
        editName.setHint("Item Name");
        editName.setText(name);

        float price = item.getPrice();
        final EditText editPrice = (EditText) alertView.findViewById(R.id.edit_price);
        editName.setHint("Item Price");
        editPrice.setText(Float.toString(price));

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Edit an Item");
        alert.setMessage("Enter new details about the item:");
        alert.setView(alertView);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                item.setName(editName.getText().toString());
                item.setPrice(Float.parseFloat(editPrice.getText().toString()));
                notifyItemChanged(itemPosition);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing
            }
        });

        alert.show();
    }
}
