package com.example.arces.mobapde_minichallenge1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity main = MainActivity.this;
    private RecyclerView rv;
    private static int itemPosition;
    private static Button delBtn;
    private static Button editBtn;
    private static TextView expenseName, expensePrice, expenseDate, instruction;
    private ExpenseSkeletonAdapter expenseAdapter;
    public static ArrayList<Expense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.expenseName = (TextView) findViewById(R.id.item_name);
        this.expensePrice = (TextView) findViewById(R.id.item_price);
        this.expenseDate = (TextView) findViewById(R.id.item_date);
        this.instruction = (TextView) findViewById(R.id.instruction);

        this.delBtn = (Button) findViewById(R.id.deleteBtn);
        this.editBtn = (Button) findViewById(R.id.editBtn);
        createViews();
        this.expenseAdapter = new ExpenseSkeletonAdapter(main, expenseList);
        this.rv.setAdapter(expenseAdapter);
        //initializeItems();
    }

    public void createViews(){
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(main);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void initializeItems(){
        ProgressDialog pd = ProgressDialog.show(MainActivity.this, null, "Initializing Items ...", true, false);
        expenseList.clear();
        Date date = Calendar.getInstance().getTime();
        Expense banana = new Expense();
        banana.setName("Banana");
        banana.setPrice((float) 12.00);
        banana.setDate(date);
        expenseList.add(banana);

        Expense ice_cream = new Expense();
        ice_cream.setName("Ice cream");
        ice_cream.setPrice((float) 20.00);
        ice_cream.setDate(date);
        expenseList.add(ice_cream);

        Expense siomai = new Expense();
        siomai.setName("Siomai");
        siomai.setPrice((float) 45.00);
        siomai.setDate(date);
        expenseList.add(siomai);


        pd.dismiss();
    }
    
    public void addExpense(View view){
        showAlert("add");
    }
    
    public void deleteExpense(View view){
        expenseAdapter.deleteExpense(itemPosition);

        delBtn.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.INVISIBLE);
        expenseName.setVisibility(View.INVISIBLE);

        instruction.setVisibility(View.VISIBLE);
        expensePrice.setVisibility(View.GONE);
        expenseDate.setVisibility(View.GONE);
        expenseName.setText("");
    }

    public void editExpense(View view){
        showAlert("edit");
    }
    
    public static void showExpense(int pos, String name, String price, String date){
        expenseName.setVisibility(View.VISIBLE);
        expenseName.setText(name);
        expensePrice.setText(price);
        expenseDate.setText(date);

        instruction.setVisibility(View.GONE);
        expensePrice.setVisibility(View.VISIBLE);
        expenseDate.setVisibility(View.VISIBLE);
        delBtn.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.VISIBLE);

        itemPosition = pos;
    }
    
    public void showAlert(final String action){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alertView = inflater.inflate(R.layout.alert_dialog, null);
        String name = "";
        float price = (float) 00.00;
        Expense expense = null;

        final EditText alertName = (EditText) alertView.findViewById(R.id.edit_name);
        alertName.setHint("Item Name");

        final EditText alertPrice = (EditText) alertView.findViewById(R.id.edit_price);
        alertPrice.setHint("Item Price");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        
        switch(action){
            case "add":
                alert.setTitle("Add an Expense");
                alert.setMessage("Enter the details about the expense:");
                price = (float)0.00;

                break;
            case "edit":
                alert.setTitle("Edit an Expense");
                alert.setMessage("Enter new details about the item:");
                expense = expenseList.get(itemPosition);

                name = expense.getName();
                price = expense.getPrice();
                break;
        }
        alertName.setText(name);
        alertPrice.setText(Float.toString(price));

        alert.setView(alertView);

        final Expense edEx = expense;
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (alertName.getText().toString().length() <= 0 || alertPrice.getText().toString().length() <= 0) {
                    Toast.makeText(MainActivity.this, "Please set the name and price of the expense", Toast.LENGTH_LONG).show();
                } else {
                    switch (action) {
                        case "add":
                            Expense addEx = new Expense();
                            addEx.setName(alertName.getText().toString());
                            addEx.setPrice(Float.parseFloat(alertPrice.getText().toString()));
                            addEx.setDate(Calendar.getInstance().getTime());
                            expenseAdapter.addExpense(addEx);
                            break;
                        case "edit":
                            edEx.setName(alertName.getText().toString());
                            edEx.setPrice(Float.parseFloat(alertPrice.getText().toString()));
                            edEx.setDate(Calendar.getInstance().getTime());

                            expenseAdapter.editExpense(edEx, itemPosition);
                            break;
                    }
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do nothing
            }
        });

        alert.show();
    }

    public static void showEditUpdate(String name, String price, String date){
        expenseName.setText(name);
        expensePrice.setText(price);
        expenseDate.setText(date);
    }
}
