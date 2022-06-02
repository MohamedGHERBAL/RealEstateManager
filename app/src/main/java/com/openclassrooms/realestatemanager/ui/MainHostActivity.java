package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ActivityItemDetailBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.ui.activities.AddEditActivity;
import com.openclassrooms.realestatemanager.ui.activities.LoanSimulatorActivity;
import com.openclassrooms.realestatemanager.ui.activities.MapActivity;
import com.openclassrooms.realestatemanager.ui.activities.SearchActivity;
import com.openclassrooms.realestatemanager.ui.fragment.ItemDetailFragment;
import com.openclassrooms.realestatemanager.ui.fragment.ItemListFragment;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainHostActivity extends AppCompatActivity {

    // *** MAIN ACTIVITY *** //

    // For Log.d .i .e
    private static final String TAG = MainHostActivity.class.getSimpleName();
    //Log.i(TAG, "onCreate");

    // Variables
    private static final long HOUSE_ID = 1;
    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 26;
    public static final int MAPS_ACTIVITY_REQUEST_CODE = 22;
    private long id;
    private List<House> houseList = new ArrayList<>();

    // For ViewBinding
    private ActivityItemDetailBinding mainBinding;

    // For Config
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private OnBackPressedListener onBackpressedListener;

    // For Fragments
    private ItemListFragment itemListFragment;
    private ItemDetailFragment itemDetailFragment;

    // For ViewModel
    private RealEstateViewModel realEstateViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        mainBinding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        Utils.context = this;

        this.configureToolBar();
        this.configureDrawerMenu();
        this.configureNavigationView();

        this.configureViewModel();
        this.getAllHousesFromDatabase();

        this.configureAndShowMainFragment();
        this.configureAndShowDetailFragment();
    }

    //----------------------------------------------------------------------------------------------
    // Configuration
    //----------------------------------------------------------------------------------------------

    // Configure ItemListFragment
    public void configureAndShowMainFragment() {
        Log.i(TAG, "configureAndShowMainFragment");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (!(fragment instanceof ItemListFragment)) {
            itemListFragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, itemListFragment)
                    .commit();
        }
    }

    // Configure ItemDetailFragment
    public void configureAndShowDetailFragment() {
        Log.i(TAG, "configureAndShowDetailFragment");

        itemDetailFragment = (ItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.item_detail_nav_container);

        if (itemDetailFragment == null && findViewById(R.id.item_detail_nav_container) != null) {
            itemDetailFragment = new ItemDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_nav_container, itemDetailFragment)
                    .commit();
        }
    }

    // Configure ViewModel
    private void configureViewModel() {
        Log.i(TAG, "configureViewModel");

        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        realEstateViewModel = new ViewModelProvider(this, mViewModelFactory).get(RealEstateViewModel.class);
        realEstateViewModel.init(HOUSE_ID);
    }

    // Configure NavigationView
    public void configureNavigationView() {
        Log.d(TAG, "configureNavigationView");

        this.navigationView = mainBinding.navView;
    }

    // Configure Toolbar
    private void configureToolBar() {
        Log.i(TAG, "configureToolBar");

        // Assigning ID of the toolbar to a variable
        Toolbar toolbar = mainBinding.mainToolbar;

        // Using toolbar as ActionBar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE); // Use to define toolbar title color

        // For ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        // Handle actions on menu items
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.main_toolbar_add:
                    Intent intentAddHouse = new Intent(MainHostActivity.this, AddEditActivity.class);
                    startActivity(intentAddHouse);
                    return true;

                case R.id.main_toolbar_modify:
                    if (id != 0) {
                        Intent intentEditHouse = new Intent(MainHostActivity.this, AddEditActivity.class);
                        intentEditHouse.putExtra("id", id);
                        startActivity(intentEditHouse);
                    } else {
                        Toast.makeText(this, "Vous devez selectione un bien éxistant !", Toast.LENGTH_LONG).show();
                    }
                    return true;

                case R.id.main_toolbar_search:
                    Intent intentSearchActivity = new Intent(MainHostActivity.this, SearchActivity.class);
                    startActivityForResult(intentSearchActivity, SEARCH_ACTIVITY_REQUEST_CODE);
                    //registerForActivityResult(intentSearchActivity, SEARCH_ACTIVITY_REQUEST_CODE);
                    return true;

                default:
                    return false;
            }
        });
    }

    // For Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        // Inflate the menu this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // Configure DrawerMenu
    private void configureDrawerMenu() {
        Log.d(TAG, "configureDrawerMenu");

        toggle = new ActionBarDrawerToggle(this, mainBinding.drawer, mainBinding.mainToolbar, R.string.drawer_menu_open, R.string.drawer_menu_close);
        mainBinding.drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        mainBinding.navView.setNavigationItemSelectedListener(item -> {
            Log.d(TAG, "onNavigationItemSelected");

            // Handle Navigation Item Click
            switch (item.getItemId()) {

                case R.id.nav_drawer_MapView:
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.nav_drawer_LoanSimulator:
                    startActivity(new Intent(getApplicationContext(), LoanSimulatorActivity.class));
                    return true;

                default:
                    break;
            }
            return true;
        });
    }

    //----------------------------------------------------------------------------------------------
    // DATAS
    //----------------------------------------------------------------------------------------------

    private void getAllHousesFromDatabase() {
        Log.i(TAG, "getAllHousesFromDatabase");

        this.realEstateViewModel.getAll().observe(this, this::updateList);
    }

    private void updateList(List<House> houses) {
        Log.i(TAG, "updateList");

        houseList = new ArrayList<>();
        houseList.addAll(houses);
    }

    // Transmission of the information of the clicked property for the display of details on DetailsFragment
    public void onHouseClick(House house) {
        Log.i(TAG, "onHouseClick");

        if (itemDetailFragment != null && Utils.isTablet(this)) {
            Log.i(TAG, "onHouseClick - IF");

            // For Tablet
            itemDetailFragment.updateData(house);
            itemDetailFragment.updateDisplay(house);
        } else {
            Log.i(TAG, "onHouseClick - ELSE");

            // For Smartphone
            itemDetailFragment = (ItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout_detail);
            itemDetailFragment = new ItemDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, itemDetailFragment)
                    .addToBackStack(null)
                    .commit();
            itemDetailFragment.onHouseClick(house);
        }
        this.id = house.getId();
    }

    //----------------------------------------------------------------------------------------------
    // OTHERS
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onHouseClick - ELSE");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case SEARCH_ACTIVITY_REQUEST_CODE:
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    fragment.onActivityResult(requestCode, resultCode, data);

                    break;


                case MAPS_ACTIVITY_REQUEST_CODE:
                    // Display house clicked on Smartphone
                    if (!Utils.isTablet(this)) {
                        House houseClicked = (House) data.getSerializableExtra(BUNDLE_HOUSE_CLICKED);
                        onHouseClick(houseClicked);
                    } else {
                        // The result from MapsActivity to DetailFragment
                        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_detail);
                        detailsFragment.onActivityResult(requestCode, resultCode, data);
                    }
                    break;

            }
        }
    }

    // On BackPressed
    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        if (onBackpressedListener != null) {
            getSupportActionBar().setTitle("Map View");
            mainBinding.navView.setCheckedItem(R.id.nav_drawer_MapView);
            onBackpressedListener.doBack();
            mainBinding.drawer.closeDrawer(GravityCompat.START);

        } else if (onBackpressedListener == null) {
            super.onBackPressed();
        }
    }

    // For onBackPressed
    public interface OnBackPressedListener {
        void doBack();
    }

    // For onBackPressed
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        Log.i(TAG, "setOnBackPressedListener");

        this.onBackpressedListener = onBackPressedListener;
    }

/*
    @Override
    public boolean onSupportNavigateUp() {
        Log.i(TAG, "onSupportNavigateUp");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_item_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
*/

}
