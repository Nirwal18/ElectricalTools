package com.nirwal.electricaltools.ui.admin.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.AdminHomeFragmentBinding;

public class AdminHomeFragment extends Fragment {


    private AdminHomeViewModel mViewModel;
    private AdminHomeFragmentBinding _viewBinding;
    


    public static AdminHomeFragment newInstance() {
        return new AdminHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding = AdminHomeFragmentBinding.inflate(inflater,container,false);
        _viewBinding.addCatagoryBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminHomeFragment_to_catagoryManagerFragment);
        });

        _viewBinding.addScreenBtn.setOnClickListener(v -> {

        });

        _viewBinding.uploadAssetsBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminHomeFragment_to_uploadAssetsFragment);
        });

        _viewBinding.profileBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_adminHomeFragment_to_profileFragment);
        });


        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AdminHomeViewModel.class);
        // TODO: Use the ViewModel
    }




}