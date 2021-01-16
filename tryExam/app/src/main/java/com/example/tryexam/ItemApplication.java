package com.example.tryexam;

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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.tryexam.database.DBUtils;
import com.example.tryexam.network.GetDataService;
import com.example.tryexam.network.RetrofitClientInstance;
import com.example.tryexam.repository.DBRepository;
import com.example.tryexam.repository.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemApplication extends Application {
    public GetDataService dataService;
    public ConnectionReceiver receiver;
    public IntentFilter intentFilter;
    public boolean internetConnection = false;
    public Repository repository;

    public class ConnectionReceiver extends BroadcastReceiver {
        public ConnectionReceiver() {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( isConnectedToInternet())
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

        if (isConnectedToInternet()) {
            dataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            internetConnection = true;
        }
        DBUtils dbUtils = new DBUtils(this);
        SQLiteDatabase readableDb = dbUtils.getReadableDatabase();
        SQLiteDatabase writableDb = dbUtils.getWritableDatabase();

        repository = new DBRepository(readableDb, writableDb);

        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("Intent.ACTION_INTERNET_MODE_CHANGED");
        registerReceiver(receiver, intentFilter);
    }

    public void messageReceived(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activateNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activateNetwork);
        boolean aux = (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
        return aux;
    }

}
