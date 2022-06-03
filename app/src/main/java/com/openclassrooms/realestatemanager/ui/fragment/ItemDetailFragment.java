package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.model.Illustration;
import com.openclassrooms.realestatemanager.databinding.FragmentItemDetailBinding;
import com.openclassrooms.realestatemanager.ui.activities.MapActivity;
import com.openclassrooms.realestatemanager.ui.adapter.GalleryAdapter;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements View.OnClickListener {

    // For Log.d .i .e
    private static final String TAG = ItemDetailFragment.class.getSimpleName();

    // For ViewBinding
    FragmentItemDetailBinding fragmentItemDetailBinding;

    // Variables
    public static final int MAPS_ACTIVITY_REQUEST_CODE = 22;
    public static final String BUNDLE_HOUSE_CLICKED = "BUNDLE_HOUSE_CLICKED";
    private static final long HOUSE_ID = 1;
    private final List<Illustration> gallery = new ArrayList<>();

    private TextView surface, rooms, bedrooms, bathrooms;
    private ImageView mapView;
    private House house;
    private long id;

    // DATAS
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private RealEstateViewModel realEstateViewModel;


    // Required empty public constructor
    public ItemDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

        // Inflate the layout for this fragment
        fragmentItemDetailBinding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View rootView = fragmentItemDetailBinding.getRoot();

        recyclerView = fragmentItemDetailBinding.fragmentDetailRecyclerview;
        mapView = fragmentItemDetailBinding.fragmentDetailMinimaps;

        this.initView();
        this.configureViewModel();
        mapView.setOnClickListener(this);

        return rootView;
    }

    //----------------------------------------------------------------------------------------------
    // Initialization
    //----------------------------------------------------------------------------------------------

    // initViews
    private void initView() {
        Log.e(TAG, "initView");

        surface = fragmentItemDetailBinding.fragmentDetailSurfaceValue;
        rooms = fragmentItemDetailBinding.fragmentDetailRoomsValue;
        bedrooms = fragmentItemDetailBinding.fragmentDetailBedroomsValue;
        bathrooms = fragmentItemDetailBinding.fragmentDetailBathroomsValue;
    }

    //----------------------------------------------------------------------------------------------
    // Configuration
    //----------------------------------------------------------------------------------------------

    // Configure RecyclerView
    public void configureRecyclerView() {
        Log.e(TAG, "configureRecyclerView");

        this.adapter = new GalleryAdapter(this.gallery);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    // Configure ViewModel
    private void configureViewModel() {
        Log.e(TAG, "configureViewModel");

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
        this.realEstateViewModel.init(HOUSE_ID);
    }

    //----------------------------------------------------------------------------------------------
    // DATAS
    //----------------------------------------------------------------------------------------------

    private void getGalleryHouseFromDatabase(long houseId) {
        Log.e(TAG, "getGalleryHouseFromDatabase");

        this.realEstateViewModel.getIllustration(houseId).observe(this, illustrations -> {
            adapter.setData(illustrations);
            updateDisplayList();
        });
    }


    // Display for tablet
    public void updateData(House house) {
        Log.e(TAG, "updateData");

        updateHouseDetails(house);
        configureViewModel();
        configureRecyclerView();
        getGalleryHouseFromDatabase(house.getId());
    }

    // Update the display
    public void updateDisplay(House house) {
        Log.e(TAG, "updateDisplay");

        if (house == null) {
            getView().setVisibility(View.GONE);
        } else {
            getView().setVisibility(View.VISIBLE);
        }
    }

    // Update the display of the list
    private void updateDisplayList() {
        Log.e(TAG, "updateDisplayList");

        if (gallery.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            configureRecyclerView();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    // Fill in the editText
    public void updateHouseDetails(House house) {
        Log.e(TAG, "updateHouseDetails");

        surface.setText(String.valueOf(house.getArea()));
        rooms.setText(String.valueOf(house.getNumberOfRooms()));
        bedrooms.setText(String.valueOf(house.getNumberOfBedrooms()));
        bathrooms.setText(String.valueOf(house.getNumberOfBathrooms()));

        List<String> pois = new ArrayList<>();
        if (house.getSchool() == 1) {
            pois.add("école");
        }
        if (house.getShopping() == 1) {
            pois.add("commerce");
        }
        if (house.getPublicTransport() == 1) {
            pois.add("transport");
        }
        if (house.getSwimmingPool() == 1) {
            pois.add("piscine");
        }
        String list = "";
        for (String poi : pois) {
            list = list + " " + poi;
        }

        fragmentItemDetailBinding.fragmentDetailIpTvValue.setText(list);
        fragmentItemDetailBinding.fragmentDetailAddressValue.setText(house.getAddress());
        fragmentItemDetailBinding.fragmentDetailDescriptionValue.setText(house.getDescription());
        fragmentItemDetailBinding.fragmentDetailAgentValue.setText(house.getRealEstateAgent());
        fragmentItemDetailBinding.fragmentDetailDateOfEntryValue.setText(house.getDateOfEntry());

        // Display Real Estate on StaticMap
        Glide.with(mapView.getContext())
                .load(convertAndShowAddressOnStaticMap(house.getAddress()))
                .into(mapView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult");

        // Display house clicked on MapsActivity on smartphone
        if (MAPS_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            Log.e(TAG, "onActivityResult - IF");

            House houseClicked = (House) data.getSerializableExtra(BUNDLE_HOUSE_CLICKED);
            updateData(houseClicked);
            this.house = houseClicked;
        }
    }

    //----------------------------------------------------------------------------------------------
    // OTHERS
    //----------------------------------------------------------------------------------------------

    // Convert Address of the RealEstate and show on the StaticMap
    public String convertAndShowAddressOnStaticMap(String address) {
        Log.e(TAG, "convertAndShowAddressOnStaticMap");

        Geocoder coder = new Geocoder(getContext());
        List<Address> addresses;

        try {
            addresses = coder.getFromLocationName(address, 10);

            if (addresses == null) {
            }

            Address location = addresses.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200" +
                    "&markers=color:red%7C" + lat + "," + lng + "&sensor=false&key=AIzaSyB9cBXJpDqmm_y_NfoEeAzB2bmbQuBO5Y4";
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onHouseClick(House house) {
        Log.e(TAG, "OnHouseClick !" + house);

        if (house != null) {
            this.house = house;

            // Use for EditActivity
            this.id = house.getId();
        }
    }

    @Override
    public void onStart() {
        Log.e(TAG, "onStart" + house);

        // Smartphone
        if (house != null) {
            Log.e(TAG, "onStart - Smartphone" + house);
            this.updateData(house);
        }

        // Tablet
        this.updateDisplay(house);
        super.onStart();
    }

    public void onClick(View view) {
        // Start MapView if user has network

        if (Utils.checkNetworkAvailable()) {
            Log.e(TAG, "onClick");

            Intent intent = new Intent(getActivity(), MapActivity.class);
            getActivity().startActivityForResult(intent, MAPS_ACTIVITY_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }


}