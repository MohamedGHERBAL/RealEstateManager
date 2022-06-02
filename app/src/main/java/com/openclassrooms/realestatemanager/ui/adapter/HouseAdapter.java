package com.openclassrooms.realestatemanager.ui.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.databinding.ItemListContentBinding;
import com.openclassrooms.realestatemanager.model.House;

import java.util.List;

/**
 * Created by Mohamed GHERBAL (pour OC) on 30/05/2022
 */
public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    // For Log.d .i .e
    private static final String TAG = HouseAdapter.class.getSimpleName();

    // DATAS
    private final OnHouseListener onHouseListener;
    private List<House> houseList;


    public HouseAdapter(List<House> houseList, OnHouseListener onHouseListener) {
        Log.i(TAG, "HouseAdapter");

        this.houseList = houseList;
        this.onHouseListener = onHouseListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view, onHouseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        holder.updateHouse(houseList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount");

        return houseList.size();
    }

    public void setData(List<House> newData) {
        Log.i(TAG, "setData");

        if (houseList != null) {
            houseList.clear();
            houseList.addAll(newData);
            notifyDataSetChanged();
        } else {
            houseList = newData;
        }
    }

    // Use to detect the click
    public interface OnHouseListener {
        void onHouseClick(int position);
    }

//**************************************************************************************************

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnHouseListener onHouseListener;
        public Resources res;
        private String illustration;

        // Constructor
        public ViewHolder(View itemView, OnHouseListener onHouseListener) {
            super(itemView);
            Log.i(TAG, "ViewHolder");

            this.onHouseListener = onHouseListener;
            itemView.setOnClickListener(this);
        }

        public void updateHouse(House house) {
            Log.e(TAG, "ViewHolder - updateHouse");

            ItemListContentBinding.bind(itemView).fragmentItemCategory.setText(house.getCategory());
            ItemListContentBinding.bind(itemView).fragmentItemCity.setText(house.getDistrict());

            if (!house.isAvailable()) {
                ItemListContentBinding.bind(itemView).fragmentMainItemSaleTv.setVisibility(View.VISIBLE);
                ItemListContentBinding.bind(itemView).fragmentMainItemSaleTv.setText(R.string.sold_the_date + house.getDateOfSale());
            }

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop()
                    .override(100, 100);

            boolean isEuro = house.isEuro();

            if (isEuro == true) {
                ItemListContentBinding.bind(itemView).fragmentItemPrice.setText(String.valueOf(house.getPrice()));
                ItemListContentBinding.bind(itemView).fragmentMainItemChangeIv.setImageResource(R.drawable.ic_baseline_euro_24);
            } else if (!isEuro) {
                ItemListContentBinding.bind(itemView).fragmentItemPrice.setText(String.valueOf(Utils.convertEuroToDollar(house.getPrice())));
                ItemListContentBinding.bind(itemView).fragmentMainItemChangeIv.setImageResource(R.drawable.ic_baseline_dollar_24);
            }

            if (house.getIllustration().isEmpty()) {
                ItemListContentBinding.bind(itemView).fragmentItemImage.setImageResource(R.drawable.sold_logo);

                if (house.isAvailable() == false) {
                    int color = Color.parseColor("#80FF333F");
                    ItemListContentBinding.bind(itemView).fragmentItemImage.setColorFilter(color);
                }
            } else {
                illustration = Utils.getIllustrationFromDevice(house);

                Glide.with(ItemListContentBinding.bind(itemView).fragmentItemImage.getContext())
                        .load(illustration)
                        .apply(myOptions)
                        .into(ItemListContentBinding.bind(itemView).fragmentItemImage);

                if (house.isAvailable() == false) {
                    int color = Color.parseColor("#80FF333F");
                    ItemListContentBinding.bind(itemView).fragmentItemImage.setColorFilter(color);
                }
            }
        }

        @Override
        public void onClick(View view) {
            Log.e(TAG, "ViewHolder - onClick !");

            onHouseListener.onHouseClick(getBindingAdapterPosition());
        }
    }
}
