package com.nirwal.electricaltools.ui.admin.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private ProfileViewModel _viewModel;
    private FragmentProfileBinding _viewBinding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        _viewBinding  = FragmentProfileBinding.inflate(inflater, container,false);


        _viewBinding.profileSignoutBtn.setOnClickListener(v -> signOut());

        return _viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        _viewModel.getCurrentUser().observe(getViewLifecycleOwner(), this::updateUI);


    }

    private void updateUI(FirebaseUser user){
        if(user==null){
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            return;
        }

        _viewBinding.profileSignoutBtn.setVisibility(View.VISIBLE);

        if(user.getPhotoUrl()!=null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(_viewBinding.profilePic);
        }


        _viewBinding.profileUserTitle.setText(user.getDisplayName());
        //_viewBinding.profileUserEmail.setText(user.getEmail());
        _viewBinding.profileUserPhone.setText(user.getPhoneNumber());
    }

    private void signOut(){
        Log.d(TAG, "signOut: ");
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        _viewBinding=null;
    }
}