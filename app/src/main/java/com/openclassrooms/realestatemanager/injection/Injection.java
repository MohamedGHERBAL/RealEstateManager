package com.openclassrooms.realestatemanager.injection;

import android.content.Context;
import android.util.Log;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.repositories.HouseDataRepository;
import com.openclassrooms.realestatemanager.repositories.IllustrationDataRepository;
import com.openclassrooms.realestatemanager.ui.MainHostActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Mohamed GHERBAL (pour OC) on 26/05/2022
 */
public class Injection {

    // For Log.d .i .e
    private static final String TAG = Injection.class.getSimpleName();


    // House Data Repository
    public static HouseDataRepository provideHouseDataSource(Context context) {
        Log.i(TAG, "provideHouseDataSource");

        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new HouseDataRepository(database.houseDao());
    }

    // Illustration Data Repository
    public static IllustrationDataRepository provideIllustrationDataSource(Context context) {
        Log.i(TAG, "provideIllustrationDataSource");

        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new IllustrationDataRepository(database.illustrationDao());
    }

    // VIEW MODEL FACTORY
    public static ViewModelFactory provideViewModelFactory(Context context) {
        Log.i(TAG, "provideViewModelFactory");

        HouseDataRepository dataSourceHouse = provideHouseDataSource(context);
        IllustrationDataRepository dataSourceIllustration = provideIllustrationDataSource(context);
        Executor executor = provideExecutor();

        return new ViewModelFactory(dataSourceIllustration, dataSourceHouse, executor);
    }

    // EXECUTOR
    public static Executor provideExecutor() {
        Log.i(TAG, "provideExecutor");

        return Executors.newSingleThreadExecutor();
    }
}
