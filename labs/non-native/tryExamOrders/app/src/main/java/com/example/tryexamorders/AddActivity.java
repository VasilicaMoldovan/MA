package com.example.tryexamorders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tryexamorders.model.Order;
import com.example.tryexamorders.network.GetDataService;
import com.example.tryexamorders.repository.Repository;
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

        dataService = ((MyApplication)getApplication()).dataService;
        repository = ((MyApplication)getApplication()).repository;

        Button add = (Button) findViewById(R.id.add2);

        Button back = (Button) findViewById(R.id.back_add);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                boolean validParameters = true;

                TextInputEditText nameInput = findViewById(R.id.tableId);
                String name = nameInput.getText().toString();

                TextInputEditText detailsInput = findViewById(R.id.detailsId);
                String details = detailsInput.getText().toString();

                TextInputEditText statusInput = findViewById(R.id.statusId);
                String status = statusInput.getText().toString();

                TextInputEditText timeInput = findViewById(R.id.timeId);
                String time = timeInput.getText().toString();

                TextInputEditText typeInput = findViewById(R.id.typeId);
                String type = typeInput.getText().toString();

                if (name.equals("") || details.equals("") || time.equals("") || type.equals("") || status.equals(""))
                    validParameters = false;

                if(validParameters == true)
                {
                    Order order = new Order(0, name, details, status, Integer.parseInt(time), type);
                    repository.add(order);

                    if (((MyApplication)getApplication()).isOnline()) {
                        Call<Order> call = dataService.add(order);
                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                startActivity(new Intent(AddActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
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

    public static void showWebSocketDialog(String text, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(text);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
