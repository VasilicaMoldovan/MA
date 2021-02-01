package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.exam.model.Item;
import com.example.exam.network.ItemAPI;
import com.example.exam.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ItemAPI dataService;
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

        dataService = ((ExamApplication)getApplication()).dataService;
        repository = ((ExamApplication)getApplication()).repository;

        final RelativeLayout loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        Button add = (Button) findViewById(R.id.addBtn);

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("Log: ", "Add call");
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        if (((ExamApplication) getApplication()).isOnline()) {
            Button emp = (Button) findViewById(R.id.empBtn);

            emp.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Log.d("Log: ", "Update call");
                    startActivity(new Intent(MainActivity.this, EmployeeAcitvity.class));
                }
            });
        }

        if (((ExamApplication) getApplication()).isOnline()) {
            Button getRule = (Button) findViewById(R.id.getRule);
            getRule.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    TextInputEditText nameInput = findViewById(R.id.idTex1);
                    String id = nameInput.getText().toString();

                    Log.d("Log: ", "Get rule call");

                    Call<Item> call = dataService.getRule(Integer.parseInt(id));

                    call.enqueue(new Callback<Item>() {
                        @Override
                        public void onResponse(Call<Item> call, Response<Item> response) {
                            Log.d("Title: ", "Add new model to server");

                            Intent appInfo=new Intent(MainActivity.this, AddActivity.class);

                            Item item =  response.body();

                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(item.getId()));
                            bundle.putString("name", item.getName());
                            bundle.putString("level", String.valueOf(item.getLevel()));
                            bundle.putString("status", item.getStatus());
                            bundle.putString("from", String.valueOf(item.getFrom()));
                            bundle.putString("to", String.valueOf(item.getTo()));

                            appInfo.putExtras(bundle);

                            startActivity(appInfo);
                            //showRule(response.body());
                            //showDetails(response.body());
                        }
                        @Override
                        public void onFailure(Call<Item> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

        Button getAll = (Button)findViewById(R.id.getAll);

        getAll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (!((ExamApplication)getApplication()).isOnline()) {
                    Toast.makeText(MainActivity.this, "You are offline!", Toast.LENGTH_SHORT).show();
                    setItems(repository.getAll());
                } else {
                    loadingPanel.setVisibility(View.VISIBLE);
                    Log.d("Log:", "Get all operation on server");

                    /* Only if required
                    String userId = ((ExamApplication)getApplication()).sharedPreferences.getString("user_id", "default if empty");*/

                    if (((ExamApplication)getApplication()).hasDoneSync == false) {

                        Call<List<Item>> call = dataService.getAll();

                        call.enqueue(new Callback<List<Item>>() {
                            @Override
                            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
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
                            public void onFailure(Call<List<Item>> call, Throwable t) {
                                System.out.println("lala5");
                                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        setItems(repository.getAll());
                    }
                }
            }
        });
    }

    public void setItems(List<Item> items) {
        System.out.println("lala3");
        itemListView = (ListView) findViewById(R.id.listMain);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);

        itemListView.setAdapter(adapter);

        if (((ExamApplication)getApplication()).hasDoneSync == false) {
            List<Item> dbItems = repository.getAll();

            for (Item i : items){
                if (containsItem(dbItems, i) == false) {
                    repository.add(i);
                }
            }

            ((ExamApplication) getApplication()).syncDone();
        }
    }

    public boolean containsItem(List<Item> items, Item item) {
        for (Item i : items) {
            if (i.getId() == item.getId())
                return true;
        }
        return false;
    }

    public void showRule(Item item)
    {
        Log.d("Log: ", "Details call");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details");
        builder.setMessage(item.toString());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDetails(Item item)
    {
        Call<Item> orderCall = dataService.getItem(item.getId());
        final Context context = this;

        orderCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d("Log: ", "Details call");
                System.out.println("lala2");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Details");
                builder.setMessage(response.body().toString());
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                System.out.println("lala5");
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}