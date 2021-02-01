package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;
import com.example.lab2.network.RetrofitClientInstance;
import com.example.lab2.repository.FlowerLabRepository;
import com.example.lab2.repository.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Repository flowerLabRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowerLabRepository = ((FloralApplication)getApplication()).flowerRepository;

        Button start = (Button) findViewById(R.id.start_id);

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, ListAllActivity.class));
            }
        });
    }

}