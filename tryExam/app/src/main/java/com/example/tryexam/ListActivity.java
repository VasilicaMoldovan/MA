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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    GetDataService dataService;
    ListView itemListView;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Bundle bundle = getIntent().getExtras();
        final String type = bundle.getString("type");

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
            Call<List<Recipe>> call = dataService.getAllRecipes(type);
            Button back = (Button) findViewById(R.id.back1Id);
            back.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(ListActivity.this, MainActivity.class));
                }
            });

            System.out.println("lala1");
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    System.out.println("lala2");
                    setTypes(response.body());
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    System.out.println("lala5");
                    Toast.makeText(ListActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setTypes(List<Recipe> types) {
        System.out.println("lala3");
        System.out.println(types.size());
        itemListView = (ListView) findViewById(R.id.listView2);
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1, types);

        List<Recipe> recipeList = repository.getAllRecipes();
        for (Recipe r : types) {
            if (!containsRecipe(recipeList, r)) {
                repository.addRecipe(r);
            }
        }

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDelete(view,  (Recipe)parent.getItemAtPosition(position));
            }
        });
    }

    public void showAlertDelete(View view, final Recipe selectedItem)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!((ItemApplication)getApplication()).isConnectedToInternet()) {
                    Toast.makeText(ListActivity.this, "Connection is lost! You cannot delete anything.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        repository.deleteRecipe(selectedItem);
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

    public void deleteFromServer(Recipe recipe) throws IOException {
        Call<Recipe> call = dataService.deleteFlower(recipe.getId());
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                System.out.println("aici");
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean containsRecipe(List<Recipe> recipeList, Recipe recipe) {
        for (Recipe r : recipeList) {
            if (r.getId() == recipe.getId())
                return true;
        }
        return false;
    }
}
