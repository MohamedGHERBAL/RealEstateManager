package com.openclassrooms.realestatemanager.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.DAO.HouseDAO;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.ui.MainHostActivity;

import java.util.List;

/**
 * Created by Mohamed GHERBAL (pour OC) on 26/05/2022
 */
public class HouseDataRepository {

    // For Log.d .i .e
    private static final String TAG = HouseDataRepository.class.getSimpleName();

    // Variables
    private final HouseDAO houseDAO;


    public HouseDataRepository(HouseDAO houseDAO) {
        Log.i(TAG, "HouseDataRepository");

        this.houseDAO = houseDAO;
    }

    // Create House
    public long createHouse(House house) {
        Log.i(TAG, "createHouse");

        return this.houseDAO.createHouse(house);
    }

    // Get House
    public LiveData<House> getHouse(long houseId) {
        Log.i(TAG, "getHouse");

        return this.houseDAO.getHouse(houseId);
    }

    // Get All
    public LiveData<List<House>> getAll() {
        Log.i(TAG, "getAll");

        return this.houseDAO.getAll();
    }

    // Update isEuro
    public void updateIsEuro(boolean isEuro, long id) {
        Log.i(TAG, "updateIsEuro");

        houseDAO.updateIsEuro(isEuro, id);
    }

    // Update Illustration for description picture
    public void updateIllustration(String illustration, long id) {
        Log.i(TAG, "updateIllustration");

        houseDAO.updateIllustration(illustration, id);
    }

    // Update House
    public void updateHouse(String category, String district, boolean isEuro,
                            int price, int area, int numberOfRooms, int numberOfBathrooms,
                            int numberOfBedRooms, int school, int shopping, int publicTransport,
                            int swimmingPool, String description, boolean available,
                            String dateOfEntry, String dateOfSale, String realEstateAgent,
                            long id) {

        Log.i(TAG, "updateHouse");
        houseDAO.updateHouse(category, district, price, isEuro, area, numberOfRooms,
                numberOfBathrooms, numberOfBedRooms, school, shopping, publicTransport,
                swimmingPool, description, available, dateOfEntry, dateOfSale, realEstateAgent, id);
    }

    // For Search
    public LiveData<List<House>> getSearchedHouse(String district,
                                                  int miniPrice, int maxiPrice, int miniArea,
                                                  int maxiArea, int miniRoom, int maxiRoom,
                                                  int school, int shopping, int publicTransport,
                                                  int swimmingPool) {

        Log.i(TAG, "getSearchedHouse");
        return this.houseDAO.getSearchedHouse(district, miniPrice, maxiPrice, miniArea, maxiArea,
                miniRoom, maxiRoom, school, shopping, publicTransport, swimmingPool);
    }

}