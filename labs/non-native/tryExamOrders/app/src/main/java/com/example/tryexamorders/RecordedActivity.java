package com.example.tryexamorders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tryexamorders.model.Order;
import com.example.tryexamorders.network.GetDataService;
import com.example.tryexamorders.repository.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordedActivity extends AppCompatActivity {
    GetDataService dataService;
    Repository repository;
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recorded_activity);

        dataService = ((MyApplication)getApplication()).dataService;
        repository = ((MyApplication)getApplication()).repository;

        Button back = (Button) findViewById(R.id.recorded_back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(RecordedActivity.this, MainActivity.class));
            }
        });

        final RelativeLayout loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        if (((MyApplication)getApplication()).isOnline()) {
            //if (((MyApplication)getApplication()).isConnectedToInternet()) {
            Call<List<Order>> call = dataService.getRecorded();

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
                    Toast.makeText(RecordedActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            System.out.println("lala6");
            showAlertDialogButtonClicked();
            //Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setItems(List<Order> items) {
        System.out.println("lala3");
        System.out.println(items.size());
        itemListView = (ListView) findViewById(R.id.recordedList);
        ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_list_item_1, items);

        itemListView.setAdapter(adapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRecordedDialog((Order)parent.getItemAtPosition(position));
            }
        });
    }

    public void showRecordedDialog(final Order order) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Status");
        final String[] types = {"ready", "preparing", "canceled"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Order order1 = new Order(order.getId(), order.getTable(), order.getDetails(), types[which], order.getTime(), order.getType());
                Call<Order> orderCall = dataService.changeStatus(order1);

                orderCall.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        System.out.println("lala2");
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        System.out.println("lala5");
                        Toast.makeText(RecordedActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        b.show();
    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are offline");
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