package com.example.tryexamorders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tryexamorders.model.Order;
import com.example.tryexamorders.network.GetDataService;
import com.example.tryexamorders.repository.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    GetDataService dataService;
    Repository repository;
    ListView itemListView;
    static Context context;
    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        dataService = ((MyApplication)getApplication()).dataService;
        repository = ((MyApplication)getApplication()).repository;

        Button add = (Button) findViewById(R.id.add1);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        Button client = (Button) findViewById(R.id.clientBtn);
        client.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });

        final RelativeLayout loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        if (((MyApplication)getApplication()).isOnline()) {
        //if (((MyApplication)getApplication()).isConnectedToInternet()) {
            Button recorded = (Button) findViewById(R.id.recordId);
            recorded.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(MainActivity.this, RecordedActivity.class));
                }
            });

            Call<List<Order>> call = dataService.getAll();
            loadingPanel.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    System.out.println("lala2");
                    setItems(response.body());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadingPanel.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    System.out.println("lala5");
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
         else {
            System.out.println("lala6");
            setItems(repository.getAll());
            showAlertDialogButtonClicked();
            //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setItems(List<Order> items) {
        System.out.println("lala3");
        System.out.println(items.size());
        itemListView = (ListView) findViewById(R.id.list1);
        ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_list_item_1, items);

        List<Order> dbItems = repository.getAll();

        for(Order i : items) {
            if (!containsItem(dbItems, i)) {
                repository.add(i);
            }
        }

        itemListView.setAdapter(adapter);

        if (((MyApplication)getApplication()).isOnline()) {
            itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showDetails((Order)parent.getItemAtPosition(position));
                }
            });
        }
    }

    public void showDetails(Order order)
    {
        Call<Order> orderCall = dataService.getItem(order.getId());
        final Context context = this;

        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                System.out.println("lala2");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Details");
                builder.setMessage(response.body().toString());
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                System.out.println("lala5");
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are offline");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean containsItem(List<Order> items, Order item){
        for (Order i : items) {
            if (i.getId() == item.getId())
                return true;
        }
        return false;
    }
}