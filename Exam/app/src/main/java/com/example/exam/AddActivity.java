package com.example.exam;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exam.model.Item;
import com.example.exam.network.ItemAPI;
import com.example.exam.repository.Repository;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    ItemAPI dataService;
    Repository repository;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        dataService = ((ExamApplication)getApplication()).dataService;
        repository = ((ExamApplication)getApplication()).repository;

        Button add = (Button) findViewById(R.id.add2);

        Button back = (Button) findViewById(R.id.back_add);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

        if (((ExamApplication) getApplication()).isOnline()) {


            Button update = (Button)findViewById(R.id.updateBtn);

            update.setOnClickListener(new View.OnClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onClick(View v){
                    Bundle bundle = getIntent().getExtras();
                    final String id1 = bundle.getString("id");

                    final String name1 = bundle.getString("name");

                    EditText nameInput = (EditText) findViewById(R.id.nameId);
                    nameInput.setText(name1);

                    final String level = bundle.getString("level");

                    EditText colorInput = (EditText) findViewById(R.id.detailsId);
                    colorInput.setText(level);

                    final String status = bundle.getString("status");

                    EditText numberInput = (EditText) findViewById(R.id.statusId);
                    numberInput.setText(status);

                    final String from = bundle.getString("from");

                    EditText priceInput = (EditText) findViewById(R.id.timeId);
                    priceInput.setText(from);

                    final String to = bundle.getString("to");

                    EditText toInput = (EditText) findViewById(R.id.typeId);
                    toInput.setText(to);

                    boolean validParameters = true;
                    Log.d("Log: ", "Update call");

                    TextInputEditText nameInput1 = findViewById(R.id.nameId);
                    String name = nameInput1.getText().toString();

                    TextInputEditText statusInput = findViewById(R.id.detailsId);
                    String level1 = statusInput.getText().toString();

                    TextInputEditText timeInput = findViewById(R.id.statusId);
                    String status1 = timeInput.getText().toString();

                    TextInputEditText costInput = findViewById(R.id.timeId);
                    String from1 = costInput.getText().toString();

                    TextInputEditText toInput1 = findViewById(R.id.typeId);
                    String to1 = toInput1.getText().toString();

                    if (name.equals("") || level1.equals("") || from.equals("") || status1.equals("") || to1.equals(""))
                        validParameters = false;

                    if(validParameters == true)
                    {
                        Item order = new Item(Integer.parseInt(id1), name, Integer.parseInt(level), status, Integer.parseInt(from), Integer.parseInt(to));
                        repository.update(Integer.parseInt(id1), name, Integer.parseInt(level), status, Integer.parseInt(from), Integer.parseInt(to));
                        //repository.add(order);

                        Call<Item> call = dataService.update(order);
                        call.enqueue(new Callback<Item>() {
                            @Override
                            public void onResponse(Call<Item> call, Response<Item> response) {
                                Log.d("Title: ", "Add new model to server");
                                startActivity(new Intent(AddActivity.this, MainActivity.class));
                            }
                            @Override
                            public void onFailure(Call<Item> call, Throwable t) {
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
        }


        add.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v){
                boolean validParameters = true;
                Log.d("Log: ", "Add call");

                TextInputEditText nameInput = findViewById(R.id.nameId);
                String name = nameInput.getText().toString();

                TextInputEditText statusInput = findViewById(R.id.detailsId);
                String level = statusInput.getText().toString();

                TextInputEditText timeInput = findViewById(R.id.statusId);
                String status = timeInput.getText().toString();

                TextInputEditText costInput = findViewById(R.id.timeId);
                String from = costInput.getText().toString();

                TextInputEditText toInput = findViewById(R.id.typeId);
                String to = toInput.getText().toString();

                if (name.equals("") || level.equals("") || from.equals("") || status.equals("") || to.equals(""))
                    validParameters = false;

                if(validParameters == true)
                {
                    Item order = new Item(0, name, Integer.parseInt(level), status, Integer.parseInt(from), Integer.parseInt(to));
                    repository.add(order);

                    Call<Item> call = dataService.add(order);
                    call.enqueue(new Callback<Item>() {
                        @Override
                        public void onResponse(Call<Item> call, Response<Item> response) {
                            Log.d("Title: ", "Add new model to server");
                            startActivity(new Intent(AddActivity.this, MainActivity.class));
                        }
                        @Override
                        public void onFailure(Call<Item> call, Throwable t) {
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

    }

    public void showAlertDialogButtonClicked()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please check again all the fields");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

