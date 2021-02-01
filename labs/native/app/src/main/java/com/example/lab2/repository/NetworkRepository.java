package com.example.lab2.repository;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab2.ListAllActivity;
import com.example.lab2.R;
import com.example.lab2.model.Flower;
import com.example.lab2.network.GetDataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRepository implements Repository{
    private GetDataService dataService;
    private List<String> flowerNames;
    private List<Flower> flowers;

    public NetworkRepository(GetDataService dataService) {
        this.dataService = dataService;
    }

    public void setFlowerNames(List<String> flowerNames) {
        this.flowerNames = flowerNames;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
    }

    public void getAllFlowers(){
        Call<List<Flower>> call = dataService.getAll();
        call.enqueue(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
                System.out.println("aici");
                setFlowers(response.body());
            }

            @Override
            public void onFailure(Call<List<Flower>> call, Throwable t) {
                System.out.println("Something went wrong...Please try later!");
                //Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllNames() {
        Call<List<String>> call = dataService.getAllNames();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                System.out.println("aici");
                setFlowerNames(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                System.out.println("Something went wrong...Please try later!");
                //Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public List<Flower> getAll() {
        getAllFlowers();

        return this.flowers;
    }

    @Override
    public List<String> getAllFlowerNames() {
        getAllNames();

        return this.flowerNames;
    }

    @Override
    public boolean addFlower(Flower flower) {
        Call<Long> call = dataService.addFlower(flower);
        final boolean[] returnValue = {false};

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                System.out.println("aici");
                if (response.body() >= 0) {
                    returnValue[0] = true;
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("Something went wrong...Please try later!");
                //Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        return returnValue[0];
    }

    @Override
    public boolean deleteFlower(Flower flower) {
        Call<Boolean> call = dataService.deleteFlower(Long.valueOf(flower.getId()));

        return true;
    }

    @Override
    public Flower updateFlower(int id, String name, String type, String color, int number, double price) {
        Flower newFlower = new Flower(id, name, type, color, number, price);
        Call<Flower> call = dataService.updateFlower(newFlower);
        final Flower[] returnValue = {};

        call.enqueue(new Callback<Flower>() {
            @Override
            public void onResponse(Call<Flower> call, Response<Flower> response) {
                System.out.println("aici");
                returnValue[0] = response.body();
            }

            @Override
            public void onFailure(Call<Flower> call, Throwable t) {
                System.out.println("Something went wrong...Please try later!");
                //Toast.makeText(ListAllActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        return returnValue[0];
    }
}
