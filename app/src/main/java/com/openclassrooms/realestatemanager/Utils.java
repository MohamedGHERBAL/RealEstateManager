package com.openclassrooms.realestatemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.model.Illustration;
import com.openclassrooms.realestatemanager.ui.MainHostActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    // For Log.d .i .e
    private static final String TAG = Utils.class.getSimpleName();

    // Variables
    private final static double DOLLARS_UNIT = 0.812; // Actual Currency 0.938775 EUR
    private final static double EUROS_UNIT = 1.065; // Actual Currency EU to US : 1.065114 USD
    static final String CHANNEL_1_ID = "channel1";
    static final String CHANNEL_1_NAME= "Channel 1";

    public static MainHostActivity context = null;

    //----------------------------------------------------------------------------------------------
    // No. 1
    //----------------------------------------------------------------------------------------------

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        Log.i(TAG, "convertDollarToEuro");

        return (int) Math.round(dollars * DOLLARS_UNIT);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     *
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros) {
        Log.i(TAG, "convertEuroToDollar");

        return (int) Math.round(euros * EUROS_UNIT);
    }

    //----------------------------------------------------------------------------------------------
    // No. 2
    //----------------------------------------------------------------------------------------------

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate() {
        Log.i(TAG, "getTodayDate");

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); // OLD Date Format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // New Date Format
        return dateFormat.format(new Date());
    }

    //----------------------------------------------------------------------------------------------
    // No. 3
    //----------------------------------------------------------------------------------------------

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {
        Log.i(TAG, "isInternetAvailable");

        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    /**
     * Vérification de la connexion réseau et wifi
     *
     * @return boolean
     */
    public static boolean checkNetworkAvailable() {
        Log.i(TAG, "checkNetworkAvailable");

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        // Get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;

            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;
        }
        return have_MobileData || have_WIFI;
    }

    //----------------------------------------------------------------------------------------------
    // EXTRA
    //----------------------------------------------------------------------------------------------

    /**
     * Detect device is Android phone or Android tablet
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        Log.i(TAG, "isTablet");

        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void createNotification(String title, String messageContent, Context context) {
        Log.i(TAG, "createNotification");

        Intent resultIntent = new Intent(context , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_1_ID);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_home_24)
                .setContentTitle(title)
                .setContentText(messageContent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_1_ID, CHANNEL_1_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId(CHANNEL_1_ID);
            Objects.requireNonNull(mNotificationManager).createNotificationChannel(notificationChannel);
        }
        Objects.requireNonNull(mNotificationManager).notify(0, mBuilder.build());
    }

    /**
     * Calculate monthly payment
     * Use for LoanSimulator
     */
    public static double calculateMonthlyPayment(double interestRate,
                                                 double loanDuration,
                                                 double loanAmount) {
        Log.i(TAG, "calculateMonthlyPayment");

        double r = interestRate / 1200;
        double r1 = Math.pow(r + 1, loanDuration);
        return ((r + (r / (r1 - 1))) * loanAmount);
    }

    /**
     * Calculate total payment
     * Use for LoanSimulator
     */
    public static double calculateTotalPayment(double monthlyPayment, double loanDuration) {
        Log.i(TAG, "calculateTotalPayment");

        return (monthlyPayment * loanDuration);
    }

    /**
     * Get the illustration stock in device
     * @param house
     * @return illustration
     */
    public static String getIllustrationFromDevice(House house) {
        Log.i(TAG, "getIllustrationFromDevice");

        File file = new File(house.getIllustration());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (bitmap != null) {
            String illustration = getStringImage(bitmap);
            Log.e(TAG, "getIllustrationFromDevice - Bitmap not null");
            return illustration;
        }
        return null;
    }

    /**
     * Get the picture stock in device
     * @param illustration
     * @return illustrationGallery
     */
    public static String getIllustrationGalleryFromDevice(Illustration illustration) {
        Log.i(TAG, "getIllustrationGalleryFromDevice");

        File file = new File(illustration.getPicture());
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmap != null) {
            Log.e(TAG, "getIllustrationGalleryFromDevice - Bitmap not null");

            String illustrationGallery = getStringImage(bitmap);
            return illustrationGallery;
        }
        return null;
    }

    /**
     * Convert a bitmap into a string
     * @param bitmap
     * @return a string
     */
    public static String getStringImage(Bitmap bitmap) {
        Log.i(TAG, "getStringImage");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
