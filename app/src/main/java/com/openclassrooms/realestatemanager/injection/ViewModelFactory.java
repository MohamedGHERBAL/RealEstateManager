package com.openclassrooms.realestatemanager.injection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.repositories.HouseDataRepository;
import com.openclassrooms.realestatemanager.repositories.IllustrationDataRepository;
import com.openclassrooms.realestatemanager.ui.MainHostActivity;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.util.concurrent.Executor;

/**
 * Created by Mohamed GHERBAL (pour OC) on 26/05/2022
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    // For Log.d .i .e
    private static final String TAG = ViewModelFactory.class.getSimpleName();

    // For DATAS
    private final IllustrationDataRepository illustrationDataSource;
    private final HouseDataRepository houseDataSource;
    private final Executor executor;

    // View Model Factory
    public ViewModelFactory(IllustrationDataRepository taskDataSource,
                            HouseDataRepository projectDataSource, Executor executor) {
        Log.i(TAG, "ViewModelFactory");

        this.illustrationDataSource = taskDataSource;
        this.houseDataSource = projectDataSource;
        this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Log.i(TAG, "create");

        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            Log.i(TAG, "create - IF");

            return (T) new RealEstateViewModel(houseDataSource, illustrationDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
