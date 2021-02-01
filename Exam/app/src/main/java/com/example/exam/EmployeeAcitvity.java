package com.example.exam;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exam.model.Item;
import com.example.exam.network.ItemAPI;
import com.example.exam.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAcitvity extends AppCompatActivity {
    ItemAPI dataService;
    Repository repository;
    ListView itemListView;

    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_acitivity);

        Button show1 = (Button) findViewById(R.id.show1);

        final RelativeLayout loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);

        dataService = ((ExamApplication)getApplication()).dataService;
        repository = ((ExamApplication)getApplication()).repository;

        show1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                TextInputEditText costInput = findViewById(R.id.fromId);
                String from = costInput.getText().toString();
                final int from1 = Integer.parseInt(from);

                TextInputEditText toInput = findViewById(R.id.toId);
                String to = toInput.getText().toString();
                final int to1 = Integer.parseInt(to);

                if (!((ExamApplication)getApplication()).isOnline()) {
                    Toast.makeText(EmployeeAcitvity.this, "You are offline!", Toast.LENGTH_SHORT).show();
                    setItems(repository.getAll(), from1, to1);
                } else {
                    loadingPanel.setVisibility(View.VISIBLE);
                    Log.d("Log:", "Get all operation on server");

                    /* Only if required
                    String userId = ((ExamApplication)getApplication()).sharedPreferences.getString("user_id", "default if empty");*/

                    //if (((ExamApplication)getApplication()).hasDoneSync == false) {

                        Call<List<Item>> call = dataService.getAll();

                        call.enqueue(new Callback<List<Item>>() {
                            @Override
                            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                                System.out.println("lala2");
                                setItems(response.body(), from1, to1);
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
                                Toast.makeText(EmployeeAcitvity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            //}
        });
    }

    public void setItems(List<Item> items, int from, int to) {
        System.out.println("lala3");
        itemListView = (ListView) findViewById(R.id.listEmp);
        List<Item> result = getItemsFromTo(items, from, to);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, result);

        itemListView.setAdapter(adapter);

    }

    public List<Item> getItemsFromTo(List<Item> items, int from, int to) {
        List<Item> items1 = new ArrayList<>();

        for (Item i : items ){
            if (i.getFrom() >= from && i.getTo() <= to) {
                items1.add(i);
            }
        }
        return items1;
    }
}