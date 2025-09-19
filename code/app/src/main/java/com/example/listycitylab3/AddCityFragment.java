package com.example.listycitylab3;

// import static java.security.AccessController.getContext;   // remove unused import

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int position, City city);
    }

    private static final String ARG_CITY = "arg_city";
    private static final String ARG_POSITION = "arg_position";

    public static AddCityFragment newInstance(@Nullable City city, int position) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_CITY, city);
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        City existing = null;
        int position;
        Bundle args = getArguments();
        if (args != null) {
            existing = (City) args.getSerializable(ARG_CITY);
            position = args.getInt(ARG_POSITION, -1);
        } else {
            position = -1;
        }
        boolean isEdit = existing != null;
        if (isEdit) {
            editCityName.setText(existing.getName());
            editProvinceName.setText(existing.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(isEdit ? "Edit City" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    City updated = new City(cityName, provinceName);
                    if (isEdit) {
                        listener.updateCity(position, updated);
                    } else {
                        listener.addCity(updated);
                    }
                })
                .create();
    }
}
