package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.databinding.FragmentItemListBinding;

import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.House;
import com.openclassrooms.realestatemanager.ui.MainHostActivity;
import com.openclassrooms.realestatemanager.ui.adapter.HouseAdapter;
import com.openclassrooms.realestatemanager.viewmodel.RealEstateViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListFragment extends Fragment implements HouseAdapter.OnHouseListener, View.OnClickListener {

    // For Log.d .i .e
    private static final String TAG = ItemListFragment.class.getSimpleName();

    // Variables
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 26;
    public static final String BUNDLE_RESULT_LIST = "BUNDLE_RESULT_LIST";

    // For ViewBinding
    FragmentItemListBinding fragmentItemListBinding;

    // For DATAS
    private final List<House> houseList = new ArrayList<>();
    private List<House> searchHouseList = new ArrayList<>();
    private TextView labelNoHouse;
    private HouseAdapter adapter;
    private RecyclerView recyclerView;

    // For ViewModel
    private RealEstateViewModel realEstateViewModel;


    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        // Inflate the layout for this fragment
        fragmentItemListBinding = FragmentItemListBinding.inflate(inflater, container, false);
        View view = fragmentItemListBinding.getRoot();

        recyclerView =  fragmentItemListBinding.itemList;
        labelNoHouse = fragmentItemListBinding.labelNoHouse;

        this.configureRecyclerView();
        this.configureViewModel();

        return view;
    }

    //----------------------------------------------------------------------------------------------
    // Configuration
    //----------------------------------------------------------------------------------------------

    // Configure RecyclerView
    private void configureRecyclerView() {
        Log.d(TAG, "configureRecyclerView");

        this.adapter = new HouseAdapter(this.houseList, this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // Configure ViewModel
    private void configureViewModel() {
        Log.d(TAG, "configureViewModel");

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);

        realEstateViewModel.getAll().observe(getViewLifecycleOwner(), houseList -> {
            adapter.setData(houseList);
            updateDisplay();
        });
    }

    //----------------------------------------------------------------------------------------------
    // DATAS
    //----------------------------------------------------------------------------------------------

    // Update list of house
    private void updateDisplay() {
        Log.d(TAG, "updateDisplay");

        if (houseList.size() == 0) {
            labelNoHouse.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            labelNoHouse.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Use to display the search properties recover from SearchActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");

        if (SEARCH_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {

            searchHouseList = (List<House>) data.getSerializableExtra(BUNDLE_RESULT_LIST);
            adapter.setData(searchHouseList);
        }
    }

    //----------------------------------------------------------------------------------------------
    // OTHERS
    //----------------------------------------------------------------------------------------------

    // Use to display list of available properties when searched result list is display
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");

        configureViewModel();
    }

    @Override
    public void onHouseClick(int position) {
        Log.i(TAG, "onHouseClick");

        ((MainHostActivity) getActivity()).onHouseClick(houseList.get(position));
    }

/*
    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        super.onDestroyView();

        fragmentItemListBinding = null;
    }
*/

}