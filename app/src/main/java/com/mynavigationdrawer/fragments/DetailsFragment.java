package com.mynavigationdrawer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mynavigationdrawer.R;


public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_layout, container, false);

        TextView textView = (TextView) view.findViewById(R.id.details_text);
        if (getArguments() != null) {
            final String str = getArguments().getString(ItemContract.DETAILS_ITEM);
            if (str != null) {
                textView.setText(str);
            }
        }
        return view;
    }
}
