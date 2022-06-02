package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.activities.AddEditActivity;


public class DescriptionPictureDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private Button takePicture;
    private Button selectPicture;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_description_picture_dialog, null);

        builder.setView(view)
                .setNegativeButton("Annuler", (dialogInterface, i) -> {
                });
        takePicture = view.findViewById(R.id.gallery_desc_picture_dialog_take_picture_bt);
        selectPicture = view.findViewById(R.id.gallery_desc_picture_dialog_select_picture_bt);

        takePicture.setOnClickListener(this);
        selectPicture.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_desc_picture_dialog_take_picture_bt:
                ((AddEditActivity)getActivity()).takePicture();
                dismiss();
                break;
            case R.id.gallery_desc_picture_dialog_select_picture_bt:
                ((AddEditActivity)getActivity()).addPictureFromDevice();
                dismiss();
                break;
        }
    }
}
