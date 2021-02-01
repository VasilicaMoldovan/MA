package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;
import com.example.lab2.network.RetrofitClientInstance;
import com.example.lab2.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAllActivity extends AppCompatActivity {
    Repository flowerLabRepository;
    GetDataService dataService;
    List<String> flowerNames = new ArrayList<>();
    ListView flowersListView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        flowerLabRepository = ((FloralApplication)getApplication()).flowerRepository;
        dataService = ((FloralApplication)getApplication()).dataService;

        Button add = (Button) findViewById(R.id.add_btn);

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(ListAllActivity.this, AddActivity.class));
            }
        });
        //((FloralApplication)getApplication()).checkConnection();
        if (((FloralApplication)getApplication()).isConnectedToServer()) {
            Call<List<Flower>> call = dataService.getAll();

            call.enqueue(new Callback<List<Flower>>() {
                @Override
                public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
                    setFlowers(response.body());
                }

                @Override
                public void onFailure(Call<List<Flower>> call, Throwable t) {
                    Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            final List<Flower> flowerList = this.getAllFlowers();
            List<String> flowerNames = flowerLabRepository.getAllFlowerNames();

            this.flowersListView = (ListView) findViewById(R.id.list_all);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flowerNames);

            this.flowersListView.setAdapter(adapter);

            flowersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showAlertDialogButtonClicked(view,position,  (String)parent.getItemAtPosition(position), flowerList);
                }
            });
        }
    }

    public void setFlowers(List<Flower> newF) {
        for (Flower f : newF) {
            this.flowerNames.add(f.getName());
        }

        flowersListView = (ListView) findViewById(R.id.list_all);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flowerNames);
        final List<Flower> flowers = newF;

        flowersListView.setAdapter(adapter);
        flowersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialogButtonClicked(view,position,  (String)parent.getItemAtPosition(position), flowers);
            }
        });
    }

    public List<Flower> getAllFlowers()
    {
        return flowerLabRepository.getAll();
    }

    public void showAlertDelete(View view, final Flower selectedItem)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //((FloralApplication)getApplication()).checkConnection();
                if (!((FloralApplication)getApplication()).isConnectedToServer()) {
                    Toast.makeText(ListAllActivity.this, "Connection is lost! You cannot delete anything.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        flowerLabRepository.deleteFlower(selectedItem);
                        deleteFromServer(selectedItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                recreate();
            }
        });

        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteFromServer(Flower flower) throws IOException {
        Call<Boolean> call = dataService.deleteFlower(Long.valueOf(flower.getId()));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println("aici");
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialogButtonClicked(final View view, final int position, final String selectedItem, final List<Flower> flowerList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent appInfo=new Intent(ListAllActivity.this, UpdateActivity.class);

                Flower currentFlower = flowerList.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(currentFlower.getId()));
                bundle.putString("name", currentFlower.getName());
                bundle.putString("type", currentFlower.getType());
                bundle.putString("color", currentFlower.getColor());
                bundle.putString("number", String.valueOf(currentFlower.getNumber()));
                bundle.putString("price", String.valueOf(currentFlower.getPrice()));

                appInfo.putExtras(bundle);

                startActivity(appInfo);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAlertDelete(view, flowerList.get(position));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}