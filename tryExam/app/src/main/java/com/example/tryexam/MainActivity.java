package com.example.tryexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tryexam.model.Recipe;
import com.example.tryexam.network.GetDataService;
import com.google.android.material.progressindicator.ProgressIndicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    GetDataService dataService;
    List<String> itemNames = new ArrayList<>();
    ListView itemListView;
    ProgressBar progressBar;
    public static final Logger log = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = ((ItemApplication)getApplication()).dataService;

        Button add = (Button) findViewById(R.id.add1);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        Button rate = (Button) findViewById(R.id.rateBtn);

        rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TopActivity.class));
            }
        });

        /*ProgressIndicator progressIndicator = new ProgressIndicator(this, null, 0,
                R.style.Widget_MaterialComponents_ProgressIndicator_Linear_Indeterminate);*/

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        Button internet;
        if (!((ItemApplication) getApplication()).isConnectedToInternet()) {
            internet = (Button) findViewById(R.id.buttonId);
            internet.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                    startActivity(getIntent());
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Call<List<String>> call = dataService.getAll();

            System.out.println("lala1");
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    System.out.println("lala2");
                    setTypes(response.body());
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    System.out.println("lala2");
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setTypes(List<String> types) {
        System.out.println("lala3");
        log.trace("Get all types={}", types);
        itemListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types);

        itemListView.setAdapter(adapter);

        final Intent appInfo = new Intent(MainActivity.this, ListActivity.class);

        final Bundle bundle = new Bundle();
        progressBar.setVisibility(View.INVISIBLE);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = (String)parent.getItemAtPosition(position);
                bundle.putString("type", type);
                appInfo.putExtras(bundle);
                startActivity(appInfo);
            }
        });
    }
}