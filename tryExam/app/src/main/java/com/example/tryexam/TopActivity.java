package com.example.tryexam;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tryexam.model.Recipe;
import com.example.tryexam.network.GetDataService;
import com.example.tryexam.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopActivity extends AppCompatActivity {
    GetDataService dataService;
    ListView itemListView;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        dataService = ((ItemApplication)getApplication()).dataService;
        repository = ((ItemApplication)getApplication()).repository;

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
            Call<List<Recipe>> call = dataService.getRecipesAscending();
            Button back = (Button) findViewById(R.id.back1Id);
            back.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(TopActivity.this, MainActivity.class));
                }
            });

            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    setTypes(response.body());
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Toast.makeText(TopActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setTypes(List<Recipe> types) {
        itemListView = (ListView) findViewById(R.id.listView2);

        sort(types);
        List<Recipe> top10Recipes = new ArrayList<>();
        for (int i = 0; i < 10 && i < types.size(); i++) {
            top10Recipes.add(types.get(i));
        }
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, top10Recipes);

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertIncrement(view,  (Recipe)parent.getItemAtPosition(position));
            }
        });
    }

    public void showAlertIncrement(View view, final Recipe selectedItem)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to increment this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //((FloralApplication)getApplication()).checkConnection();
                if (!((ItemApplication)getApplication()).isConnectedToInternet()) {
                    Toast.makeText(TopActivity.this, "Connection is lost! You cannot delete anything.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        //repository.updateRecipe(selectedItem.getId(), selectedItem.getName(), selectedItem.getDetails(), selectedItem.getTime(), selectedItem.getType(), selectedItem.getRating() + 1);
                        increment(selectedItem);
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

    public void increment(Recipe recipe) throws IOException {
        Call<Recipe> call = dataService.increment(recipe);
        System.out.println(recipe.getId());
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(TopActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sort(List<Recipe> recipeList) {
        for (int i = 0; i < recipeList.size() - 1; i++) {
            for (int j = i + 1; j < recipeList.size(); j++) {
                if (recipeList.get(i).getRating() > recipeList.get(j).getRating()) {
                    Collections.swap(recipeList, i, j);
                }
            }
        }
    }

}

