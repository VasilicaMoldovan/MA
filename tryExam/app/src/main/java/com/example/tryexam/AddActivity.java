package com.example.tryexam;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tryexam.model.Recipe;
import com.example.tryexam.network.GetDataService;
import com.example.tryexam.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    GetDataService dataService;
    Repository repository;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        dataService = ((ItemApplication)getApplication()).dataService;
        repository = ((ItemApplication)getApplication()).repository;

        if (((ItemApplication)getApplication()).isConnectedToInternet()) {
            Button add = (Button) findViewById(R.id.addBtn);

            Button back = (Button) findViewById(R.id.back2);
            back.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                }
            });

            add.setOnClickListener(new View.OnClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onClick(View v){
                    boolean validParameters = true;

                    TextInputEditText nameInput = findViewById(R.id.nameId);
                    String name = nameInput.getText().toString();

                    TextInputEditText detailsInput = findViewById(R.id.detailsId);
                    String details = detailsInput.getText().toString();

                    TextInputEditText timeInput = findViewById(R.id.timeId);
                    String time = timeInput.getText().toString();

                    TextInputEditText typeInput = findViewById(R.id.typeId);
                    String type = typeInput.getText().toString();

                    TextInputEditText ratingInput = findViewById(R.id.ratingId);
                    String rating = ratingInput.getText().toString();

                    if (name.equals("") ||details.equals("") || time.equals("") || type.equals("") || rating.equals(""))
                        validParameters = false;

                    if(validParameters == true)
                    {
                        Recipe recipe = new Recipe(0, name, details, Integer.parseInt(time), type, Integer.parseInt(rating));
                        repository.addRecipe(recipe);
                        Call<Recipe> call = dataService.addRecipe(recipe);
                        call.enqueue(new Callback<Recipe>() {
                            @Override
                            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                                startActivity(new Intent(AddActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Recipe> call, Throwable t) {
                                Toast.makeText(AddActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else
                    {
                        showAlertDialogButtonClicked();
                    }
                }
            });

        } else {
            Toast.makeText(AddActivity.this, "You are offline. Please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please check again all the fields");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
