package com.openclassrooms.realestatemanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.model.House;

/**
 * Created by Mohamed GHERBAL (pour OC) on 01/06/2022
 */
public class RealEstateContentProvider extends ContentProvider {

    // For Log.d .i .e
    private static final String TAG = RealEstateContentProvider.class.getSimpleName();

    // For DATAS
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = House.class.getSimpleName();
    public static final Uri URI_HOUSE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME); // For TEST


    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate");

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG, "query");

        if (getContext() != null) {
            long houseId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateDatabase.getInstance(getContext()).houseDao().getHousesWithCursor(houseId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i(TAG, "getType");

        return "vnd.android.cursor.house/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.i(TAG, "insert");

        if (getContext() != null) {
            final long id = RealEstateDatabase.getInstance(getContext()).houseDao().createHouse(House.fromContentValues(contentValues));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG, "delete");

        if (getContext() != null) {
            final int count = RealEstateDatabase.getInstance(getContext()).houseDao().deleteHouse(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG, "update");

        if (getContext() != null) {
            final int count = RealEstateDatabase.getInstance(getContext()).houseDao().updateHouse(
                    contentValues.getAsString("category"),
                    contentValues.getAsString("district"),
                    contentValues.getAsInteger("price"),
                    contentValues.getAsBoolean("isEuro"),
                    contentValues.getAsInteger("area"),
                    contentValues.getAsInteger("numberOfRooms"),
                    contentValues.getAsInteger("numberOfBathrooms"),
                    contentValues.getAsInteger("numberOfBedrooms"),
                    contentValues.getAsInteger("school"),
                    contentValues.getAsInteger("shopping"),
                    contentValues.getAsInteger("publicTransport"),
                    contentValues.getAsInteger("swimmingPool"),
                    contentValues.getAsString("description"),
                    contentValues.getAsBoolean("available"),
                    contentValues.getAsString("dateOfEntry"),
                    contentValues.getAsString("dateOfSale"),
                    contentValues.getAsString("realEstateAgent"),
                    contentValues.getAsLong("houseId"));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
