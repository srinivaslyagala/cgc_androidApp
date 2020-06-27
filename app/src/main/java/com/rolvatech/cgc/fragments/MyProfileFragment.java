package com.rolvatech.cgc.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rolvatech.cgc.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    RecyclerView recyclerView;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        return root;
    }
}
