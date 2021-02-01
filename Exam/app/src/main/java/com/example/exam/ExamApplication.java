package com.example.exam;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.exam.database.DBUtils;
import com.example.exam.network.ItemAPI;
import com.example.exam.network.RetrofitClientInstance;
import com.example.exam.repository.DBRepository;
import com.example.exam.repository.Repository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ExamApplication extends Application {
    public Repository repository;
    public ItemAPI dataService;
    public ConnectionReceiver receiver;
    public IntentFilter intentFilter;
    private static boolean isOffline = false;
    private OkHttpClient client;
    private Context context;
    SharedPreferences sharedPreferences = null;
    public boolean hasDoneSync = false;

    private class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);
            MainActivity.runOnUI(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Action: " + text, Toast.LENGTH_SHORT).show();
                }
            });
            EmployeeAcitvity.runOnUI(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Action: " + text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class ConnectionReceiver extends BroadcastReceiver {
        public ConnectionReceiver() {
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( isConnectedToInternet(context))
            {
                isOffline = false;
                Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
            }
            else {
                isOffline = true;
                Toast.makeText(context, "Action123: " + intent.getAction(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline() {
        return !isOffline;
    }

    public void syncDone() {
        this.hasDoneSync = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();

        DBUtils dbUtils = new DBUtils(this);
        SQLiteDatabase readableDb = dbUtils.getReadableDatabase();
        SQLiteDatabase writableDb = dbUtils.getWritableDatabase();

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

        repository = new DBRepository(readableDb, writableDb);
        if (isConnectedToInternet(getApplicationContext())) {
            context = getApplicationContext();
            dataService = RetrofitClientInstance.getRetrofitInstance().create(ItemAPI.class);
            isOffline = false;
            client = new OkHttpClient();
            Request request = new Request.Builder().url("http://10.0.2.2:2019").build();
            EchoWebSocketListener listener = new EchoWebSocketListener();
            WebSocket ws = client.newWebSocket(request, listener);
            client.dispatcher().executorService().shutdown();
        }

        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isConnectedToInternet(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activateNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activateNetwork);
        return (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
    }
}
