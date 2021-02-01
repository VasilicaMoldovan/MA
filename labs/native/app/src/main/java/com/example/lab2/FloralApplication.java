package com.example.lab2;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.lab2.database.DBUtils;
import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;
import com.example.lab2.network.RetrofitClientInstance;
import com.example.lab2.repository.DBRepository;
import com.example.lab2.repository.NetworkRepository;
import com.example.lab2.repository.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloralApplication extends Application {
    public Repository flowerRepository;
    public GetDataService dataService;
    public ConnectionReceiver receiver;
    public IntentFilter intentFilter;

    public class ConnectionReceiver extends BroadcastReceiver {
        public ConnectionReceiver() {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
           if ( isConnectedToServer())
            {
                messageReceived();
                Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
            }
           else {
               Toast.makeText(context, "Action123: " + intent.getAction(), Toast.LENGTH_SHORT).show();
           }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();

        DBUtils dbUtils = new DBUtils(this);
        SQLiteDatabase readableDb = dbUtils.getReadableDatabase();
        SQLiteDatabase writableDb = dbUtils.getWritableDatabase();

        flowerRepository = new DBRepository(readableDb, writableDb);
        if (isConnectedToServer()) {
            dataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        }

        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("Intent.ACTION_INTERNET_MODE_CHANGED");
        registerReceiver(receiver, intentFilter);
    }

    public void messageReceived(){
        if (dataService == null) {
            dataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        }
        final List<Flower> dbFlowers = flowerRepository.getAll();
        Call<List<Flower>> call = dataService.getAll();

        call.enqueue(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
                List<Flower> flowerList = response.body();

                for(Flower flower : dbFlowers) {
                    if ( !containsFlower(flowerList, flower.getName()) ) {
                        Call<Long> call1 = dataService.addFlower(flower);
                        call1.enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                System.out.println("lala");
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                System.out.println("lala");
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Flower>> call, Throwable t) {
                System.out.println("failure");
            }
        });

    }

    public boolean containsFlower(List<Flower> flowers, String flower) {
        for(Flower f : flowers) {
            if (f.getName().equals(flower))
                return true;
        }
        return false;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    public void checkConnection(){
        if (isConnectedToServer() == false) {
            dataService = null;
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isConnectedToServer() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activateNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activateNetwork);
        boolean aux = (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
        return aux;
    }
}
