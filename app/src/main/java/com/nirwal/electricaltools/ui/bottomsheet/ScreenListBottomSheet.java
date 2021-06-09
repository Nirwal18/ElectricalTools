package com.nirwal.electricaltools.ui.bottomsheet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.screen.Screen;
import com.nirwal.electricaltools.ui.adaptors.ScreenListAdaptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     InverterCalculatorListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ScreenListBottomSheet extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_DATA = "DATA";
    private ScreenListAdaptor _screenListAdaptor;

    // TODO: Customize parameters
    public static ScreenListBottomSheet newInstance(List<Screen> data) {
        final ScreenListBottomSheet fragment = new ScreenListBottomSheet();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_DATA, (Serializable) data);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_screen_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        List<Screen> screens =  (ArrayList<Screen>)getArguments().getSerializable(ARG_DATA);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _screenListAdaptor = new ScreenListAdaptor(requireActivity(),screens);

        recyclerView.setAdapter(_screenListAdaptor);
    }

}