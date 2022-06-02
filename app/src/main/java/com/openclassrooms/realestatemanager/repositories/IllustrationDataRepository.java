package com.openclassrooms.realestatemanager.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.DAO.IllustrationDAO;
import com.openclassrooms.realestatemanager.model.Illustration;

import java.util.List;

/**
 * Created by Mohamed GHERBAL (pour OC) on 26/05/2022
 */
public class IllustrationDataRepository {

    // For Log.d .i .e
    private static final String TAG = IllustrationDataRepository.class.getSimpleName();

    // Variables
    private final IllustrationDAO illustrationDAO;


    public IllustrationDataRepository(IllustrationDAO illustrationDao) {
        Log.i(TAG, "IllustrationDataRepository");

        this.illustrationDAO = illustrationDao;
    }

    // Create Illustration
    public void createIllustration(Illustration illustration) {
        Log.i(TAG, "createIllustration");

        illustrationDAO.createIllustration(illustration);
    }

    // Get Illustration
    public LiveData<List<Illustration>> getIllustration(long houseId) {
        Log.i(TAG, "getIllustration");

        return this.illustrationDAO.getIllustration(houseId);
    }

}
