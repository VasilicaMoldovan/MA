package com.example.lab2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;
import com.example.lab2.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Repository flowerRepository;
        final GetDataService dataService;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        flowerRepository = ((FloralApplication)getApplication()).flowerRepository;
        dataService = ((FloralApplication)getApplication()).dataService;
        //((FloralApplication)getApplication()).checkConnection();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerItems, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Button add = (Button) findViewById(R.id.add_btn);

        Button back = (Button) findViewById(R.id.back_add);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(AddActivity.this, ListAllActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                boolean validParameters = true;

                TextInputEditText nameInput = findViewById(R.id.name_1);
                String name = nameInput.getText().toString();

                TextInputEditText colorInput = findViewById(R.id.color_1);
                String color = colorInput.getText().toString();

                TextInputEditText numberInput = findViewById(R.id.number_1);
                String number = numberInput.getText().toString();

                TextInputEditText priceInput = findViewById(R.id.price_1);
                String price = priceInput.getText().toString();

                String type = spinner.getSelectedItem().toString();

                if (name.equals("") || color.equals("") || number.equals("") || price.equals("") )
                    validParameters = false;

                if(validParameters == true)
                {
                    Flower flower = new Flower(name, type, color, Integer.parseInt(number), Double.parseDouble(price));
                    //((FloralApplication)getApplication()).checkConnection();
                    if (!((FloralApplication)getApplication()).isConnectedToServer()) {
                        flowerRepository.addFlower(flower);
                        startActivity(new Intent(AddActivity.this, ListAllActivity.class));
                    } else {
                        flowerRepository.addFlower(flower);
                        Call<Flower> call = dataService.updateFlower(flower);
                        call.enqueue(new Callback<Flower>() {
                            @Override
                            public void onResponse(Call<Flower> call, Response<Flower> response) {
                                startActivity(new Intent(AddActivity.this, ListAllActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Flower> call, Throwable t) {
                                Toast.makeText(AddActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                else
                {
                    showAlertDialogButtonClicked();
                }
            }
        });
    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please check again all the fields");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}