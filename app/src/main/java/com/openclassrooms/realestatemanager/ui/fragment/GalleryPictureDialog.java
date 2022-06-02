package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.activities.AddEditActivity;


public class GalleryPictureDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText description;
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_gallery_picture_dialog, null);

        builder.setView(view);
        builder.setPositiveButton("Valider", (dialogInterface, i) -> {
            String pictureDescription = description.getText().toString();
            dialogListener.applyDescription(pictureDescription);
        });
        builder.setNegativeButton("Annuler", (dialogInterface, i) -> {
        });

        Button takePicture = view.findViewById(R.id.gallery_picture_dialog_take_picture_bt);
        Button selectPicture = view.findViewById(R.id.gallery_picture_dialog_select_picture_bt);
        description = view.findViewById(R.id.gallery_picture_dialog_description_et);

        takePicture.setOnClickListener(this);
        selectPicture.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_picture_dialog_take_picture_bt:
                ((AddEditActivity) getActivity()).takePicture();
                break;

            case R.id.gallery_picture_dialog_select_picture_bt:
                ((AddEditActivity) getActivity()).addPictureFromDevice();
                break;
        }
    }

    public interface DialogListener {
        void applyDescription(String pictureDescription);
    }
}
