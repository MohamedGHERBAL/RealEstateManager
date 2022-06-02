package com.openclassrooms.realestatemanager.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.model.Illustration;
import com.openclassrooms.realestatemanager.repositories.HouseDataRepository;
import com.openclassrooms.realestatemanager.repositories.IllustrationDataRepository;
import com.openclassrooms.realestatemanager.ui.fragment.ItemDetailFragment;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Mohamed GHERBAL (pour OC) on 26/05/2022
 */
public class RealEstateViewModel extends ViewModel {

    // For Log.d .i .e
    private static final String TAG = RealEstateViewModel.class.getSimpleName();

    // For Repositories
    private final HouseDataRepository houseDataSource;
    private final IllustrationDataRepository illustrationDataSource;
    private final Executor executor;

    // For DATAS
    private LiveData<House> currentHouse;

    // RealEstate View Model
    public RealEstateViewModel(HouseDataRepository houseDataSource, IllustrationDataRepository illustrationDataSource, Executor executor) {
        Log.i(TAG, "RealEstateViewModel");

        this.houseDataSource = houseDataSource;
        this.illustrationDataSource = illustrationDataSource;
        this.executor = executor;
    }

    // Init
    public void init(long houseId) {
        Log.i(TAG, "init");

        if (this.currentHouse != null) {
            return;
        }
        currentHouse = houseDataSource.getHouse(houseId);
    }

    // ------------------
    // FOR HOUSE
    // ------------------
    public void createHouse(House house, Context context) {
        Log.i(TAG, "createHouse");

        executor.execute(() -> {
            long id = houseDataSource.createHouse(house);
            if (id >= 0) {
                Log.d(TAG, "createHouse : Success !");

                Utils.createNotification(context.getString(R.string.notif_serv_house_success_title), context.getString(R.string.notif_serv_house_success_msg), context);
            } else {
                Log.d(TAG, "createHouse : Fail !");

                Utils.createNotification(context.getString(R.string.notif_serv_house_fail_title), context.getString(R.string.notif_serv_house_fail_msg), context);
            }
        });
    }

    public void updateIsEuro(boolean isEuro, long id) {
        Log.i(TAG, "updateIsEuro");

        executor.execute(() -> houseDataSource.updateIsEuro(isEuro,id));
    }

    public void updateHouse(String category, String district, boolean isEuro,
                            int price, int area, int numberOfRooms, int numberOfBathrooms,
                            int numberOfBedRooms, int school, int shopping, int publicTransport,
                            int swimmingPool, String description, boolean available,
                            String dateOfEntry, String dateOfSale, String realEstateAgent,
                            long id) {

        Log.i(TAG, "updateHouse");
        executor.execute(() -> houseDataSource.updateHouse(category, district, isEuro, price, area, numberOfRooms,
                numberOfBathrooms, numberOfBedRooms, school, shopping, publicTransport,
                swimmingPool, description, available, dateOfEntry, dateOfSale, realEstateAgent,
                id));
    }

    public void updateIllustration(String illustration, long id) {
        Log.i(TAG, "updateIllustration");

        executor.execute(() -> houseDataSource.updateIllustration(illustration, id));
    }

    public LiveData<List<House>> getAll() {
        Log.i(TAG, "getAll");

        return houseDataSource.getAll();
    }

    public LiveData<House> getHouse(long houseId) {
        Log.i(TAG, "getHouse");

        return  houseDataSource.getHouse(houseId);
    }

    public LiveData<List<House>> getSearchedHouse(String district, int miniPrice, int maxiPrice,
                                                  int miniArea, int maxiArea, int miniRoom, int maxiRoom,
                                                  int school, int shopping, int publicTransport,
                                                  int swimmingPool) {

        Log.i(TAG, "getSearchedHouse");
        return houseDataSource.getSearchedHouse(district, miniPrice, maxiPrice, miniArea, maxiArea,
                miniRoom, maxiRoom, school, shopping, publicTransport, swimmingPool);
    }

    // ------------------
    // FOR ILLUSTRATION
    // ------------------
    public void createIllustration(Illustration illustration) {
        Log.i(TAG, "createIllustration");

        executor.execute(() -> illustrationDataSource.createIllustration(illustration));
    }

    public LiveData<List<Illustration>> getIllustration(long houseId) {
        Log.i(TAG, "getIllustration");

        return illustrationDataSource.getIllustration(houseId);
    }
}
