package com.openclassrooms.realestatemanager.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.model.Illustration;
import com.openclassrooms.realestatemanager.ui.fragment.ItemDetailFragment;

import java.util.List;

/**
 * Created by Mohamed GHERBAL (pour OC) on 31/05/2022
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    // For Log.d .i .e
    private static final String TAG = GalleryAdapter.class.getSimpleName();

    // Variables
    private List<Illustration> gallery;


    public GalleryAdapter(List<Illustration> gallery) {
        Log.i(TAG, "onCreateViewHolder");

        this.gallery = gallery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "onCreateViewHolder");

        holder.updateIllustration(gallery.get(position));
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "onCreateViewHolder");

        return gallery.size();
    }

    public void setData(List<Illustration> newData) {
        Log.i(TAG, "setData");

        if (gallery != null) {
            gallery.clear();
            gallery.addAll(newData);
        } else {
            gallery = newData;
        }
    }

//--------------------------------------------------------------------------------------------------
// ViewHolder
//--------------------------------------------------------------------------------------------------

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView description;
        private final ImageView illustrationView;
        private String picture;

        // Constructor
        public ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder");


            description = itemView.findViewById(R.id.fragment_detail_item_description_tv);
            illustrationView = itemView.findViewById(R.id.fragment_detail_item_iv);
        }

        public void updateIllustration(Illustration illustration) {
            Log.i(TAG, "ViewHolder - updateIllustration");

            description.setText(illustration.getDescription());

            if (illustration.getPicture().isEmpty()) {
                illustrationView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
            } else {
                picture = Utils.getIllustrationGalleryFromDevice(illustration);

                Glide.with(illustrationView.getContext())
                        .load(picture)
                        .into(illustrationView);
            }
        }
    }
}
