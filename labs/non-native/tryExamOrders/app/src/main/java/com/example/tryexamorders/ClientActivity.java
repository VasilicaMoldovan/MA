package com.example.tryexamorders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tryexamorders.model.Order;
import com.example.tryexamorders.network.GetDataService;
import com.example.tryexamorders.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends AppCompatActivity {
    GetDataService dataService;
    Repository repository;
    ListView itemListView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);

        dataService = ((MyApplication)getApplication()).dataService;
        repository = ((MyApplication)getApplication()).repository;

        Button search = (Button) findViewById(R.id.searchBtn);

        Button back = (Button) findViewById(R.id.table_back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(ClientActivity.this, MainActivity.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                boolean validParameters = true;

                TextInputEditText nameInput = findViewById(R.id.tableNId);
                String name = nameInput.getText().toString();


                if (name.equals(""))
                    validParameters = false;

                if(validParameters == true)
                {

                    if (((MyApplication)getApplication()).isOnline()) {
                        Call<Order> call = dataService.getDetails(name);
                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                if (response.body() != null) {
                                    setItems(response.body());
                                } else {
                                    showEmptyTable();
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Toast.makeText(ClientActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    showAlertDialogButtonClicked();
                }
            }
        });

    }

    public void setItems(Order item) {
        System.out.println("lala3");
        itemListView = (ListView) findViewById(R.id.listTables);
        List<Order> orderList = new ArrayList<>();
        orderList.add(item);
        ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_list_item_1, orderList);

        itemListView.setAdapter(adapter);
    }

    public void showEmptyTable()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No order for the given table");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please check again all the fields");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showWebSocketDialog(String text, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(text);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

