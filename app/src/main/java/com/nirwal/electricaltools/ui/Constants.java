package com.nirwal.electricaltools.ui;

import com.google.api.LogDescriptor;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.calculator.LoadCalculatorFragment;
import com.nirwal.electricaltools.ui.home.HomeFragment;
import com.nirwal.electricaltools.ui.model.FragmentAddress;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

    public static  final int URI_TYPE_FRAGMENTS = 0;
    public static  final int URI_TYPE_WEBVIEW_FRAG = 1;
    public static  final int URI_TYPE_SCREEN_LIST = 2;

    public static final String FRAGMENT_HOME = HomeFragment.class.getSimpleName();
    public static final String FRAGMENT_LOAD_CALCULATOR = LoadCalculatorFragment.class.getSimpleName();



    public static List<FragmentAddress> getFragmentAddressBook(){
        List<FragmentAddress> addressBook = new ArrayList<>();
        addressBook.add(new FragmentAddress(LoadCalculatorFragment.class.getName(), R.id.loadCalculatorFragment));

        return addressBook;
    }







}
