package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.DAO.HouseDAO;
import com.openclassrooms.realestatemanager.database.DAO.IllustrationDAO;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.model.Illustration;
import com.openclassrooms.realestatemanager.ui.activities.MapActivity;

/**
 * Created by Mohamed GHERBAL (pour OC) on 25/05/2022
 */
@Database(entities = {House.class, Illustration.class}, version = 1, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase {

    // For Log.d .i .e
    private static final String TAG = RealEstateDatabase.class.getSimpleName();

    // --- SINGLETON ---
    private static volatile RealEstateDatabase INSTANCE;

    // --- DAO ---
    public abstract HouseDAO houseDao();
    public abstract IllustrationDAO illustrationDao();


    // --- INSTANCE ---
    public static RealEstateDatabase getInstance(Context context) {
        Log.i(TAG, "getInstance");

        if (INSTANCE == null) {
            synchronized (RealEstateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RealEstateDatabase.class, "RealEstateDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // --- CALLBACK ---
    private static Callback prepopulateDatabase() {
        Log.i(TAG, "prepopulateDatabase");

        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                Log.i(TAG, "prepopulateDatabase - onCreate");
                super.onCreate(db);

/*
                // --- HOUSE Content Values ---
                ContentValues firstHouse = new ContentValues();

                firstHouse.put("id", 1);
                firstHouse.put("category", "Apartment");
                firstHouse.put("district", "PARIS");
                firstHouse.put("price", 500000);
                firstHouse.put("isEuro", true);
                firstHouse.put("area", 200);
                firstHouse.put("numberOfRooms", 4);
                firstHouse.put("numberOfBathrooms", 1);
                firstHouse.put("numberOfBedrooms", 2);
                firstHouse.put("school", false);
                firstHouse.put("shopping", true);
                firstHouse.put("publicTransport", true);
                firstHouse.put("swimmingPool", false);
                firstHouse.put("description", "Magnifique appartement situé à Etienne Marcel...");
                firstHouse.put("illustration", "");
                firstHouse.put("address", "1 rue Etienne Marcel, 75001 PARIS");
                firstHouse.put("available", true);
                firstHouse.put("dateOfEntry", "01/02/2022");
                firstHouse.put("dateOfSale", "null");
                firstHouse.put("realEstateAgent", "James Bond");
                db.insert("House", OnConflictStrategy.IGNORE, firstHouse);


                // --- ILLUSTRATION Content Values ---
                ContentValues firstPicture = new ContentValues();

                firstPicture.put("id", 1);
                firstPicture.put("houseId", 1);
                firstPicture.put("description", "Salon");
                firstPicture.put("picture", "");
                db.insert("Illustration", OnConflictStrategy.IGNORE, firstPicture);


                ContentValues secondePicture = new ContentValues();

                secondePicture.put("id", 2);
                secondePicture.put("houseId", 1);
                secondePicture.put("description", "Cuisine");
                secondePicture.put("picture", "");
                db.insert("Illustration", OnConflictStrategy.IGNORE, secondePicture);
*/
            }
        };
    }
}
