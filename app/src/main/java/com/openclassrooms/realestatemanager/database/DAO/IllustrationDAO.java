package com.openclassrooms.realestatemanager.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.Illustration;

import java.util.List;

/**
 * Created by Mohamed GHERBAL (pour OC) on 25/05/2022
 */
@Dao
public interface IllustrationDAO {

    @Insert
    long createIllustration(Illustration illustration);

    @Query("SELECT * FROM Illustration WHERE houseId = :houseId")
    LiveData<List<Illustration>> getIllustration(long houseId);

}
