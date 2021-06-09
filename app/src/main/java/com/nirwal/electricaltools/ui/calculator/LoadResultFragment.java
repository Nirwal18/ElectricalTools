package com.nirwal.electricaltools.ui.calculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.electricaltools.databinding.FragmentLoadResultBinding;


public class LoadResultFragment extends Fragment {
    FragmentLoadResultBinding _binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private float _totalLoad;

    public LoadResultFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoadResultFragment newInstance(String param1, String param2) {
        LoadResultFragment fragment = new LoadResultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _totalLoad = getArguments().getFloat("TOTAL_LOAD",0.0f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _binding = FragmentLoadResultBinding.inflate(inflater, container, false);
        _binding.textViewResult.setText(String.valueOf(_totalLoad));
        return _binding.getRoot();
    }



}