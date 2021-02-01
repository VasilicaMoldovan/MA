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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;
import com.example.lab2.repository.Repository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Repository flowerRepository;
        final GetDataService dataService;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        flowerRepository = ((FloralApplication)getApplication()).flowerRepository;
        dataService = ((FloralApplication)getApplication()).dataService;

        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");

        final String name = bundle.getString("name");

        EditText nameInput = (EditText)findViewById(R.id.name_2);
        nameInput.setText(name);

        final String color = bundle.getString("color");

        EditText colorInput = (EditText)findViewById(R.id.color_2);
        colorInput.setText(color);

        final String number = bundle.getString("number");

        EditText numberInput = (EditText)findViewById(R.id.number_2);
        numberInput.setText(number);

        final String price = bundle.getString("price");

        EditText priceInput = (EditText)findViewById(R.id.price_2);
        priceInput.setText(price);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button back = (Button) findViewById(R.id.upd_back);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(UpdateActivity.this, ListAllActivity.class));
            }
        });

        Button update = (Button) findViewById(R.id.upd_btn);

        update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                boolean validParameters = true;

                EditText name_input = findViewById(R.id.name_2);
                String new_name = name_input.getText().toString();

                EditText color_input = findViewById(R.id.color_2);
                String new_color = color_input.getText().toString();

                EditText number_input = findViewById(R.id.number_2);
                String new_number = number_input.getText().toString();

                EditText price_input = findViewById(R.id.price_2);
                String new_price = price_input.getText().toString();

                if (new_name.equals("") || new_color.equals("") || new_number.equals("") || new_price.equals("") )
                    validParameters = false;

                String new_type = spinner.getSelectedItem().toString();

                if(validParameters == true) {
                    //((FloralApplication)getApplication()).checkConnection();
                    if (!((FloralApplication)getApplication()).isConnectedToServer()) {
                        Toast.makeText(UpdateActivity.this, "Connection is lost! You cannot update anything.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateActivity.this, ListAllActivity.class));
                    } else {
                        flowerRepository.updateFlower(Integer.parseInt(id), new_name, new_type, new_color, Integer.parseInt(new_number), Double.parseDouble(new_price));
                        Flower flower = new Flower(Integer.parseInt(id), new_name, new_type, new_color, Integer.parseInt(new_number), Double.parseDouble(new_price));
                        Call<Flower> call = dataService.updateFlower(flower);
                        call.enqueue(new Callback<Flower>() {
                            @Override
                            public void onResponse(Call<Flower> call, Response<Flower> response) {
                                startActivity(new Intent(UpdateActivity.this, ListAllActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Flower> call, Throwable t) {
                                Toast.makeText(UpdateActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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