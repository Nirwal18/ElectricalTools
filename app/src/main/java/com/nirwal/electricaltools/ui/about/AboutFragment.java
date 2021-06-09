package com.nirwal.electricaltools.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.AboutFragmentBinding;

public class AboutFragment extends Fragment {

    private AboutViewModel _viewModel;

    AboutFragmentBinding _binding;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        _binding = AboutFragmentBinding.inflate(inflater,container,false);
        _binding.aboutTxt.setOnClickListener((v)->{
            Navigation.findNavController(requireView()).navigate(R.id.action_aboutFragment_to_loginFragment);
        });
        return _binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
    }

}