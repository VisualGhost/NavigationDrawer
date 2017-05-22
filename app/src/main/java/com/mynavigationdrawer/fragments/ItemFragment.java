package com.mynavigationdrawer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mynavigationdrawer.MainActivity;
import com.mynavigationdrawer.R;

public class ItemFragment extends Fragment {

    private static final String TAG = ItemFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach "+hashCode());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate "+hashCode());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView "+hashCode());
        View view = inflater.inflate(R.layout.item_layout, container, false);

        TextView textView = (TextView) view.findViewById(R.id.item_text);
        if (getArguments() != null) {
            final String str = getArguments().getString(ItemContract.TEXT_ITEM);
            if (str != null) {
                textView.setText(str);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)getActivity()).showDetails(str);
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated "+hashCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart "+hashCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume "+hashCode());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause "+hashCode());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop "+hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView "+hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy "+hashCode());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach "+hashCode());
    }
}
